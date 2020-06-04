package model.util;

import model.entities.DiceShaker;
import model.entities.Territory;

public class Battle {

	/**
	 * Uses the DiceShaker to roll the dice during a battle between two territories and returns true if the operation is legal
	 * @param attacker is the territory that is attacking
	 * @param defender is the territory that is defending
	 * @param atkDice is the number of dice rolled by the attacker
	 * @param defDice is the number of dice rolled by the defender
	 * @return boolean
	 */
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
