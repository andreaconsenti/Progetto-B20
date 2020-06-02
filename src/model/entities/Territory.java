package model.entities;

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
	
	public Territory(String name, int id, String continent, String hexaColor) {
		this.name = name;
		tanks = 0;
		this.id = id;
		this.hexaColor = hexaColor;
		this.continent = continent;
	}
	
	public void setConfinanti(ArrayList<Territory> confinanti) {
		this.confinanti = confinanti;
	}
	
	public boolean isConfinante(Territory t) {
		
		for(Territory t1 : confinanti) {
			if(t1.getId() == t.getId())
				return true;
		}
		return false;
	}
	
	public void addTanks(int newTanks) {
		tanks = getTanks() + newTanks;
	}
	
	public void removeTanks(int lostTanks) {
		tanks = getTanks() - lostTanks;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/*public Continent getContinent() {
		return continent;
	}*/

	public ArrayList<Territory> getConfinanti() {
		return confinanti;
	}

	public int getTanks() {
		return tanks;
	}

	public Player getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return name+"\n";
	}
	
	public void printConfini() {
		System.out.println("Confini di " + this.name + ": ");
		
		for(Territory t : confinanti) {
			System.out.println(t.toString());	
		}
	}

	public String getContinent() {
		return continent;
	}

	public String getHexaColor() {
		return hexaColor;
	}
	
}
