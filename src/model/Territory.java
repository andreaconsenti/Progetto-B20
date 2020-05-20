package model;

import java.util.ArrayList;

public class Territory {
	
	private String name;
	private Player owner;
	private int tanks;
	private ArrayList<Territory> confinanti;
	private Continent continent;
	private int id;
	
	public Territory(String name, Continent continent, int id) {
		this.name = name;
		this.continent = continent;
		tanks = 0;
		this.id = id;
	}
	
	public void addConfinanti(Territory confinante) {
		confinanti.add(confinante);
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

	public Continent getContinent() {
		return continent;
	}

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
	
}
