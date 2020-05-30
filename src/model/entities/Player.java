package model.entities;

public class Player {
	
	//private boolean isAI;
	
	private String name;
	private COLOR color;
	private int tanks;
	private int bonusTanks;
	private int continents;
	private int territories;
	private Mission mission;
	
	
	public Player(String name, COLOR color) {
		this.name = name;
		this.color = color;
		this.tanks = 0;
		continents = 0;
	}
	
	public void giveMission(Mission mission) {
		this.mission = mission;
	}
	
	public void addTanks (int newTanks) {
		tanks += newTanks;
	}
	
	public void removeTanks (int lostTanks) {
		tanks -= lostTanks;
	}
	
	public void giveBonusTanks(int n) {
		bonusTanks += n;
	}
	
	public int getBonusTanks() {
		return bonusTanks;
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

	public int getTerritories() {
		return territories;
	}

	public void addTerritory() {
		this.territories += 1;
	}

	public String getMissionDescription() {
		return mission.toString();
	}
	
	
	
}
