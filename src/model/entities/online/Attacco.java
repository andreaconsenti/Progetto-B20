package model.entities.online;

import model.entities.Territory;

import java.io.Serializable;

public class Attacco implements Serializable {
    private Territory attaccante;
    private Territory difensore;
    private int carriAggiornatiAttaccante;
    private int carriAggiornatiDifensore;

    public Attacco(Territory attaccante, Territory difensore, int carriAggiornatiAttaccante, int carriAggiornatiDifensore) {
        this.attaccante = attaccante;
        this.difensore = difensore;
        this.carriAggiornatiAttaccante = carriAggiornatiAttaccante;
        this.carriAggiornatiDifensore = carriAggiornatiDifensore;
    }

    public Territory getAttaccante() {
        return attaccante;
    }

    public void setAttaccante(Territory attaccante) {
        this.attaccante = attaccante;
    }

    public Territory getDifensore() {
        return difensore;
    }

    public void setDifensore(Territory difensore) {
        this.difensore = difensore;
    }


    @Override
    public String toString() {
        return "Attacco{" +
                "attaccante=" + attaccante.getName() +
                ", difensore=" + difensore.getName() +
                ", carriAggiornatiAttaccante=" + carriAggiornatiAttaccante +
                ", carriAggiornatiDifensore=" + carriAggiornatiDifensore +
                '}';
    }
}
