package controller;

//funz
//import model.entities.RisikoGame;
import model.entities.COLOR;
import model.entities.online.Attacco;
import model.entities.online.RisikoGame;
import model.entities.Territory;
import model.entities.online.Update;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public interface RemotePlay extends Remote {

    void remotePlaceTank(Territory remoteTerritory) throws IOException;

    boolean getBandiera() throws IOException;
    void resetArrayList() throws RemoteException;

    RisikoGame getCurrentGame() throws RemoteException;

    boolean getServerTurnClosed() throws RemoteException;
    boolean getServerAttackClosed() throws RemoteException;

    void remoteAttack(Territory territory1, Territory territory2, int newNumT1, int newNumT2, COLOR c1, COLOR c2) throws RemoteException;

    void remoteChangeTurn() throws RemoteException;

    int getServerTerrTank(Territory recTerr) throws RemoteException;

    void remoteMove(Territory t1, Territory t2, int value) throws RemoteException;

    void forceClosePostMove() throws RemoteException;

    ArrayList<Update> getUpdate() throws RemoteException;

    String getCurrentColor() throws RemoteException;

    String getRealCurrentColor() throws RemoteException;

    int getBonusLeft() throws RemoteException;

    void globalUpdate(Territory t) throws RemoteException;

    RisikoGame getFullGame() throws RemoteException;

    void closeGame() throws RemoteException;

}
