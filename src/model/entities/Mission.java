package model.entities;

public class Mission {
	
	private int numberOfTerritories;
	private int numberOfTanks;
	private Continent con1;
	private Continent con2;
	private boolean con3;
	private String description;
	private MISSION_TYPE type;
	
	private enum MISSION_TYPE {
		TYPE1, TYPE2;
	};
	
	
	public Mission(int nty, int ntk, String description) {
		
		this.type = MISSION_TYPE.TYPE1;
		numberOfTerritories = nty;
		numberOfTanks = ntk;
		this.description = description;
		
	}
	
	public Mission(Continent cont1, Continent cont2, boolean cont3, String description) {
		
		this.type = MISSION_TYPE.TYPE2;
		con1 = cont1;
		con2 = cont2;
		con3 = cont3;
		this.description = description;
		
	}

	public int getNumberOfTerritories() {
		return numberOfTerritories;
	}

	public int getNumberOfTanks() {
		return numberOfTanks;
	}

	public Continent getContinent1() {
		return con1;
	}

	public Continent getContinent2() {
		return con2;
	}

	public boolean hasContinent3() {
		return con3;
	}

	public String getDescription() {
		return description;
	}

	public MISSION_TYPE getType() {
		return type;
	}
	
	
}
