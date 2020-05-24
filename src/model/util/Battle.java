package model.util;

import model.entities.DiceShaker;
import model.entities.Territory;

public class Battle {

	public static boolean attack(Territory attacker, Territory defender, int atkDice, int defDice) {
		
		DiceShaker shaker = new DiceShaker();
		int[] loss = new int[2];
		
		if(!attacker.isConfinante(defender)) {
			return false;
		}
		
		loss = shaker.rollDices(atkDice, defDice);
		
		attacker.removeTanks(loss[0]);
		defender.removeTanks(loss[1]);
		
		return true;
	}
	
	
	
}
