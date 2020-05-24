package model.entities;

public class Die {
	
	private final int MAX = 6;  // maximum face value

	private int faceValue;  // current value showing on the die
	
	public Die() {
		faceValue = 1;
	}

	// Alternate Constructor
	public Die(int value) {
	   faceValue = value;
	}
	
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