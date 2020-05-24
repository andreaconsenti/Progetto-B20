package model;

public class Player {
	
	//private boolean isAI;
	
	private String name;
	private COLOR color;
	private int tanks;
	private int continents;
	
	
	public Player(String name, COLOR color) {
		this.name = name;
		this.color = color;
		continents = 0;
	}
	
	
	public void addTanks (int newTanks) {
		tanks += newTanks;
	}
	
	public void removeTanks (int lostTanks) {
		tanks -= lostTanks;
	}
	
	
	public String getName() {
		return name;
	}
	
	public COLOR getColor() {
		return color;
	}

	public int getTanks() {
		return tanks;
	}

	public int getContinents() {
		return continents;
	}
	
	
	
}
