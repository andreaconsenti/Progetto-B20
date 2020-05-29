package model.entities;

public class Mission {
	
	private int numberOfTerritories;
	private int numberOfTanks;
//	private Continent con1;
//	private Continent con2;
	private String con1;
	private String con2;
	private boolean con3;
	private String description;
	private int codMission;
	private MISSION_TYPE type;
	
	private enum MISSION_TYPE {
		TYPE1, TYPE2;
	};
	
	
	public Mission(int nty, int ntk, int codMission) {
		
		this.type = MISSION_TYPE.TYPE1;
		numberOfTerritories = nty;
		numberOfTanks = ntk;
		this.codMission=codMission;
		description="Il tuo obiettivo è di conquistare " +nty+ " territori con almeno " +ntk+ " armate su ogni terriorio\n";
	}
	
	public Mission(String cont1, String cont2, boolean cont3, int codMission) {
		
		this.type = MISSION_TYPE.TYPE2;
		con1 = cont1;
		con2 = cont2;
		con3 = cont3;
		this.codMission=codMission;
		description="Il tuo obiettivo è di conquistare la totalità di " +getContinent1()+ ", di " +getContinent2()+ " e di un altro continente a tua scelta\n";
	}

	public int getNumberOfTerritories() {
		return numberOfTerritories;
	}

	public int getNumberOfTanks() {
		return numberOfTanks;
	}

	public /*Continent*/ String getContinent1() {
		return con1;
	}

	public /*Continent*/ String getContinent2() {
		return con2;
	}

	public boolean hasContinent3() {
		return con3;
	}

	public String getDescription() {
		return description;
	}

	public int getCodeMission() {
		return codMission;
	}
	
	public MISSION_TYPE getType() {
		return type;
	}

	public void printMission() {
		// TODO Auto-generated method stub
		System.out.println(description);
	}
	
	
}
