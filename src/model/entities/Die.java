package model.entities;

public class Die {
	
	private final int MAX = 6;  

	private int faceValue;  
	
	/**
	 * Creates a die
	 */
	public Die() {
		faceValue = 1;
	}

	/**
	 * Alternative constructor to create a due
	 * @param value is used to set the face showing on the die
	 */
	public Die(int value) {
	   faceValue = value;
	}
	
	/**
	 * Rolls the die generating a number between 1 and 6
	 * @return faceValue
	 */
	public int roll() {				
		faceValue = (int)(Math.random() * MAX) + 1;
	    return faceValue;
	}
	
	
	/*public static void main(String[] args){
		
		Die dado1 = new Die();
		Die dado2 = new Die();
		
		System.out.println("Risultato dado 1 : " + dado1.roll() + "\n");
		System.out.println("Risultato dado 2 : " + dado2.roll() + "\n");
		
	}*/
}