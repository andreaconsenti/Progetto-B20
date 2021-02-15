package controller;

//funz
//import model.entities.RisikoGame;
import model.entities.COLOR;
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

    boolean getServerTurnClosed() throws RemoteException;
    boolean getServerAttackClosed() throws RemoteException;
    int[] getServerAtkResults() throws RemoteException;
    int[] getServerDefResults() throws RemoteException;
    int getServerAtkNumber() throws RemoteException;
    int getServerDefNumber() throws RemoteException;
    Territory getServerAtkTerritory() throws RemoteException;
    Territory getServerDefTerritory() throws RemoteException;

    int getServerAtkNewTankNum() throws RemoteException;
    int getServerDefNewTankNum() throws RemoteException;


    void remoteAttack(Territory territory1, Territory territory2, int newNumT1, int newNumT2, COLOR c1, COLOR c2) throws RemoteException;

    void remoteSetOwner() throws RemoteException;

    void remoteChangeTurn() throws RemoteException;


}
