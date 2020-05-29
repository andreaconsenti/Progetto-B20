package model.entities;

public class Card {
	
	private FIGURE figure;
	private Territory territory;
	private int id;
	
	public Card(FIGURE figure, Territory territory, int id) {	//costruttore di una carta territorio
		this.figure = figure;
		this.territory = territory;
		this.id=id;
	}

	public FIGURE getFigure() {			//ritorna la figura della carta
		return figure;
	}

	public Territory getTerritory() {	//ritorna il territorio della carta
		return territory;
	}
	
	public int getId() {		//ritorna il numero dell'ID corrispondente a un territorio
		return id;
	}
	
	public void printCard() {	
		if(territory!=null)
			System.out.println("La carta di "+ this.territory.getName() +" ha come simbolo un: " +this.figure + "\n");
		else
			System.out.println("La tua carta è un Jolly\n");
	}
}