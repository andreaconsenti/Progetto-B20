package model.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Continent implements Serializable {
	
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
		territories = new ArrayList<Territory>();
	}
	
	/**
	 * Adds a territory to the list of territories included in a continent
	 * @param territory is the territory to add
	 */
	public void addTerritory(Territory territory) {		
		territories.add(territory);
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
	
	public void setOwned(boolean owned) {
		this.owned = owned;
	}
	
	/**
	 * Returns the code of the continent
	 * @return code
	 */
	public int getCode() {				
		return code;
	}
	
	/**
	 * Returns a territory
	 * @return territory
	 */
	public Territory getRandomTerritory() {
		return territories.get(2);
	}
	
	/**
	 * Returns the list of this continent's territories
	 * @return code
	 */
	public ArrayList<Territory> getTerritories(){
		return territories;
	}
}