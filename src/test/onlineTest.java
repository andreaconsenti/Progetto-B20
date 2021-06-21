package test;

import controller.RemoteJoin;
import controller.RemotePlay;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.online.RisikoGame;
import model.entities.Territory;
import model.entities.online.Update;
import org.junit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import view.Main;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class onlineTest {

    private Territory[] clientTerritoryList;
    private Territory[] serverTerritoryList;
    private RisikoGame clientGame, serverGame;
    private RemoteJoin remoteJoin;
    private RemotePlay remotePlay;
    private Registry registry;

    @Before
    public void setUp() {

        Player p1 = new Player("PlayerA", COLOR.YELLOW, false);
        Player p2 = new Player("PlayerB", COLOR.RED, false);

        clientTerritoryList = new Territory[3];

        Territory t1 = new Territory("Alaska", 01, "NordAmerica", "F1CB80");
        t1.addTanks(0);
        t1.setOwner(p1);

        Territory t2 = new Territory("Brasile", 11, "SudAmerica", "EFF2C5");
        t2.addTanks(5);
        t2.setOwner(p1);

        Territory t3 = new Territory("Ucraina", 20, "Europa", "916768");
        t3.addTanks(10);
        t3.setOwner(p2);


        clientTerritoryList[0] = t1;
        clientTerritoryList[1] = t2;
        clientTerritoryList[2] = t3;

        Random rand = new Random();

        t1.addTanks(rand.nextInt(100));
        t2.addTanks(rand.nextInt(100));
        t2.addTanks(rand.nextInt(100));

        Territory[] serverTerritoryList = new Territory[3];
        serverTerritoryList[0] = t1;
        serverTerritoryList[1] = t2;
        serverTerritoryList[2] = t3;

        Player[] players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        try {
            clientGame = new RisikoGame(players, "assets/RisikoClassic/territori.txt", "assets/RisikoClassic/continenti.txt", "assets/RisikoClassic/obiettivi.txt");
            serverGame = new RisikoGame(players, "assets/RisikoClassic/territori.txt", "assets/RisikoClassic/continenti.txt", "assets/RisikoClassic/obiettivi.txt");
        } catch (IOException exception) {
            System.out.println("File non trovati");
        }


    }
    @Before()
    public void setServer() {
        registry = new Registry() {
            @Override
            public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
                return null;
            }

            @Override
            public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {

            }

            @Override
            public void unbind(String name) throws RemoteException, NotBoundException, AccessException {

            }

            @Override
            public void rebind(String name, Remote obj) throws RemoteException, AccessException {

            }

            @Override
            public String[] list() throws RemoteException, AccessException {
                return new String[0];
            }
        };
    }

    @Test
    /*
     *  Testa l'aggiornamento del server con territori aggiornati dal client
     */
    public void aggiornaMappa() throws RemoteException {

        setUp();

        for(int i = 0; i < clientTerritoryList.length; i++) {
            aggiornaTerritorio(clientTerritoryList[i]);
        }

        for(int i = 0; i < clientTerritoryList.length; i++) {
            Territory clientTerr = clientTerritoryList[i];
            Territory serverTerr = serverGame.getTerritory(clientTerr);

            assertEquals(clientTerr.getTanks(), serverTerr.getTanks());
            assertEquals(clientTerr.getOwner(), serverTerr.getOwner());
        }
    }


    @Test
    /*
     *  Testa corretta attivazione del server RMI e relativa risposta
     */
    public void attivaServer() throws RemoteException, UnknownHostException {
        TestObject testObject = new TestObject();
        RemoteJoin stub = (RemoteJoin) UnicastRemoteObject.exportObject(testObject, 0);
        System.setProperty("java.rmi.server.hostname", Inet4Address.getLocalHost().getHostAddress());
        registry.rebind("Test", stub);
        assertEquals("OK", stub.sayHello());
    }


    @Test
    /*
     *  Testa il cambio turno remoto
     */
    public void cambioTurnoRemoto() throws RemoteException, UnknownHostException {
        Player giocatoreIniziale = serverGame.getCurrentTurn();
        TestObject testObject = new TestObject();
        RemotePlay stub = (RemotePlay) UnicastRemoteObject.exportObject(testObject,0);
        System.setProperty("java.rmi.server.hostname", Inet4Address.getLocalHost().getHostAddress());
        registry.rebind("Test", stub);

        stub.remoteChangeTurn();
        Player giocatoreSuccessivo = serverGame.getCurrentTurn();

        assertNotEquals(giocatoreIniziale,giocatoreSuccessivo);
    }

    @Test
    /*
     *  Testa il posizionamento su un territorio del server da parte del client
     */
    public void posizionaSuServer() throws IOException, InterruptedException {
        TestObject testObject = new TestObject();
        RemotePlay stub = (RemotePlay) UnicastRemoteObject.exportObject(testObject,0);
        System.setProperty("java.rmi.server.hostname", Inet4Address.getLocalHost().getHostAddress());
        registry.rebind("Test", stub);

        //Prelevo territorio casuale dal client
        Random random = new Random();
        ArrayList<Territory> territoriClient = clientGame.getTerritories();
        int rndInt = random.nextInt(territoriClient.size());
        Territory territorioCasualeClient = territoriClient.get(rndInt);


        int vecchiaQuantita = serverGame.getTerritory(territorioCasualeClient).getTanks();

        //aggiungo carro su server
        stub.remotePlaceTank(territorioCasualeClient);

        int nuovaQuantita = serverGame.getTerritory(territorioCasualeClient).getTanks();

        assertEquals(vecchiaQuantita + 1, nuovaQuantita);

    }


    public void aggiornaTerritorio(Territory territorioClient) {

        Player newPlayer = serverGame.getPlayerByColor(territorioClient.getOwner().getColor());
        serverGame.getTerritory(territorioClient).setOwner(newPlayer);
        serverGame.getTerritory(territorioClient).removeTanks(serverGame.getTerritory(territorioClient).getTanks());
        serverGame.getTerritory(territorioClient).addTanks(territorioClient.getTanks());

    }
    public class TestObject implements RemoteJoin, RemotePlay {
        @Override
        public String sayHello() throws RemoteException {
            return "OK";
        }

        @Override
        public String joinRequest(String clientInput) throws RemoteException {
            return null;
        }

        @Override
        public ArrayList<Player> getList() throws RemoteException {
            return null;
        }

        @Override
        public String getMap() throws RemoteException {
            return null;
        }

        @Override
        public String getTerritories() throws RemoteException {
            return null;
        }

        @Override
        public String getTerrFile() throws RemoteException {
            return null;
        }

        @Override
        public String getContinentFile() throws RemoteException {
            return null;
        }

        @Override
        public String getMissions() throws RemoteException {
            return null;
        }

        @Override
        public boolean gameIsReady() throws RemoteException {
            return false;
        }


        @Override
        public void remotePlaceTank(Territory remoteTerritory) throws IOException {
            serverGame.getTerritory(remoteTerritory).addTanks(1);
        }

        @Override
        public boolean getBandiera() throws IOException {
            return false;
        }

        @Override
        public void resetArrayList() throws RemoteException {

        }

        @Override
        public RisikoGame getCurrentGame() throws RemoteException {
            return null;
        }

        @Override
        public boolean getServerTurnClosed() throws RemoteException {
            return false;
        }


        @Override
        public void remoteChangeTurn() throws RemoteException {
            serverGame.nextTurn();
        }

        @Override
        public void forceClosePostMove() throws RemoteException {

        }

        @Override
        public ArrayList<Update> getUpdate() throws RemoteException {
            return null;
        }

        @Override
        public String getCurrentColor() throws RemoteException {
            return null;
        }

        @Override
        public String getRealCurrentColor() throws RemoteException {
            return null;
        }

        @Override
        public int getBonusLeft() throws RemoteException {
            return 0;
        }

        @Override
        public void globalUpdate(Territory t) throws RemoteException {

        }

        @Override
        public RisikoGame getFullGame() throws RemoteException {
            return null;
        }

        @Override
        public void closeGame() throws RemoteException {

        }
    };
}
