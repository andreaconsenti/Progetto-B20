package model.entities;

import java.awt.Color;
import java.util.ArrayList;

public class Territory {
	
	private String name;
	private Player owner;
	private int tanks;
	private ArrayList<Territory> confinanti;
	//private Continent continent;
	private String continent;
	private int id;
	private String hexaColor;
	
	/*public Territory(String name, Continent continent, int id) {
		this.name = name;
		this.continent = continent;
		tanks = 0;
		this.id = id;
	}*/
	
	/**
	 * Creates a new territory
	 * @param name is the name of the territory
	 * @param id is the numerical code used to identify the territory
	 * @param continent is the continent where the territory is located
	 * @param hexaColor is the color of the territory on the map
	 */
	public Territory(String name, int id, String continent, String hexaColor) {
		this.name = name;
		tanks = 0;
		this.id = id;
		this.hexaColor = hexaColor;
		this.continent = continent;
	}
	
	/**
	 * Sets the borders of a territory through an array list
	 * @param confinanti is the list of territories in the borders
	 */
	public void setConfinanti(ArrayList<Territory> confinanti) {
		this.confinanti = confinanti;
	}
	
	/**
	 * Verifies if two territories are directly near each other
	 * @param t is the second territory
	 * @return boolean
	 */
	public boolean isConfinante(Territory t) {
		
		for(Territory t1 : confinanti) {
			if(t1.getId() == t.getId())
				return true;
		}
		return false;
	}
	
	/**
	 * Adds tanks to the territory
	 * @param newTanks is the number of tanks added
	 */
	public void addTanks(int newTanks) {
		tanks = getTanks() + newTanks;
	}
	
	/**
	 * Removes tanks from the territory
	 * @param lostTanks is the number of tanks to remove
	 */
	public void removeTanks(int lostTanks) {
		tanks = getTanks() - lostTanks;
	}
	
	/**
	 * Changes the owner of the territory
	 * @param owner is the new owner
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/*public Continent getContinent() {
		return continent;
	}*/

	/**
	 * Returns an array with all the territories on the border
	 * @return confinanti
	 */
	public ArrayList<Territory> getConfinanti() {
		return confinanti;
	}

	/**
	 * Returns the number of tanks on the territory
	 * @return tanks
	 */
	public int getTanks() {
		return tanks;
	}

	/**
	 * Returns the owner of the territory
	 * @return owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Returns the name of the territory
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the ID of the territory
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return name+"\n";
	}
	
	/**
	 * Prints the borders of the territory
	 */
	public void printConfini() {
		System.out.println("Confini di " + this.name + ": ");
		
		for(Territory t : confinanti) {
			System.out.println(t.toString());	
		}
	}

	/**
	 * Returns the continent of the territory
	 * @return continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * Returns the color of the territory on the map
	 * @return hexaColor
	 */
	public String getHexaColor() {
		return hexaColor;
	}
	
	/**
	 * Converts the color from hexadecimal to RGB
	 * @return color
	 */
	public Color getRGB () {
	    return new Color(
	            Integer.valueOf(this.hexaColor.substring( 0, 2 ), 16 ),
	            Integer.valueOf(this.hexaColor.substring( 2, 4 ), 16 ),
	            Integer.valueOf(this.hexaColor.substring( 4, 6 ), 16 ));
	}
	
}
