package model.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import controller.GameSceneController;
import model.entities.Mission.MISSION_TYPE;
import model.util.FileHandler;

public class RisikoGame {
	
	public enum GAME_PHASE{
		FIRSTTURN, REINFORCEMENT, BATTLE, FINALMOVE;
	}
	
	private Player[] players;
	private ArrayList<Territory> territories;
	private ArrayList<Continent> continents;
	private ArrayList<Mission> missions;
	private ArrayList<Card> cards;
	private FileHandler fileHandler = new FileHandler();
	private GAME_PHASE gamePhase;
	private Player currentTurn;
	private int turnCounter;
	private boolean conquerMade;
	
	/**
	 * Creates and initializes a RisikoGame
	 * @param players are the players of the game
	 * @param terrFile is the file containing the territories
	 * @param continentsFile is the file containing the continents
	 * @param missionsFile is the file containing the missions
	 * @throws NumberFormatException if there are problems with the parsing
	 * @throws IOException if there is a problem with the file
	 */
	public RisikoGame(Player[] players, String terrFile, String continentsFile, String missionsFile) throws NumberFormatException, IOException {
		this.players = players;
		this.players = shufflePlayers();
		
		giveStarterTanks();
		
		territories = fileHandler.addConfinanti(fileHandler.genTerritories(terrFile), terrFile);
		continents = fileHandler.genContinents(continentsFile);
		for (Continent c : continents) {
			setContinent(c);	
		}
		
		missions = fileHandler.genMissions(missionsFile, continents);  // per funzionamento corretto partita (definitivo)
//		missions = fileHandler.genMissions("assets/obiettiviTest.txt", continents);  // per testare la vittoria
		giveMissions();
		
		cards = fileHandler.genCards(territories, terrFile);
		shuffleCards();
		
		initTerritoryOwners();
		
		setGamePhase(GAME_PHASE.FIRSTTURN);
		
		turnCounter = 0;
		currentTurn = this.players[turnCounter];
		if(currentTurn.isAI()) {
			nextTurn();
		}
		conquerMade = false;	
	}
	
	/**
	 * The games goes to the next turn
	 */
	public void nextTurn(){
		turnCounter++;
		if(turnCounter == players.length) {
			turnCounter = 0;
		}
		currentTurn = this.players[turnCounter];
		if(currentTurn.isAI() && gamePhase == GAME_PHASE.FIRSTTURN) {
			currentTurn.playTurn();
		}
		if(currentTurn.isEliminated()) {
			nextTurn();
		}
	}
	
	/**
	 * The game goes to the next phase
	 */
	public void nextPhase() {
		switch(gamePhase) {
		case FIRSTTURN:
			gamePhase = GAME_PHASE.REINFORCEMENT; 
			currentTurn = this.players[0];
			turnCounter = 0;
			giveBonus(currentTurn);
			if(currentTurn.isAI()) {
				currentTurn.playTurn();
			}
			break;
		case REINFORCEMENT:
			gamePhase = GAME_PHASE.BATTLE;
			
			break;
		case BATTLE:
			if(conquerMade) {
				giveCard();
			}
			gamePhase = GAME_PHASE.FINALMOVE;
			
			break;
		case FINALMOVE:
			conquerMade = false;
			giveBonus(currentTurn);
			gamePhase = GAME_PHASE.REINFORCEMENT;
			
			break;
		}
	}
	
	/**
	 * Returns the total amount of bonus tanks
	 * @return s
	 */
	public int getBonusTanksSum() {
		int s = 0;
		for(Player p : players) {
			s += p.getBonusTanks();
		}
		return s;
	}
	
	/**
	 * Determines if the first phase ended
	 * @return boolean
	 */
	public boolean firstPhaseEnded() {
		
		if(getBonusTanksSum() == 0) {
			for(Player p : players) {
				if(p.getBonusTanks() != 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Moves tanks from a territory to another
	 * @param t1 is the first territory
	 * @param t2 is the second territory
	 * @param n is the number of tanks
	 */
	public void moveTanks(Territory t1, Territory t2, int n) {
		t1.removeTanks(n);
		t2.addTanks(n);
	}
	
	/**
	 * Makes a battle between 2 players
	 * @param atkResults are the dice of the attacker
	 * @param defResults are the dice of the defender
	 * @param atk is the number of attacking dice
	 * @param def is the number of defending dice
	 */
	public void battle(int[] atkResults, int[] defResults, int atk, int def) {
		
		int n = Math.min(atk, def);
		
		for(int i=0; i < n; i++) {
			if(atkResults[i] > defResults[i]) {
				getTerritory(GameSceneController.territory2).removeTanks(1);
				getPlayer(GameSceneController.territory2.getOwner()).removeTanks(1);
			} else {
				getTerritory(GameSceneController.territory1).removeTanks(1);
				currentTurn.removeTanks(1);
			}
		}	
	}
	
	/**
	 * A player conquers a territory
	 * @param t1 is the attacking territory
	 * @param t2 is the defending territory
	 */
	public void conquer(Territory t1, Territory t2) {
		boolean t2ContConquered = false;
		if(isOwned(getTerrContinent(t2))) {
			t2ContConquered = true;
		}
		getPlayer(t1.getOwner()).addTerritory();
		getPlayer(t2.getOwner()).removeTerritory();
		getTerritory(t2).setOwner(getTerritory(t1).getOwner());
		conquerMade = true;
		if(t2ContConquered) {
			getPlayer(t2.getOwner()).removeContinent();
		}
		checkOwn(getTerrContinent(t2));

	}
	
	/**
	 * Checks if a continent is owned
	 * @param c is the continent
	 */
	private void checkOwn(Continent c) {
		if(isOwned(c)) {
			getPlayer(getTerritory(c.getRandomTerritory()).getOwner()).addContinents();
		}
	}
	/**
	 * Adds territories to a continent
	 * @param c is the continent
	 */
	public void setContinent(Continent c) {
		for(Territory t: territories) {
			if (t.getContinent().contentEquals(c.getName())) {
				c.addTerritory(t);
			}
		}
	}

	/**
	 * Verifies if the continent is owned completely by a player
	 * @return owned
	 */
	public boolean isOwned(Continent c) {
		int i = 0;
		Player temp = currentTurn;
		for(Territory t : c.getTerritories()) {
			if(i == 0) {
				temp = t.getOwner();
			}
			if(!getTerritory(t).getOwner().equals(temp)) {
				return false;
			}
			i++;
		}
		return true;
	}
	
	/**
	 * Returns a random territory of a continent
	 * @return t
	 */
	public Territory getRandomTerritory(Continent c) {		
		for(Territory t: territories) {
			if(t.getContinent().equals(c.getName())) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Verifies if a mission is completed
	 * @return boolean
	 */
	public boolean verifyMission () {
		MISSION_TYPE missionType = currentTurn.getMission().getType();
		int i = 0;
		
		if(missionType == MISSION_TYPE.TYPE1) {
			if(currentTurn.getTerritories() >= currentTurn.getMission().getNumberOfTerritories()) {
				for(Territory t : territories) {
					if(t.getOwner().equals(currentTurn) && t.getTanks() >= currentTurn.getMission().getNumberOfTanks()) {
						i++;
					}
				}
				if(i == currentTurn.getMission().getNumberOfTerritories()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		else {
			if(missionType==MISSION_TYPE.TYPE2) {
				Continent c1 = currentTurn.getMission().getContinent1();
				Continent c2 = currentTurn.getMission().getContinent2();
				if(isOwned(c1)){
					Territory t1 = getTerritory(c1.getRandomTerritory());
					if(t1.getOwner().equals(currentTurn)) {
						if(isOwned(c2)){
							Territory t2 = getTerritory(c2.getRandomTerritory());
							if(t2.getOwner().equals(currentTurn)) {
								if(!currentTurn.getMission().hasContinent3()) {
									return true;
								}
								else
									if(currentTurn.getContinents()>2)
									return true;
							}
						}
					}
				}
			}
			else
				return false;
		}
		return false;
	}
	
	/**
	 * Returns the continent of a territory
	 * @param ti is the territory
	 * @return Continent
	 */
	public Continent getTerrContinent(Territory ti) {
		for(Continent co : continents) {
			if(co.getName().equals(ti.getContinent())){
				return co;
			}
		}
		return null;
	}
	
	/**
	 * plays the combination of 3 cards and gives bonus
	 * @param c1 is the first card
	 * @param c2 is the second card
	 * @param c3 is the third card
	 */
	public void playCardTris(Card c1, Card c2, Card c3) {
		currentTurn.giveBonusTanks(checkTris(c1, c2, c3));
		currentTurn.playCard(c1);
		currentTurn.playCard(c2);
		currentTurn.playCard(c3);
	}
	
	/**
	 * Checks if the combination of 3 cards is a valid one and gives bonus tanks if possible
	 * @param ca1 is the first card
	 * @param ca2 is the second card
	 * @param ca3 is the third card
	 * @return int, the number of bonus tanks
	 */
	public int checkTris(Card ca1, Card ca2, Card ca3) {
		
		ArrayList<FIGURE> figures = new ArrayList<FIGURE>();
		int bonus = 0;
		figures.add(ca1.getFigure());
		figures.add(ca2.getFigure());
		figures.add(ca3.getFigure());
		for (Territory t: territories) {
			if(!(ca1.getFigure().equals(FIGURE.JOLLY))) {
				if(ca1.getTerritory().equals(t)) {
					if (t.getOwner().equals(currentTurn)) 
						bonus = bonus + 2;
				}
			}
			if(!(ca2.getFigure().equals(FIGURE.JOLLY))) {
				if(ca2.getTerritory().equals(t)) {
					if (t.getOwner().equals(currentTurn)) 
						bonus = bonus + 2;
				}
			}
			if(!(ca3.getFigure().equals(FIGURE.JOLLY))) { 
				if(ca3.getTerritory().equals(t)) {
					if (t.getOwner().equals(currentTurn)) 
						bonus = bonus + 2;
				}
			}	
		}
		
		if(ca1.getFigure() == ca2.getFigure() && ca2.getFigure() == ca3.getFigure()) {
			if(ca1.getFigure() == FIGURE.CANNONE) {
				return 4+bonus;
			} else if (ca1.getFigure() == FIGURE.CAVALIERE) {
				return 8+bonus;
			} else if (ca1.getFigure() == FIGURE.FANTE) {
				return 6+bonus;
			}
		} else if (figures.contains(FIGURE.CANNONE) && figures.contains(FIGURE.FANTE) && figures.contains(FIGURE.CAVALIERE)) {
			return 10+bonus;
		} else if (figures.contains(FIGURE.JOLLY)) {
			return 12+bonus;
		}
		return 0;
	}
	
	/**
	 * Gives starter tanks for each player
	 */
	private void giveStarterTanks() {
		switch(this.players.length) {
		case 3:
			for(Player p : players) {
				p.giveBonusTanks(35);
			}
			break;
		case 4:
			for(Player p : players) {
				p.giveBonusTanks(30);
			}
			break;
		case 5:
			for(Player p : players) {
				p.giveBonusTanks(25);
			}
			break;
		case 6:
			for(Player p : players) {
				p.giveBonusTanks(20);
			}
			break;
		}
	}
	/**
	 * Gives missions for each player
	 */
	private void giveMissions() {
		Mission[] shuffledMissions = shuffleMissions();
		int i = 0;
		for(Player p : players) {
			p.giveMission(shuffledMissions[i]);
			i++;
		}
	}
	
	/**
	 * Shuffles the missions of the missions list
	 * @return array of Missions
	 */
    private Mission[] shuffleMissions() {
    	Mission[] shuffledMissions = new Mission[missions.size()];
    	int k = 0;
    	for(Mission m : missions) {
    		shuffledMissions[k] = m;
    		k++;
    	}
        for (int i = 0; i < missions.size(); i++) {
            int j = i + (int) ((missions.size() - i) * Math.random());
            Mission temp = shuffledMissions[i];
            shuffledMissions[i] = shuffledMissions[j];
            shuffledMissions[j] = temp;
        }
        return shuffledMissions;
    }
	
    /**
     * Shuffles the territories of the territory list
     * @return array of Territories
     */
    private Territory[] shuffleTerritories() {
    	Territory[] shuffledTerritories = new Territory[territories.size()];
    	int k = 0;
    	for(Territory t : territories) {
    		shuffledTerritories[k] = t;
    		k++;
    	}
        for (int i = 0; i < territories.size(); i++) {
            int j = i + (int) ((territories.size() - i) * Math.random());
            Territory temp = shuffledTerritories[i];
            shuffledTerritories[i] = shuffledTerritories[j];
            shuffledTerritories[j] = temp;
        }
        return shuffledTerritories;
    }
    
    /**
     * Shuffles the cards of the cards list
     */
    private void shuffleCards() {
    	Card[] shuffledCards = new Card[cards.size()];
    	int k = 0;
    	for(Card c : cards) {
    		shuffledCards[k] = c;
    		k++;
    	}
        for (int i = 0; i < cards.size(); i++) {
            int j = i + (int) ((cards.size() - i) * Math.random());
            Card temp = shuffledCards[i];
            shuffledCards[i] = shuffledCards[j];
            shuffledCards[j] = temp;
        }
        ArrayList<Card> temp = new ArrayList<Card>();
        for(Card c: shuffledCards) {
        	temp.add(c);
        }
        cards = temp;
    }
    
    /**
     * Sets the owner for each territory of the territoryList
     */
    private void initTerritoryOwners() {
        int playerID = 0;
        Territory[] shuffledTerritories = shuffleTerritories();
        for (int i = 0; i < territories.size(); i++) {
            for(Territory t : territories) {
            	if(t.getId() == shuffledTerritories[i].getId()) {
            		t.setOwner(players[playerID]);
            		players[playerID].addTerritory();
            		t.addTanks(1);
            		t.getOwner().placeTank(1);
            	}
            }
            playerID = (playerID + 1) % players.length;
        }
    }
	
    /**
     * Gives bonus tanks to a player depending on territories owned
     * @param pl is the player
     */
	public void giveBonus(Player pl) {

		pl.giveBonusTanks((int)Math.floor(pl.getTerritories()/3));	
		
		for(Continent c : continents) {
			if(isOwned(c)) {
				if(getRandomPlayer(c).equals(pl)) {
					pl.giveBonusTanks(c.getBonus());
				}
			}
		}
	}
	
	/**
	 * Returns a random player 
	 * @param c is the continent
	 * @return Player
	 */
	public Player getRandomPlayer(Continent c) {
		for (Territory t: territories) {
			if (t.getContinent().equals(c.getName())) {
				return t.getOwner();
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of all territories
	 * @return ArrayList of Territories
	 */
	public ArrayList<Territory> getTerritories(){
		return territories;
	}
	
	/**
	 * Shuffles the players of the playerList
	 * @return array of players
	 */
    private Player[] shufflePlayers() {
    	Player[] shuffledPlayers = new Player[players.length];
    	int k = 0;
    	for(Player p : players) {
    		shuffledPlayers[k] = p;
    		k++;
    	}
        for (int i = 0; i < players.length; i++) {
            int j = i + (int) ((players.length - i) * Math.random());
            Player temp = shuffledPlayers[i];
            shuffledPlayers[i] = shuffledPlayers[j];
            shuffledPlayers[j] = temp;
        }
        return shuffledPlayers;
    }
	
    /**
     * Returns the player of the current turn of the game
     * @return Player
     */
	public Player getCurrentTurn() {
		return currentTurn;
	}
	
	/**
	 * Returns the current phase of the game
	 * @return gamePhase
	 */
	public GAME_PHASE getGamePhase() {
		return gamePhase;
	}
	
	/**
	 * Sets the current phase of the game
	 * @param gamePhase is the current gamephase
	 */
	public void setGamePhase(GAME_PHASE gamePhase) {
		this.gamePhase = gamePhase;
	}
	
	/**
	 * Adds a tank to a territory
	 * @param t is the territory
	 */
	public void addTerritoryTanks(Territory t) {
		getTerritory(t).addTanks(1);
	}
	
	/**
	 * Returns a territory
	 * @param t is the territory
	 * @return Territory
	 */
	public Territory getTerritory(Territory t) {
		for(Territory te : territories) {
			if(te.getId() == t.getId()) {
				return te;
			}
		}
		return null;
	}
	
	/**
	 * Returns the player
	 * @param p is the player
	 * @return Player
	 */
	public Player getPlayer(Player p) {
		for(Player pl : players) {
			if(pl.getName().equals(p.getName())) {
				return pl;
			}
		}
		return null;
	}
	
	/**
	 * Gives a card to a Player then shuffles them
	 */
	public void giveCard() {
		getPlayer(currentTurn).giveCard(cards.get(0));
		cards.remove(0);
		shuffleCards();
	}
	
	/**
	 * Returns a random territory from owned territories of the current player
	 * @return Territory
	 */
	public Territory getRandomCurrentPlayerTerritory() {
		
		ArrayList<Territory> temp = new ArrayList<Territory>();
		
		for(Territory t : territories) {
			if(t.getOwner().equals(currentTurn)) {
				temp.add(t);
			}
		}
		Random rand = new Random();
		return temp.get(rand.nextInt(temp.size()));
	}
	
	public int getCurrTurnBonusTanks() {
		return currentTurn.getBonusTanks();
	}
	
	private void playerEliminated(Player p) {
		p.setEliminated(true);
	}

}