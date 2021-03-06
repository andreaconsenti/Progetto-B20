package controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import model.entities.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteJoin extends Remote {
    String sayHello() throws RemoteException;
    String joinRequest(String clientInput) throws RemoteException;
    ArrayList<Player> getList() throws RemoteException;
    String getMap() throws RemoteException;
    String getTerritories() throws RemoteException;
    String getTerrFile() throws RemoteException;
    String getContinentFile() throws RemoteException;
    String getMissions() throws RemoteException;
    boolean gameIsReady() throws RemoteException;


}