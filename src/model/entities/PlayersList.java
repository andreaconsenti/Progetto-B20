package model.entities;

import java.util.ArrayList;

public class PlayersList {
	
	public static Player[] players;
	
	public static Player[] getPlayers() {
		return players;
	}
	
	public static void setPlayers(ArrayList<Player> list) {
		players = new Player[list.size()];
		int i = 0;
		for(Player p : list) {
			players[i] = p;
			i++;
		}
	}
	
}
