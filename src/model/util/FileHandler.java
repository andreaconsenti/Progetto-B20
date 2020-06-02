package model.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import model.entities.Card;
import model.entities.Continent;
import model.entities.FIGURE;
import model.entities.Mission;
import model.entities.RisikoGame;
import model.entities.Territory;

public class FileHandler {
	private ArrayList<Territory> list;
	private ArrayList<Card> card;
	private ArrayList<Mission> missions;
	private ArrayList<Continent> continents;
	private int nLine = 0;
	private int nLineC=0;
	private int nLineM=0;
	private int nLineCont=0;
	
	// Method that generates an ArrayList which contains all territories
	public ArrayList<Territory> genTerritories(String path) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		
		list = new ArrayList<Territory>();
		int n = Integer.parseInt(in.readLine());
		
		String name;
		String continent;
		int code;
		String color;
			
		for(int i = 0; i<n; i++) {
			
			line = in.readLine();
			code = Integer.parseInt(line.substring(0,2));
			StringTokenizer st = new StringTokenizer (line.substring(2));
			name = st.nextToken();
			continent = st.nextToken();
			color = st.nextToken();
			
			
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
	
	public ArrayList<Territory> addConfinanti(ArrayList<Territory> list, String path) throws NumberFormatException, IOException{
		
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;;
		int id;
		int n_confini;
		
		ArrayList<Territory> tempList;
		
		int n = Integer.parseInt(in.readLine());
		
		for(int i = 0; i<n; i++) {
			
			tempList = new ArrayList<Territory>();
			line = in.readLine();
			id = Integer.parseInt(line.substring(0,2));
			StringTokenizer st = new StringTokenizer (line.substring(2));
			n_confini = Integer.parseInt(st.nextToken());
			
			for(int k = 0; k<n_confini; k++) {
				
				tempList.add(list.get(Integer.parseInt(st.nextToken())-1));
			}
			
			list.get(id-1).setConfinanti(tempList);
		}
		in.close();
		return list;	
	}
	
	public ArrayList<Card> genCards(ArrayList<Territory> list, String path) throws NumberFormatException, IOException{
		
		BufferedReader in= new BufferedReader(new FileReader(path));
		String line;
		
		card=new ArrayList<Card>();
		int n=Integer.parseInt(in.readLine());
		
		Territory territory=null;
		FIGURE figure=null;		// Capire se lasciarlo di tipo String o trovare il modo per farlo leggere direttamente come tipo della ENUM
		int code;
		
		for(int i=0; i<n; i++) {
			line=in.readLine();
			code=Integer.parseInt(line.substring(0, 2));
			StringTokenizer st=new StringTokenizer(line.substring(2));
			int k=Integer.parseInt(st.nextToken());
			switch (k) {
				case 1:
					figure=FIGURE.JOLLY;
					territory=null;
					break;
				case 2:
					figure=FIGURE.FANTE;
					if(code<43)
						territory=list.get((code)-1);
					break;
				case 3:
					figure=FIGURE.CANNONE;
					if(code<43)
						territory=list.get((code)-1);
					break;
				case 4:
					figure=FIGURE.CAVALIERE;
					if(code<43)
						territory=list.get((code)-1);
					break;
			}
	
			if(!addCard(new Card(figure, territory, code))) {
				break;
			}
		}
		in.close();
		return card;
	}
	
	public boolean addCard(Card c) {
		if(nLineC<=card.size()) {
			card.add(c);
			nLineC++;
			return true;
		}
		return false;
	}
	
	public ArrayList<Continent> genContinents (String path) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		
		continents = new ArrayList<Continent>();
		int n = Integer.parseInt(in.readLine());
		
		String name;
		int bonus;
		int code;
			
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
		return continents;
	}
	
	public boolean addContinents(Continent c) {
		if(nLineCont <= list.size()) {
			continents.add(c);
			nLineCont++;
			return true;
		}
		return false;
	}
	
	public ArrayList<Mission> genMissions(String path, ArrayList<Continent> continents) throws NumberFormatException, IOException{
		
		BufferedReader in= new BufferedReader(new FileReader(path));
		String line;
		
		missions=new ArrayList<Mission>();
		int n=Integer.parseInt(in.readLine());
		
		int nty;
		int ntk;
		Continent cont1 = null;
		Continent cont2 = null;
		int codeMission;
		int typeMission;
		int code;
		int code2;
		int j;
		
		
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
					cont1=continents.get((code)-1);
					cont2=continents.get((code2)-1);
					if(!addMission(new Mission(cont1, cont2, false, codeMission))) {
						break;
					}
					break;
			}	
		}
		in.close();
		return missions;
	}
	
	public boolean addMission(Mission m) {
		if(nLineM<=missions.size()) {
			missions.add(m);
			nLineM++;
			return true;
		}
		return false;
	}

	
//		TEST MAIN
	
	
	
	public void printTerritories(ArrayList<Territory> list) {
		for(Territory t : list) {
			System.out.println(t.toString());
		}
	}
	
	public void printCards(ArrayList<Card> card) {
		for(Card c : card) {
			c.printCard();
		}
	}
	
	public void printContinents(ArrayList<Continent> continents) {
		for(Continent cont: continents) {
			System.out.println(cont.getName());
		}
	}
	
	public void printMissions(ArrayList<Mission> missions) {
		for(Mission m : missions) {
			m.printMission();
		}
	}
	
//	public static void main(String[] args) throws NumberFormatException, IOException {
//		FileHandler f = new FileHandler();
//		System.out.println("-----TERRITORI-----\n");
//		ArrayList<Territory> lista = f.genTerritories("assets/TerritoriEColori.txt");
////		System.out.println(lista.get(0));		//Commentato perché in output faceva uscire 2 volte "Alaska"  @author AleCarbo
//		f.printTerritories(lista);
//		System.out.println("-----CONFINI-----\n");
//		ArrayList<Territory> listaCompleta = f.addConfinanti(lista, "assets/confini.txt");
//		for(Territory t : listaCompleta) {
//			t.printConfini();
//		}
//		System.out.println("-----CARTE-----\n");
//		ArrayList<Card> carte=f.genCards(lista, "assets/carte.txt");
//		f.printCards(carte);
//		System.out.println("-----CONTINENTI-----");
//		ArrayList<Continent> continenti=f.genContinents("assets/continenti.txt");
//		f.printContinents(continenti);
//		
//		System.out.println("\n-----OBIETTIVI-----\n");
////		f.genContinents();
//		ArrayList<Mission> missioni=f.genMissions("assets/obiettivi.txt", continenti);
//		f.printMissions(missioni);
//		
//	}	
}