package model.entities;

public class Card {
	
	private FIGURE figure;
	private Territory territory;
	
	public Card(FIGURE figure, Territory territory) {
		this.figure = figure;
		this.territory = territory;
	}

	public FIGURE getFigure() {
		return figure;
	}

	public Territory getTerritory() {
		return territory;
	}
	
	
}
