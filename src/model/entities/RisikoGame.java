package model.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	private DiceShaker diceShaker = new DiceShaker();
	private FileHandler fileHandler = new FileHandler();
	private GAME_PHASE gamePhase;
	private Player currentTurn;
	private int turnCounter;
	
	
	public RisikoGame(Player[] players) throws NumberFormatException, IOException {
		this.players = players;
		this.players = shufflePlayers();
		
		giveStarterTanks();
		
		territories = fileHandler.addConfinanti(fileHandler.genTerritories("assets/infoTerritori.txt"), "assets/infoTerritori.txt");
		continents = fileHandler.genContinents("assets/continenti.txt");
		
		missions = fileHandler.genMissions("assets/obiettivi.txt", continents);
		giveMissions();
		
		cards = fileHandler.genCards(territories, "assets/infoTerritori.txt");
		
		initTerritoryOwners();
		
		setGamePhase(GAME_PHASE.FIRSTTURN);
		
		turnCounter = 0;
		currentTurn = this.players[turnCounter];
		
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
			gamePhase = GAME_PHASE.FINALMOVE;
			
			break;
		case FINALMOVE:
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
    
    private Card[] shuffleCards() {
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
        return shuffledCards;
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
