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
	 * Compares a card with this and returns true if they are the same
	 * @param c card to compare with this
	 */
	public boolean equals(Card c) {
		if(c.getTerritory().equals(this.territory)) {
			return true;
		} else
			return false;
	}
	
	
}