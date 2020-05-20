package model;

import java.util.ArrayList;

public class Continent {
	
	private String name;
	private int bonus;
	private ArrayList<Territory> territories;
	private boolean owned;
	
	public Continent(String name, int bonus) {
		this.name = name;
		this.bonus = bonus;
	}
	
	public void addTerritory(Territory territory) {
		territories.add(territory);
	}
	
	public boolean isOwned() {
		for (Territory t1 : territories) {
			for(Territory t2 : territories) {
				if(t2.getOwner() != t1.getOwner()) {
					return owned = false;
				}
			}
		}
		return owned = true;
		 
	}

	public int getBonus() {
		return bonus;
	}

	public String getName() {
		return name;
	}
	
	
	
}
