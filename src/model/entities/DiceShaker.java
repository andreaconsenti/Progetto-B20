package model.entities;

import java.util.Arrays;


public class DiceShaker {
	
	private Die die1;
	private Die die2;
	private Die die3;
	private Die die4;
	private Die die5;
	private Die die6;
	
	private int[] atkResults;			
	private int[] defResults;
	private int[] loss;

	/**
	 * Creates a DiceShaker with 6 dice, used for battles, that calculates the loss for attacker and defender
	 */
	public DiceShaker() {		
		die1 = new Die();
		die2 = new Die();
		die3 = new Die();
		die4 = new Die();
		die5 = new Die();
		die6 = new Die();
		
		loss = new int[2];			
		atkResults = new int[3];	
		defResults = new int[3];	

	}
	
	/**
	 * Rolls the dice used
	 * @param atk is the number of dice used by the attacker
	 * @param def is the number of dice used by the defender
	 * @return loss for the attacker and the defender
	 */
	public int[] rollDices(int atk, int def) {		
		
		loss[0] = 0;
		loss[1] = 0;
		
		switch(atk) {								//switch per tirare 1,2 o 3 dadi dell'attaccante
		case 1:
			atkResults[0] = die1.roll();
			break;
		case 2:
			atkResults[0] = die1.roll();
			atkResults[1] = die2.roll();
			break;
		case 3:
			atkResults[0] = die1.roll();
			atkResults[1] = die2.roll();
			atkResults[2] = die3.roll();
			break;
		}
		
		switch(def) {								//switch per tirare 1,2 o 3 dadi del difensore
		case 1:
			defResults[0] = die4.roll();
			break;
		case 2:
			defResults[0] = die4.roll();
			defResults[1] = die5.roll();
			break;
		case 3:
			defResults[0] = die4.roll();
			defResults[1] = die5.roll();
			defResults[2] = die6.roll();
			break;
		}
		
		/**
		 * Sorting the arrays using a for cycle to put them in decreasing order
		 */
		Arrays.sort(atkResults);
		Arrays.sort(defResults);
		 for (int i = 0, j = 3 - 1, tmp; i < j; i++, j--) {
	            tmp = atkResults[i];
	            atkResults[i] = atkResults[j];
	            atkResults[j] = tmp;
	        }
		 for (int i = 0, j = 3 - 1, tmp; i < j; i++, j--) {
	            tmp = defResults[i];
	            defResults[i] = defResults[j];
	            defResults[j] = tmp;
	        }
		 
		
		/*System.out.println("atkArray:\n");
		for(int k=0; k < atkResults.length; k++) {
			System.out.println(atkResults[k]);
		}
		System.out.println("defArray:\n");
		for(int k=0; k < defResults.length; k++) {
			System.out.println(defResults[k]);
		}*/
		 
		 /**
		  * Increments the number of tanks lost by attacker and defender
		  */
		
		for(int i=0; i < Math.min(atk, def); i++) {			
			
			if(atkResults[i] > defResults[i]) {
				loss[1]++;
			} else {
				loss[0]++;
			}
		}
		
		return loss;
		
	}

	
		/*public static void main(String[] args){
		
		DiceShaker bussolotto = new DiceShaker();
		int[] loss = bussolotto.rollDices(3, 3);
		
		System.out.println("Perdite\n" + "Atk: " + loss[0] + "\n" + "Def: " + loss[1]);
		
	
	
		}*/
}
