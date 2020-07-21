package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class attaccoTest {
	
	private int[] atkResults;
	private int[] defResults;
	private int[] loss;
	private int atk;
	private int def;

	@BeforeEach
	public void setUp() throws NumberFormatException, IOException {
		atkResults = new int[3];
		defResults = new int[3];
		loss = new int[2];
	}
	
	@Test
	/*
	 * Test di attacco con 3 dadi per l'attaccante e 3 per il difensore. L'attacco perde 2 tanks, la difesa 1.
	 */
	void vittoriaAttacco1() {
		int[] input1 = {2, 1};
		atkResults[0] = 6;
		atkResults[1] = 4;
		atkResults[2] = 4;
		
		defResults[0] = 6;
		defResults[1] = 1;
		defResults[2] = 4;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, atk, def);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}
	
	
	@Test
	/*
	 * Test di attacco con 3 dadi per l'attaccante e 3 per il difensore. L'attacco perde 1 tank, la difesa 2.
	 */
	void vittoriaAttacco2() {
		int[] input1 = {1, 2};
		atkResults[0] = 6;
		atkResults[1] = 4;
		atkResults[2] = 4;
		
		defResults[0] = 5;
		defResults[1] = 1;
		defResults[2] = 4;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, atk, def);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}
	
	@Test
	/*
	 * Test di attacco con 3 dadi per l'attaccante e 3 per il difensore. L'attacco perde 0 tanks, la difesa 3.
	 */
	void vittoriaAttacco3() {
		int[] input1 = {0, 3};
		atkResults[0] = 6;
		atkResults[1] = 4;
		atkResults[2] = 4;
		
		defResults[0] = 3;
		defResults[1] = 1;
		defResults[2] = 2;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, atk, def);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}
	
	@Test
	/*
	 * Test di attacco con 3 dadi per l'attaccante e 3 per il difensore. L'attacco perde 3 tanks, la difesa 0.
	 */
	void vittoriaAttacco4() {
		int[] input1 = {3, 0};
		atkResults[0] = 2;
		atkResults[1] = 4;
		atkResults[2] = 3;
		
		defResults[0] = 6;
		defResults[1] = 5;
		defResults[2] = 4;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, atk, def);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}
	
	@Test
	/*
	 * Test di attacco con 2 dadi per l'attaccante e 3 per il difensore. L'attacco perde 2 tanks, la difesa 0.
	 */
	void vittoriaAttacco5() {
		int[] input1 = {2, 0};
		atkResults[0] = 2;
		atkResults[1] = 4;
		
		defResults[0] = 6;
		defResults[1] = 5;
		defResults[2] = 4;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, 2, 3);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}

	@Test
	/*
	 * Test di attacco con 2 dadi per l'attaccante e 3 per il difensore. L'attacco perde 1 tank, la difesa 1.
	 */
	void vittoriaAttacco6() {
		int[] input1 = {1, 1};
		atkResults[0] = 4;
		atkResults[1] = 5;
		
		defResults[0] = 6;
		defResults[1] = 3;
		defResults[2] = 3;
		atk = 2;
		def = 3;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, atk, def);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}

	@Test
	/*
	 * Test di attacco con 2 dadi per l'attaccante e 3 per il difensore. L'attacco perde 0 tanks, la difesa 2.
	 */
	void vittoriaAttacco7() {
		int[] input1 = {0, 2};
		atkResults[0] = 4;
		atkResults[1] = 4;
		
		defResults[0] = 1;
		defResults[1] = 2;
		defResults[2] = 3;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, 2, 3);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}

	@Test
	/*
	 * Test di attacco con 1 dado per l'attaccante e 3 per il difensore. L'attacco perde 0 tank, la difesa 1.
	 */
	void vittoriaAttacco8() {
		int[] input1 = {0, 1};
		atkResults[0] = 4;
		
		defResults[0] = 1;
		defResults[1] = 2;
		defResults[2] = 3;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, 1, 3);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}

	@Test
	/*
	 * Test di attacco con 1 dadi per l'attaccante e 3 per il difensore. L'attacco perde 1 tank, la difesa 0.
	 */
	void vittoriaAttacco9() {
		int[] input1 = {1, 0};
		atkResults[0] = 4;
		
		defResults[0] = 5;
		defResults[1] = 2;
		defResults[2] = 3;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, 1, 3);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}

	@Test
	/*
	 * Test di attacco con 2 dadi per l'attaccante e 2 per il difensore. L'attacco perde 1 tank, la difesa 1.
	 */
	void vittoriaAttacco10() {
		int[] input1 = {1, 1};
		atkResults[0] = 4;
		atkResults[1] = 4;
		
		defResults[0] = 1;
		defResults[1] = 5;
		atk = atkResults.length;
		def = defResults.length;
		Arrays.sort(atkResults);
		for (int i = 0, j = atkResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = atkResults[i];
	           atkResults[i] = atkResults[j];
	           atkResults[j] = tmp;
	       }
		Arrays.sort(defResults);
		for (int i = 0, j = defResults.length - 1, tmp; i < j; i++, j--) {
	           tmp = defResults[i];
	           defResults[i] = defResults[j];
	           defResults[j] = tmp;
	       }
		battle(atkResults, defResults, 2, 2);
		assertEquals(input1[0], loss[0]);
		assertEquals(input1[1], loss[1]);
	}
	
	private int[] battle(int[] atkResults, int[] defResults, int atk, int def) {
		loss[0]=0;
		loss[1]=0;
		
		int n = Math.min(atk, def);	
		
		for(int i=0; i<n; i++) {
			if(atkResults[i] > defResults[i]) {
				loss[1]++;
			} else {
				loss[0]++;
			}
		}
		return loss;
	}
}