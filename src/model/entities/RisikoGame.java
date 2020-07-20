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
	
	public void nextTurn(){
//		if(gamePhase != GAME_PHASE.FIRSTTURN) {
//			gamePhase = GAME_PHASE.REINFORCEMENT;
//		}
		turnCounter++;
		if(turnCounter == players.length) {
			turnCounter = 0;
		}
		currentTurn = this.players[turnCounter];
		if(currentTurn.isAI() && gamePhase == GAME_PHASE.FIRSTTURN) {
			currentTurn.playTurn();
		}
	}
	
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
		
	public int getBonusTanksSum() {
		int s = 0;
		for(Player p : players) {
			s += p.getBonusTanks();
		}
		return s;
	}
	
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
	
	public void moveTanks(Territory t1, Territory t2, int n) {
		t1.removeTanks(n);
		t2.addTanks(n);
	}
	
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
	
	private void checkOwn(Continent c) {
		if(isOwned(c)) {
			getPlayer(getTerritory(c.getRandomTerritory()).getOwner()).addContinents();
		}
	}
		
	public void setContinent(Continent c) {
		for(Territory t: territories) {
			if (t.getContinent().contentEquals(c.getName())) {
				c.addTerritory(t);
			}
		}
	}
	
//	public void checkContinent() {
//		currentTurn.zeroContinents();
//		for (Continent c: continents) {
//			if(isOwned(c)) {
//				if (getTerritory(getRandomTerritory(c)).getOwner().equals(currentTurn))
//					currentTurn.addContinents();
//			}
//		}
//	}
	
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
				if(i == currentTurn.getTerritories()) {
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
	
	
	public Continent getTerrContinent(Territory ti) {
		for(Continent co : continents) {
			if(co.getName().equals(ti.getContinent())){
				return co;
			}
		}
		return null;
	}
	
	
	public void playCardTris(Card c1, Card c2, Card c3) {
		currentTurn.giveBonusTanks(checkTris(c1, c2, c3));
		currentTurn.playCard(c1);
		currentTurn.playCard(c2);
		currentTurn.playCard(c3);
	}
	
	public int checkTris(Card ca1, Card ca2, Card ca3) {
		
		ArrayList<FIGURE> figures = new ArrayList<FIGURE>();
		figures.add(ca1.getFigure());
		figures.add(ca2.getFigure());
		figures.add(ca3.getFigure());
		
		if(ca1.getFigure() == ca2.getFigure() && ca2.getFigure() == ca3.getFigure()) {
			if(ca1.getFigure() == FIGURE.CANNONE) {
				return 3;
			} else if (ca1.getFigure() == FIGURE.CAVALIERE) {
				return 8;
			} else if (ca1.getFigure() == FIGURE.FANTE) {
				return 6;
			}
		} else if (figures.contains(FIGURE.CANNONE) && figures.contains(FIGURE.FANTE) && figures.contains(FIGURE.CAVALIERE)) {
			return 10;
		} else if (figures.contains(FIGURE.JOLLY)) {
			return 12;
		}
		return 0;
		
	}
	
	private void giveStarterTanks() {
		switch(this.players.length) {
		case 3:
			for(Player p : players) {
				p.giveBonusTanks(16);
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
	
	private void giveMissions() {
		Mission[] shuffledMissions = shuffleMissions();
		int i = 0;
		for(Player p : players) {
			p.giveMission(shuffledMissions[i]);
			i++;
		}
	}
	
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
	
	public Player getRandomPlayer(Continent c) {
		for (Territory t: territories) {
			if (t.getContinent().equals(c.getName())) {
				return t.getOwner();
			}
		}
		return null;
	}
	
	public ArrayList<Territory> getTerritories(){
		return territories;
	}
	
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
	
	public Player getCurrentTurn() {
		return currentTurn;
	}

	public GAME_PHASE getGamePhase() {
		return gamePhase;
	}

	public void setGamePhase(GAME_PHASE gamePhase) {
		this.gamePhase = gamePhase;
	}
	
	public void addTerritoryTanks(Territory t) {
		for(Territory te : territories) {
			if(te.getId() == t.getId()) {
				te.addTanks(1);
			}
		}
		
	}
	
	public Territory getTerritory(Territory t) {
		for(Territory te : territories) {
			if(te.getId() == t.getId()) {
				return te;
			}
		}
		return null;
	}
	
	public Player getPlayer(Player p) {
		for(Player pl : players) {
			if(pl.getName().equals(p.getName())) {
				return pl;
			}
		}
		return null;
	}
	
	public void giveCard() {
		getPlayer(currentTurn).giveCard(cards.get(0));
		cards.remove(0);
		shuffleCards();
	}
	
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
	

	//-------------------TEST MAIN
	
	public void printTerritories() {
		for(Territory t : territories) {
			System.out.println(t.toString());
			System.out.println(t.getOwner().getName());
		}
	}
	
	public void printPlayers() {
		for(Player p : players) {
			System.out.println(p.getName());
			System.out.println(p.getMissionDescription());
		}
	}
	
	
	public void printContinents() {
		int i = 0;
		for(Continent c : continents) {
			System.out.println(c.getName());
			i = 0;
			for(Territory t : territories) {
				if(t.getContinent().equals(c.getName())) {
					i++;
					System.out.println(i);
				}
			}
		}
	}


	public void printBonusTanks() {
		for(Player p : this.players) {
			System.out.println(p.getName());
			System.out.println(p.getBonusTanks());
		}
	}




	
	
//	public static void main(String[] args) throws NumberFormatException, IOException {
//		
//		Player p1 = new Player("Luca", COLOR.BLACK, false);
//		Player p2 = new Player("Andre", COLOR.PINK, false);
//		Player p3 = new Player("Gino", COLOR.BLUE, false);
////		Player p4 = new Player("Daniele", COLOR.RED);
////		Player p5 = new Player("Alfonso", COLOR.GREEN);
//		
//		Player[] list = {p1, p2, p3};
//		
//		RisikoGame game = new RisikoGame(list, "assets/RisikoClassic/territori.txt", "assets/RisikoClassic/continenti.txt", "assets/RisikoClassic/obiettivi.txt");
////		game.printTerritories();
//		System.out.println(" ");
//		//game.printPlayers();
//		//game.printCards();
//		game.printContinents();
//		
////		game.printBonusTanks();
//		
//		
//		
//		
//		
//	}
//	
	
	
}
