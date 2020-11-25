package controller;

import model.entities.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteJoin extends Remote {
    String sayHello() throws RemoteException;
    String joinRequest(String clientInput) throws RemoteException;
    ArrayList<Player> getList() throws RemoteException;

}