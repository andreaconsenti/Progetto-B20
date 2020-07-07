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
	
	
	public RisikoGame(Player[] players) throws NumberFormatException, IOException {
		this.players = players;
		this.players = shufflePlayers();
		
		giveStarterTanks();
		
		territories = fileHandler.addConfinanti(fileHandler.genTerritories("assets/infoTerritori.txt"), "assets/infoTerritori.txt");
		continents = fileHandler.genContinents("assets/continenti.txt");
		
		missions = fileHandler.genMissions("assets/obiettivi.txt", continents);
		giveMissions();
		
		cards = fileHandler.genCards(territories, "assets/infoTerritori.txt");
		shuffleCards();
		
		initTerritoryOwners();
		
		setGamePhase(GAME_PHASE.FIRSTTURN);
		
		turnCounter = 0;
		currentTurn = this.players[turnCounter];
		conquerMade = false;
		
	}
	
	public void nextTurn() {
		turnCounter++;
		if(turnCounter == players.length) {
			turnCounter = 0;
		}
		currentTurn = this.players[turnCounter];
	}
	
	public void nextPhase() {
		switch(gamePhase) {
		case FIRSTTURN:
			gamePhase = GAME_PHASE.REINFORCEMENT; 
			currentTurn = this.players[0];
			turnCounter = 0;
			giveBonus(currentTurn);
			
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
		t1.getOwner().addTerritory();
		t2.getOwner().removeTerritory();
		getTerritory(t2).setOwner(getTerritory(t1).getOwner());
		conquerMade = true;
	}
		
		
	public boolean verifyMission () {
		int codMission = getCurrentTurn().getMission().getCodeMission();
		MISSION_TYPE missionType = getCurrentTurn().getMission().getType();
		int i = 0;
		
		if (missionType == MISSION_TYPE.TYPE1) {
			
				for (Territory t : territories) {
					if (t.getOwner() == getCurrentTurn() && getCurrentTurn().getMission().getNumberOfTanks() <= t.getTanks() ) {
						i++;
					}
				}
				if (getCurrentTurn().getMission().getNumberOfTerritories() <= i) {
					return true;
				}
				else 
					return false;
		}
		
		else {
			if () {
				return true;
			}
			
		else
			return false; 
		}
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
		
//		for(Continent c : continents) {
//			if(c.isOwned()) {
//				if(c.getRandomPlayer().equals(pl)) {
//					pl.giveBonusTanks(c.getBonus());
//				}
//			}
//		}
		
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
	
	public void printCards() {
		for(Card c : cards) {
			c.printCard();
			
		}
	}
	
	public void printContinents() {
		for(Continent c : continents) {
			System.out.println(c.getName());
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
//		Player p1 = new Player("Luca", COLOR.BLACK);
//		Player p2 = new Player("Andre", COLOR.PINK);
//		Player p3 = new Player("Gino", COLOR.BLUE);
////		Player p4 = new Player("Daniele", COLOR.RED);
////		Player p5 = new Player("Alfonso", COLOR.GREEN);
//		
//		Player[] list = {p1, p2, p3};
//		
//		RisikoGame game = new RisikoGame(list);
//		
//		//game.printTerritories();
//		System.out.println(" ");
//		//game.printPlayers();
//		//game.printCards();
//		//game.printContinents();
//		
//		game.printBonusTanks();
//		
//		
//		
//		
//		
//	}
//	
	
	
}
