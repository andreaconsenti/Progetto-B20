package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePlay extends Remote {
    void callCard() throws IOException;
}
