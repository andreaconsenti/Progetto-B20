package model.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import model.entities.Territory;

public class FileHandler {
	private ArrayList<Territory> list;
	private int nLine = 0;
	
	
	// Method that generates an ArrayList which contains all territories
	public ArrayList<Territory> genTerritories(String path) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		
		list = new ArrayList<Territory>();
		int n = Integer.parseInt(in.readLine());
		
		String name;
		String continent;
		int code;
		
		
		for(int i = 0; i<n; i++) {
			
			line = in.readLine();
			code = Integer.parseInt(line.substring(0,2));
			StringTokenizer st = new StringTokenizer (line.substring(2));
			name = st.nextToken();
			continent = st.nextToken();
			
			if(!addTerritory(new Territory(name, code, continent))) {
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
		return list;
		
		
		
	}
	
	
	

	
	// TEST MAIN
	
	
	
	
	/*public void printTerritories(ArrayList<Territory> list) {
		for(Territory t : list) {
			System.out.println(t.toString());
		}
	}
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		FileHandler f = new FileHandler();
		ArrayList<Territory> lista = f.genTerritories("assets/TerritoriEConfini.txt");
		//System.out.println(lista.get(0));
		//f.printTerritories(lista);
		
		
		ArrayList<Territory> listaCompleta = f.addConfinanti(lista, "assets/confini.txt");
		
		for(Territory t : listaCompleta) {
			t.printConfini();
		}
		
	}*/
	
	
}
