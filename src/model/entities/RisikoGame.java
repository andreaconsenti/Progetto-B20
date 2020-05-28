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
	private ArrayList<Continent> continents = new ArrayList<Continent>();
	private ArrayList<Mission> missions;
	private ArrayList<Card> cards;
	private DiceShaker diceShaker = new DiceShaker();
	private FileHandler fileHandler = new FileHandler();
	
	
	
	public RisikoGame(Player[] players) throws NumberFormatException, IOException {
		this.players = players;
		giveStarterTanks();
		//giveMissions();
		
		territories = fileHandler.addConfinanti(fileHandler.genTerritories("assets/TerritoriEConfini.txt"), "assets/confini.txt");
		genContinents();
		
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
		for(Player p : players) {
			int item = new Random().nextInt(this.missions.size());
			int i = 0;
			for(Mission m : missions) {
				if(i == item) {
					p.giveMission(m);
					missions.remove(m);
					i++;
				}
			}
		}
	}
	
	private void genContinents() {
		continents.add(new Continent("NordAmerica", 5));
		continents.add(new Continent("SudAmerica", 2));
		continents.add(new Continent("Europa", 5));
		continents.add(new Continent("Africa", 3));
		continents.add(new Continent("Asia", 7));
		continents.add(new Continent("Australia", 2));
		
		/*for(Continent c : continents) {
			
			for(Territory t : territories) {
				
				if(t.getContinent().equals(c.getName())) {
					c.addTerritory(t);
				}
				
			}
			
		}*/
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
	
	
	public void printTerritories() {
		for(Territory t : territories) {
			System.out.println(t.toString());
			System.out.println(t.getOwner().getName());
		}
	}
	
	public void printPlayers() {
		for(Player p : players) {
			System.out.println(p.getName());
			System.out.println(p.getTerritories());
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
	
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		Player p1 = new Player("Luca", COLOR.BLACK);
		Player p2 = new Player("Andre", COLOR.PINK);
		Player p3 = new Player("Gino", COLOR.BLUE);
		Player p4 = new Player("Daniele", COLOR.RED);
		
		Player[] list = {p1, p2, p3, p4};
		
		RisikoGame game = new RisikoGame(list);
		
		game.printTerritories();
		System.out.println(" ");
		game.printPlayers();
		
		
		
		
	}
	
	
	
}
