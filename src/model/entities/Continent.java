package model.entities;

import java.util.ArrayList;

public class Continent {
	
	private int code;
	private String name;
	private int bonus;
	private ArrayList<Territory> territories;
	private boolean owned;
	
	/**
	 * Creates a new continent
	 * @param code is a number that identifies the continent
	 * @param name is the name of the continent
	 * @param bonus is the number of additional tanks granted by owning the continent
	 */
	public Continent(int code, String name, int bonus) {		
		this.code=code;
		this.name=name;
		this.bonus = bonus;
	}
	
	/**
	 * Adds a territory to the list of territories included in a continent
	 * @param territory is the territory to add
	 */
	public void addTerritory(Territory territory) {		
		territories.add(territory);
	}
	
	/**
	 * Verifies if the continent is owned completely by a player
	 * @return owned
	 */
	public boolean isOwned() {							
		for (Territory t1 : territories) {
			for(Territory t2 : territories) {
				if(t2.getOwner() != t1.getOwner()) {
					return owned = false;
				}
			}
		}
		return owned = true;
		 
	}

	/**
	 * Returns the number of additional tanks granted by the continent
	 * @return bonus
	 */
	public int getBonus() {				 
		return bonus;
	}

	/**
	 * Returns the name of the continent
	 * @return name
	 */
	public String getName() {			
		return name;
	}

	/**
	 * Tells if a continent is owned by a player or not
	 * @return owned
	 */
	public boolean getOwned() {			
		return owned;
	}
	
	/**
	 * Returns the code of the continent
	 * @return code
	 */
	public int getCode() {				
		return code;
	}
	
	/**
	 * Returns the owner of the continent if the continent is totally occupied by one player
	 * @return player
	 */
	public Player getRandomPlayer() {		
		return territories.get(1).getOwner();
	}
	
	
}
