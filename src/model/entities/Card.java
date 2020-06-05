package model.entities;

public class Card {
	
	private FIGURE figure;
	private Territory territory;
	private int id;
	
	/**
	 * Creates a new territory card
	 * @param figure is the type of the card described by the enum FIGURE: JOLLY, FANTE, CANNONE o CAVALIERE
	 * @param territory is the territory associated to the card
	 * @param id number to identify the territory
	 */
	public Card(FIGURE figure, Territory territory, int id) {	
		this.figure = figure;
		this.territory = territory;
		this.id=id;
	}

	/**
	 * Returns the figure of the card
	 * @return figure
	 */
	public FIGURE getFigure() {			
		return figure;
	}

	/**
	 * Returns the territory of the card
	 * @return territory
	 */
	public Territory getTerritory() {	
		return territory;
	}
	
	/**
	 * Returns the id matching the territory
	 * @return id
	 */
	public int getId() {		
		return id;
	}
	
	/**
	 * Prints the features of the card
	 */
	public void printCard() {	
		if(territory!=null)
			System.out.println("La carta di "+ this.territory.getName() +" ha come simbolo un: " +this.figure + "\n");
		else
			System.out.println("La tua carta è un Jolly\n");
	}
}