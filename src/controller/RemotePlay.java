package controller;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePlay extends Remote {
    void remoteCardPressed() throws IOException;
}
