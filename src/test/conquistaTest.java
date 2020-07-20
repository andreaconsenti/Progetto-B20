package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.entities.*;
import model.entities.Mission.MISSION_TYPE;
import model.util.FileHandler;

class conquistaTest {
	
	private static Player [] p;
	private static Player p1;
	private ArrayList<Continent> continenti;
	private ArrayList<Territory> list;
	private ArrayList<Mission> missioni;
	private int nLine=0;
	private int nLineCont=0;
	private int nLineM;
	private RisikoGame g;
	private static int [] atkResults;
	private static int [] defResults;
//	private static String path1 = "assets/RisikoClassic/territori.txt";
//	private static String path2 = "assets/RisikoClassic/continenti.txt";
//	private static String path3 = "assets/RisikoClassic/obiettivi.txt";
//	private static FileHandler f;
	
	@BeforeEach
	public void setUp() throws NumberFormatException, IOException {
//		Player p1 = new Player("Ale", COLOR.BLUE, false);
		p1 = new Player("Ale", COLOR.BLUE, false);
//		Player p2 = new Player("Fra", COLOR.RED, false);
//		Player p3 = new Player("Luca", COLOR.GREEN, false);
//		
		Player[] p = {p1};
		int [] atkResults = {3, 4, 6};
		int [] defResults = {2, 5, 5};
//		
		g = new RisikoGame(p, "assets/RisikoClassic/territori.txt", "assets/RisikoClassic/continenti.txt", "assets/RisikoClassic/obiettivi.txt");
	}
	
	private void createLists() throws IOException {
		list = createTerritories("assets/RisikoClassic/territori.txt");
		continenti = createCompleteContinent("assets/RisikoClassic/continenti.txt");
		missioni = createMissions("assets/RisikoClassic/obiettivi.txt", continenti);
	}
	
	/*
	 * Test che controlla che le dimensioni dell'arraylist dei continenti e dei territori 
	 * siano effettivamente pari al numero di continenti e territori
	 * che sono presenti nel gioco (versione RisikoClassic)
	 */
	
	@Test
	void sizeTest() throws NumberFormatException, IOException {
		createLists();
		assertEquals(6, continenti.size());
		assertEquals(42, list.size());
		assertEquals(9, missioni.size());
	}
	
//	@Test
//	void conquistaTerritorio() {
//		
//	}
	
	@Test
	void conquistaObiettivo1() throws NumberFormatException, IOException {
		createLists();
		int i=0;
		if(p1.getMission().getType().equals(MISSION_TYPE.TYPE1)) {
			if(p1.getTerritories()==24) {
				for(Territory t : list) {
					if(!(t.getTanks()>=2)) {
						i=1;
					}
				}
				if(i==0)
					g.verifyMission();
			}
		}
		assertEquals(true, g.verifyMission());
	}
	
	@Test
	void conquistaObiettivo2() throws IOException {
		createLists();
		if(p1.getMission().getType().equals(MISSION_TYPE.TYPE2)) {
			if(p1.getMission().getContinent1().getRandomTerritory().getOwner().equals(p1) && p1.getMission().getContinent2().getRandomTerritory().getOwner().equals(p1)) {
				if(p1.getContinents()>2 && p1.getMission().hasContinent3()) {
					g.verifyMission();
				}
			}
		}
		assertEquals(true, g.verifyMission());
	}
	
	private ArrayList<Territory> createTerritories (String path) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		int n = Integer.parseInt(in.readLine());
		
		list = new ArrayList<Territory>();
		
		String name;
		String continent;
		int code;
		String color;
			
		for(int i = 0; i<n; i++) {
			
			line = in.readLine();
			code = Integer.parseInt(line.substring(0,2));
			StringTokenizer st = new StringTokenizer (line.substring(4));
			color = st.nextToken();
			int k=Integer.parseInt(line.substring(22, 23));
			int j=k*3;
			StringTokenizer s = new StringTokenizer (line.substring(j+23));
			name = s.nextToken();
			continent = s.nextToken();
			
			if(!addTerritory(new Territory(name, code, continent, color))) {
				break;
			}
		}
		in.close();
		return list;
	}
	
	public boolean addTerritory(Territory t) {
		if(nLine <= list.size()) {
			list.add(t);
			nLine++;
			return true;
		}
		return false;
	}
	
	private ArrayList<Continent> createCompleteContinent(String path) throws NumberFormatException, IOException{
		
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		int n = Integer.parseInt(in.readLine());
		String name;
		int bonus;
		int code;
		
		continenti = new ArrayList<Continent>();
		
		for(int i = 0; i<n; i++) {
			
			line = in.readLine();
			code = Integer.parseInt(line.substring(0,2));
			StringTokenizer st = new StringTokenizer (line.substring(2));
			name = st.nextToken();
			bonus = Integer.parseInt(st.nextToken());
			
			if(!addContinents(new Continent(code, name, bonus))) {
				break;
			}
		}
		in.close();
		return continenti;
	}
	
	public boolean addContinents(Continent c) {
		if(nLineCont <= list.size()) {
			continenti.add(c);
			nLineCont++;
			return true;
		}
		return false;
	}
	
	private ArrayList<Mission> createMissions(String path, ArrayList<Continent> continenti) throws NumberFormatException, IOException{
		
		BufferedReader in= new BufferedReader(new FileReader(path));
		String line;
		int n=Integer.parseInt(in.readLine());
		
		missioni=new ArrayList<Mission>();
		
		int nty;
		int ntk;
		Continent cont1 = null;
		Continent cont2 = null;
		int codeMission;
		int typeMission;
		int code;
		int code2;
		int code3;
		boolean temp;
		
		for(int i=0; i<n; i++) {
			line=in.readLine();
			typeMission=Integer.parseInt(line.substring(0, 1));
			StringTokenizer st=new StringTokenizer(line.substring(1));
			codeMission=Integer.parseInt(st.nextToken());
			switch(typeMission) {
				case 1:
					nty=Integer.parseInt(st.nextToken());
					ntk=Integer.parseInt(st.nextToken());
					if(!addMission(new Mission(nty, ntk, codeMission))) {
						break;
					}
					break;
				case 2:
					code=Integer.parseInt(st.nextToken());
					code2=Integer.parseInt(st.nextToken());
					code3=Integer.parseInt(st.nextToken());
					if(code3==1)
						temp=true;
					else
						temp=false;
					cont1=continenti.get((code)-1);
					cont2=continenti.get((code2)-1);
					if(!addMission(new Mission(cont1, cont2, temp, codeMission))) {
						break;
					}
					break;
			}	
		}
		in.close();
		return missioni;
	}
	
	/**
	 * Adds a mission to the list of missions and verifies if the operation was successful
	 * @param m is the mission added
	 * @return boolean
	 */
	public boolean addMission(Mission m) {
		if(nLineM<=missioni.size()) {
			missioni.add(m);
			nLineM++;
			return true;
		}
		return false;
	}

	
}