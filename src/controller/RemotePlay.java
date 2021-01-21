package controller;

//funz
//import model.entities.RisikoGame;
import model.entities.online.RisikoGame;
import model.entities.Territory;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface RemotePlay extends Remote {
    void callCard() throws IOException;
    void remotePlaceTank(Territory remoteTerritory) throws IOException;
    boolean getServerTerrStatus() throws IOException;
    boolean getBandiera() throws IOException;
    void resetArrayList() throws RemoteException;
    //preferibile sostituire con un array di territori
    Territory getLastServerTerritory() throws RemoteException;

    ArrayList<Territory> getUpdatedTerritory() throws RemoteException;

    RisikoGame getCurrentGame() throws RemoteException;
}
