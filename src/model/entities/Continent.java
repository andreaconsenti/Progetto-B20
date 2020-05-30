package model.entities;

import java.util.ArrayList;

public class Continent {
	
	private int code;
	private String name;
	private int bonus;
	private ArrayList<Territory> territories;
	private boolean owned;
	
	public Continent(int code, String name, int bonus) {		//costruttore di continent (nome + bonus conquista)
		this.code=code;
		this.name=name;
		this.bonus = bonus;
	}
	
	public void addTerritory(Territory territory) {		//metodo che aggiunge un territorio alla lista di territori di un continente
		territories.add(territory);
	}
	
	public boolean isOwned() {							//ciclo che permette di verificare se un continente è posseduto o meno da un giocatore
		for (Territory t1 : territories) {
			for(Territory t2 : territories) {
				if(t2.getOwner() != t1.getOwner()) {
					return owned = false;
				}
			}
		}
		return owned = true;
		 
	}

	public int getBonus() {				//ritorna il numero dei carrarmati bonus in seguito alla conquista del continente 
		return bonus;
	}

	public String getName() {			//ritorna il nome del continente
		return name;
	}

	public boolean getOwned() {			//ritorna il possedimento o meno di un continente
		return owned;
	}
	
	public int getCode() {				//ritorna il codice del continente
		return code;
	}
	
	public Player getRandomPlayer() {		//metodo utilizzato per riconoscere il player occupante, nel caso in cui occupi tutto il continente
		return territories.get(1).getOwner();
	}
	
	
}
