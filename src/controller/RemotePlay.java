package controller;
import model.entities.COLOR;
import model.entities.online.RisikoGame;
import model.entities.Territory;
import model.entities.online.Update;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemotePlay extends Remote {

    void remotePlaceTank(Territory remoteTerritory) throws IOException;

    boolean getBandiera() throws IOException;
    void resetArrayList() throws RemoteException;

    RisikoGame getCurrentGame() throws RemoteException;

    boolean getServerTurnClosed() throws RemoteException;

    void remoteAttack() throws RemoteException;

    void remoteChangeTurn() throws RemoteException;

    void forceClosePostMove() throws RemoteException;

    ArrayList<Update> getUpdate() throws RemoteException;

    String getCurrentColor() throws RemoteException;

    String getRealCurrentColor() throws RemoteException;

    int getBonusLeft() throws RemoteException;

    void globalUpdate(Territory t) throws RemoteException;

    RisikoGame getFullGame() throws RemoteException;

    void closeGame() throws RemoteException;

}
