package model.entities;

public class Mission {
	
	private int numberOfTerritories;
	private int numberOfTanks;
	private Continent con1;
	private Continent con2;
	private boolean con3;
	private String description;
	private int codMission;
	private MISSION_TYPE type;
	
	/**
	 * Types that describe the possible objectives of a mission
	 */
	public enum MISSION_TYPE {
		TYPE1, TYPE2;
	};
	
	/**
	 * Creates a new type1 mission
	 * @param nty is the number of territories to conquer
	 * @param ntk is the number of tanks to have on each territory
	 * @param codMission is the id of the mission
	 */
	public Mission(int nty, int ntk, int codMission) {
		
		this.type = MISSION_TYPE.TYPE1;
		numberOfTerritories = nty;
		numberOfTanks = ntk;
		this.codMission=codMission;
		description="Il tuo obiettivo è di conquistare " +nty+ " territori con almeno " +ntk+ " armate su ogni terriorio\n";
	}
	
	/**
	 * Creates a new type2 mission
	 * @param cont1 is the first continent to conquer
	 * @param cont2 is the second continent to conquer
	 * @param cont3 is the third continent to conquer
	 * @param codMission is the id of the mission
	 */
	public Mission(Continent cont1, Continent cont2, boolean cont3, int codMission) {
		
		this.type = MISSION_TYPE.TYPE2;
		con1 = cont1;
		con2 = cont2;
		con3 = cont3;
		this.codMission=codMission;
		description="Il tuo obiettivo è di conquistare la totalità di " +getContinent1().getName()+ ", " +getContinent2().getName()+ " e di un altro continente a tua scelta\n";
	}

	/**
	 * returns the number of territories required by the mission
	 * @return numberOfTerritories
	 */
	public int getNumberOfTerritories() {
		return numberOfTerritories;
	}

	/**
	 * returns the number of tanks required by the mission
	 * @return numberOfTanks
	 */
	public int getNumberOfTanks() {
		return numberOfTanks;
	}

	/**
	 * Returns the first continent required by the mission
	 * @return con1
	 */
	public Continent getContinent1() {
		return con1;
	}

	/**
	 * Returns the second continent required by the mission
	 * @return con2
	 */
	public Continent getContinent2() {
		return con2;
	}

	/**
	 * Returns the third continent required by the mission
	 * @return con3
	 */
	public boolean hasContinent3() {
		return con3;
	}

	/**
	 * Returns the description of the mission
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the code of the mission
	 * @return codMission
	 */
	public int getCodeMission() {
		return codMission;
	}
	
	/**
	 * Returns the type of the mission
	 * @return type
	 */
	public MISSION_TYPE getType() {
		return type;
	}

	/**
	 * Prints the description of the mission
	 */
	public void printMission() {
		// TODO Auto-generated method stub
		System.out.println(description);
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
