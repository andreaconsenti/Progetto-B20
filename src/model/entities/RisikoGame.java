package model.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.util.FileHandler;

public class RisikoGame {
	
	private enum GAME_PHASE{
		REINFORCEMENT, BATTLE, FINALMOVE;
	}
	
	private Player[] players;
	private ArrayList<Territory> territories;
	private ArrayList<Continent> continents;
	private ArrayList<Mission> missions;
	private ArrayList<Card> cards;
	private DiceShaker diceShaker = new DiceShaker();
	private FileHandler fileHandler = new FileHandler();
	
	
	
	public RisikoGame(Player[] players) throws NumberFormatException, IOException {
		this.players = players;
		giveStarterTanks();
		
		
		territories = fileHandler.addConfinanti(fileHandler.genTerritories("assets/TerritoriEColori.txt"), "assets/confini.txt");
		continents = fileHandler.genContinents("assets/continenti.txt");
		
		missions = fileHandler.genMissions("assets/obiettivi.txt", continents);
		giveMissions();
		
		cards = fileHandler.genCards(territories, "assets/carte.txt");
		
		initTerritoryOwners();
		
		
	}
	
	
	
	private void giveStarterTanks() {
		switch(this.players.length) {
		case 3:
			for(Player p : players) {
				p.addTanks(35);
			}
		case 4:
			for(Player p : players) {
				p.addTanks(30);
			}
		case 5:
			for(Player p : players) {
				p.addTanks(25);
			}
		case 6:
			for(Player p : players) {
				p.addTanks(20);
			}
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
    
    private void initTerritoryOwners() {
        int playerID = 0;
        Territory[] shuffledTerritories = shuffleTerritories();
        for (int i = 0; i < territories.size(); i++) {
            for(Territory t : territories) {
            	if(t.getId() == shuffledTerritories[i].getId()) {
            		t.setOwner(players[playerID]);
            		players[playerID].addTerritory();
            	}
            }
            playerID = (playerID + 1) % players.length;
        }
    }

	
	public void giveBonus() {

		for(Player p : players) {
			p.giveBonusTanks((int)Math.floor(p.getTerritories()/3));	
		}
		
		for(Continent c : continents) {
			if(c.isOwned()) {
				Player p = c.getRandomPlayer();
				p.giveBonusTanks(c.getBonus());
			}
		}
		
	}
	
	public ArrayList<Territory> getTerritories(){
		return territories;
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
	
	
	
	/*public static void main(String[] args) throws NumberFormatException, IOException {
		
		Player p1 = new Player("Luca", COLOR.BLACK);
		Player p2 = new Player("Andre", COLOR.PINK);
		Player p3 = new Player("Gino", COLOR.BLUE);
		Player p4 = new Player("Daniele", COLOR.RED);
		Player p5 = new Player("Alfonso", COLOR.GREEN);
		
		Player[] list = {p1, p2, p3, p4, p5};
		
		RisikoGame game = new RisikoGame(list);
		
		//game.printTerritories();
		System.out.println(" ");
		//game.printPlayers();
		//game.printCards();
		//game.printContinents();
		
		
		
		
	}*/
	
	
	
}
