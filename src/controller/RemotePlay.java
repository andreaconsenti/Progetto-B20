package controller;

import model.entities.RisikoGame;
import model.entities.Territory;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface RemotePlay extends Remote {
    void callCard() throws IOException;
    void remotePlaceTank(Territory remoteTerritory) throws IOException;
    boolean getServerTerrStatus() throws IOException;
    Territory getLastServerTerritory() throws RemoteException;
    RisikoGame getCurrentGame() throws RemoteException;
}
