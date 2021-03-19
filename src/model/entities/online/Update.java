package model.entities.online;

import model.entities.Player;
import model.entities.Territory;

import java.io.Serializable;

public class Update implements Serializable {

    private Territory territory;
    private Player owner;
    private int tanks;


    public Update(Territory territory, Player owner, int tanks) {
        this.territory = territory;
        this.owner = owner;
        this.tanks = tanks;

    }

    public Territory getTerritory() {
        return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getTanks() {
        return tanks;
    }

    public void setTanks(int tanks) {
        this.tanks = tanks;
    }

}
