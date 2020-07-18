package model.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import controller.AIRecapSceneController;
import controller.AttackSceneController;
import controller.GameSceneController;

public class Player {
	
	//private boolean isAI;
	
	private String name;
	private COLOR color;
	private int tanks;
	private int bonusTanks;
	private int continents;
	private int territories;
	private Mission mission;
	private DiceShaker shaker;
	private ArrayList<Card> cards;
	private boolean isAI;
	
	/**
	 * Creates a new player
	 * @param name is the name of the player
	 * @param color is the color chosen by the player
	 */
	public Player(String name, COLOR color, boolean ai) {
		this.name = name;
		this.color = color;
		this.tanks = 0;
		this.bonusTanks = 0;
		continents = 0;
		shaker = new DiceShaker();
		cards = new ArrayList<Card>();
		isAI = ai;
	}
	
	/**
	 * Gives a mission to the player
	 * @param mission is the mission given
	 */
	public void giveMission(Mission mission) {
		this.mission = mission;
	}
	
	public Mission getMission() {
		return mission;
	}
	
	/**
	 * Gives tanks to the player
	 * @param newTanks is the number of tanks added
	 */
	public void addTanks (int newTanks) {
		tanks += newTanks;
	}
	
	/**
	 * Removes tanks from the player
	 * @param lostTanks is the number of tanks lost
	 */
	public void removeTanks (int lostTanks) {
		tanks -= lostTanks;
	}
	
	/**
	 * Modifies the number of additional tanks the player receives
	 * @param n is the number of tanks added or subtracted
	 */
	public void giveBonusTanks(int n) {
		bonusTanks += n;
	}
	
	/**
	 * Returns the number of bonus tanks the player gets each turn
	 * @return bonusTanks
	 */
	public int getBonusTanks() {
		return bonusTanks;
	}
	
	/**
	 * Returns the name of the player
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the color chosen by the player
	 * @return color
	 */
	public COLOR getColor() {
		return color;
	}

	/**
	 * Returns the number of tanks that the player has
	 * @return tanks
	 */
	public int getTanks() {
		return tanks;
	}

	/**
	 * Returns the number of continents owned by the player
	 * @return continents
	 */
	public int getContinents() {
		return continents;
	}
	
	public void addContinents() {
		this.continents += 1;
	}
	
	public void removeContinent() {
		this.continents -= 1;
	}
	
	public void zeroContinents() {
		this.continents = 0;
	}

	/**
	 * Returns the number of territories owned by the player
	 * @return territories
	 */
	public int getTerritories() {
		return territories;
	}

	/**
	 * Adds a territory to the number of territories owned by the player
	 */
	public void addTerritory() {
		this.territories += 1;
	}
	
	public void removeTerritory() {
		this.territories -= 1;
	}

	/**
	 * Returns the description of the mission of the player
	 * @return mission.toString()
	 */
	public String getMissionDescription() {
		return mission.toString();
	}
	
	/**
	 * Returns the name of the color chosen by the player
	 * @return Color
	 */
	public String getColorName() {
		switch(this.color) {
		case GREEN:
			return "Verde";
		case YELLOW:
			return "Giallo";
		case RED:
			return "Rosso";
		case PINK:
			return "Rosa";
		case BLUE:
			return "Blu";
		case BLACK:
			return "Nero";
		}
		return null;	
	}
	
	public void placeTank(int n) {
		
		this.bonusTanks -= n;
		this.tanks +=n;
		
	}
	
	public void giveCard(Card c) {
		cards.add(c);
	}
	
	public void playCard(Card c) {
		cards.remove(c);
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public boolean equals(Player p) {
		if(p.getName() == this.name)
			return true;
		else
			return false;
	}
	
	public int[] rollDices(int n) {
		return shaker.rollDices(n);
	}
	
	public boolean isAI() {
		return isAI;
	}
	
	
	
	
	//_______________AI METHODS______________
	
	
	public void playTurn(){
		
		switch(GameSceneController.getInstance().getGame().getGamePhase()) {
		case FIRSTTURN:
			Territory temp = GameSceneController.getInstance().getGame().getRandomCurrentPlayerTerritory();
			if(this.bonusTanks > 0) {
				GameSceneController.getInstance().setSelTerritory(temp);
				AIRecapSceneController.getInstance().setText(this.name + ": armata posizionata sul territorio " + temp.getName() + "\n");
				GameSceneController.getInstance().firstTurn();
			}
			break;
		case REINFORCEMENT:
			while(this.bonusTanks > 0) {
				temp = GameSceneController.getInstance().getGame().getRandomCurrentPlayerTerritory();
				if(this.bonusTanks > 0) {
					GameSceneController.getInstance().setSelTerritory(temp);
					try {
						AIRecapSceneController.getInstance().setText(this.name + ": armata posizionata sul territorio " + temp.getName() + "\n");
						GameSceneController.getInstance().reinforcementClick();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
			GameSceneController.getInstance().nextPhase();
			
			for(Territory t : GameSceneController.getInstance().getGame().getTerritories()) {
				if(t.getTanks() > 3 && t.getOwner().equals(this)) {
					GameSceneController.getInstance().setTerritory12(t, null);
					break;
				}
			}
			
			if(GameSceneController.territory1 != null) {
				for(Territory t : GameSceneController.territory1.getConfinanti()) {
					if(!t.getOwner().equals(this) && t.getTanks() < 3) {
						GameSceneController.getInstance().setTerritory2(t);
						AttackSceneController.aiAttack();
						if(t.getTanks() == 0) {
							GameSceneController.getInstance().getGame().conquer(GameSceneController.territory1, t);
							GameSceneController.getInstance().getGame().moveTanks(GameSceneController.territory1, GameSceneController.territory2, 1);
						}
						AIRecapSceneController.getInstance().setText(this.name + ": ha attaccato " + GameSceneController.territory2.getName() + "con " + GameSceneController.territory1.getName());
						GameSceneController.getInstance().updateTanks();
						GameSceneController.getInstance().setTerritory12(null, null);
						break;
					}
				}
			}
			
			GameSceneController.getInstance().nextTurn();
			
			break;
		}
		
		
	}
	
	
}
