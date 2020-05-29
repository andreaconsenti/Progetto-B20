package model.entities;

public class Card {
	
	private FIGURE figure;
	private Territory territory;
	private int id;
	
	public Card(FIGURE figure, Territory territory, int id) {
		this.figure = figure;
		this.territory = territory;
		this.id=id;
	}

	public FIGURE getFigure() {
		return figure;
	}

	public Territory getTerritory() {
		return territory;
	}
	
	public int getId() {
		return id;
	}
	
	public void printCard() {
		if(territory!=null)
			System.out.println("La carta di "+ this.territory.getName() +" ha come simbolo un: " +this.figure + "\n");
		else
			System.out.println("La tua carta è un Jolly\n");
	}
}