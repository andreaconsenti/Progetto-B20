package controller.online;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.EventObject;

import controller.RemoteJoin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.PlayersList;

public class OnlineSceneController implements RemoteJoin, Serializable {
    @FXML
    private Button backButton;
    @FXML
    private TextField nameField;
    @FXML
    private Button serverButton;
    @FXML
    private Button startButton;
    @FXML
    private Button partecipaButton;
    @FXML
    private ListView<String> playerList;
    @FXML
    private ListView<Player> defVisualPlayerList;
    @FXML
    private TextField serverStatusField;
    @FXML
    private Button lockListButton;
    @FXML
    private Button getInfoButton;
    @FXML
    private MenuButton mapInput;
    @FXML
    private MenuItem map1;
    @FXML
    private MenuItem map2;
    @FXML
    private MenuButton mapinput;
    @FXML
    private ImageView mapPreview;
    @FXML
    private Label ipLabel;
    @FXML
    public TextField remoteServerField;
    @FXML
    private Label clientWaitLabel;


    private ArrayList<Player> list;
    public static Registry registry;
    public static RemoteJoin stub;

    public boolean gameReady;

    private boolean mapChosen;
    public static String map;
    public static String territories;
    public static String terrFile;
    public static String continentsFile;
    public static String missions;

    public static String myColor;
    public static boolean amIaServer;
    public static boolean amIaClient;



    /**
     * Manages the pressure of the Indietro button, exiting the rules
     *
     * @param event is the event generated
     * @throws IOException
     */
    public void backPressed(ActionEvent event) throws IOException {
        Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/view.fxml"));
        Scene playerScene = new Scene(playerSceneParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(playerScene);
        window.show();

        if (serverButton.isDisabled()) {
            /*
             * Risolvere problema del server rimasto aperto
             * */
        }
    }

    /**
     * Manages the pressure of the Server button
     *
     * @param event is the event generated
     * @throws IOException
     */
    public void serverPressed(ActionEvent event) {
        try {
            amIaServer = true;
            RemoteJoin stub = (RemoteJoin) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(1888);
            //ultimo funz System.setProperty("java.rmi.server.hostname", "192.168.1.204");
            System.setProperty("java.rmi.server.hostname", Inet4Address.getLocalHost().getHostAddress());
            registry.rebind("Hello", stub);
            System.out.println("Server ready.");
            serverStatus("ok");
            list = new ArrayList<Player>();

            ipLabel.setVisible(true);

            try {
                System.out.println("MIO IP: " + Inet4Address.getLocalHost().getHostAddress());
                ipLabel.setText("IP ASSEGNATO A SERVER: " + Inet4Address.getLocalHost().getHostAddress() + "\n      Comunicalo ai tuoi avversari.");
            } catch (UnknownHostException e1) {}

        } catch (Exception e) {
            serverStatus("err");
            e.printStackTrace();
        }

    }

    /**
     * Manages the printing of server info
     *
     * @param
     * @throws
     */
    public void serverStatus(String activationResponse) {
        if (activationResponse.equals("ok")) {
            serverStatusField.setText("Attivo");
            serverStatusField.setStyle("-fx-text-fill: green");
            serverButton.setDisable(true);
        }
        if (activationResponse.equals("err")) {
            serverStatusField.setText("Errore");
            serverStatusField.setStyle("-fx-text-fill: #ff0000");
        }

    }

    /**
     * Manages the pressure of Partecipa button. It registers the client among the server players.
     *
     * @param event is the event generated
     * @throws
     */
    public void partecipaPressed(ActionEvent event) {
        try {
            amIaClient = true;
            registry = LocateRegistry.getRegistry(remoteServerField.getText(), 1888);
            stub = (RemoteJoin) registry.lookup("Hello");
            String nameFieldValue = nameField.getText();
            String response = stub.joinRequest(nameFieldValue);
            myColor = response;

            System.out.println("Colore assegnato: " + myColor);
            Thread.sleep(500);


            if(amIaServer == false) {
                while(stub.gameIsReady() == false) {
                    Thread.sleep(500);
                }
                clientWaitLabel.setVisible(true);
                clientWaitLabel.setText("SERVER HA CONFIGURATO LA PARTITA! PREMI 'GIOCA!' PER CONTINUARE");

                getInfoButton.setDisable(false);
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Manages the Maps Drop-Down Menu
     *
     * @param
     * @throws
     */
    public void mapChoseEnabler() {

        mapinput.setDisable(false);
        map1.setOnAction(e -> {
            mapSelected("src/view/fxmls/images/Maps/RisikoClassic/map_preview.png", "src/view/fxmls/images/Maps/RisikoClassic/map.jpg",
                    "src/view/fxmls/images/Maps/RisikoClassic/territories.png", "assets/RisikoClassic/territori.txt", "assets/RisikoClassic/continenti.txt", "assets/RisikoClassic/obiettivi.txt");
        });

        map2.setOnAction(e -> {
            mapSelected("src/view/fxmls/images/Maps/SPQRisiko/map_preview.png", "src/view/fxmls/images/Maps/SPQRisiko/map.jpg",
                    "src/view/fxmls/images/Maps/SPQRisiko/territories.png", "assets/SPQRisiko/territori.txt", "assets/SPQRisiko/continenti.txt", "assets/SPQRisiko/obiettivi.txt");
        });

    }

    /**
     * Manages the pressure of "Accetta le connessioni" button. It accepts registered players.
     *
     * @param event is the event generated
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void chiudiPressed(ActionEvent event) throws RemoteException, NotBoundException {

        stub = (RemoteJoin) registry.lookup("Hello");


        partecipaButton.setDisable(true);
        nameField.setEditable(false);

        defVisualPlayerList.setVisible(true);
        ArrayList<String> lista = new ArrayList<>(playerList.getItems());
        defVisualPlayerList.getItems().setAll(list);

        mapChoseEnabler();
    }

    /**
     * Chooses colors for clients
     *
     * @param
     * @throws
     */
    public COLOR autoColorChooser() {

        int listSize = list.size();
        switch (listSize) {
            case 0:
                return COLOR.YELLOW;
            case 1:
                return COLOR.RED;
            case 2:
                return COLOR.BLACK;
            case 3:
                return COLOR.BLUE;
            case 4:
                return COLOR.GREEN;
            default:
                return COLOR.PINK;
        }
    }

    /**
     * Sets the map after the server drop down selection
     *
     * @param
     * @throws
     */
    private void mapSelected(String image, String mapImage, String terrImage, String terrFiles, String contsFile, String missFile) {

        getInfoButton.setDisable(false);

        if (mapImage.equals("src/view/fxmls/images/Maps/RisikoClassic/map.jpg")) {
            mapinput.setText(map1.getText());
        }
        if (mapImage.equals("src/view/fxmls/images/Maps/SPQRisiko/map.jpg")) {
            mapinput.setText(map2.getText());
        }

        mapinput.setStyle("-fx-text-fill: black;");
        File file = new File(image);
        Image temp = new Image(file.toURI().toString());
        mapPreview.setImage(temp);
        map = mapImage;
        territories = terrImage;
        terrFile = terrFiles;
        continentsFile = contsFile;
        missions = missFile;
        mapChosen = true;
    }

    /**
     * First Java RMI test
     *
     * @param
     * @throws
     */
    @Override
    public String sayHello() throws RemoteException {
        return "hello funziona";
    }

    /**
     * RMI Client Join request. Returns
     *
     * @param
     * @throws RemoteException
     * @return new client color
     */
    @Override
    public String joinRequest(String clientInput) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                playerList.getItems().add(clientInput);
                list.add(new Player(clientInput, autoColorChooser(), false));
            }
        });

        return autoColorChooser().toString();
    }

    /**
     * RMI players list request.
     *
     * @param
     * @throws RemoteException
     * @return complete player list from server
     */
    @Override
    public ArrayList<Player> getList() throws RemoteException {
        return list;
    }

    /**
     * RMI game map request.
     *
     * @param
     * @throws RemoteException
     * @return game map
     */
    @Override
    public String getMap() throws RemoteException {
        return map;
    }

    /**
     * RMI territories list request
     *
     * @param
     * @throws RemoteException
     * @return full game territories as String
     */
    @Override
    public String getTerritories() throws RemoteException {
        return territories;
    }

    /**
     * RMI TerrFile name request
     *
     * @param
     * @throws RemoteException
     * @return TerrFile name
     */
    @Override
    public String getTerrFile() throws RemoteException {
        return terrFile;
    }

    /**
     * RMI ContinentFile request.
     *
     * @param
     * @throws RemoteException
     * @return ContinentsFile name
     */
    @Override
    public String getContinentFile() throws RemoteException {
        return continentsFile;
    }

    /**
     * RMI Missions request.
     *
     * @param
     * @throws RemoteException
     * @return missions file
     */
    @Override
    public String getMissions() throws RemoteException {
        return missions;
    }


    /**
     * RMI Client Join request. Returns
     *
     * @param
     * @throws RemoteException
     * @return true if Server has started the game, else false
     */
    @Override
    public boolean gameIsReady() throws RemoteException {
        return gameReady;
    }

    /**
     * Manages the Gioca! Button pressing
     * @param
     * @throws IOException
     * @return new client color
     */
    public void startGamePressed2(ActionEvent event) throws IOException {
        // Adattare il controller del GameScene.fxml per poter caricare
        // il nuovo scenario!

        list = stub.getList();
        map = stub.getMap();
        territories = stub.getTerritories();
        terrFile = stub.getTerrFile();
        continentsFile = stub.getContinentFile();
        missions = stub.getMissions();

        if(amIaServer) {
            gameReady = true;
        }

        PlayersList.setPlayers(list);
        Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/OnlineGameScene.fxml"));

        Scene playerScene = new Scene(playerSceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(playerScene);
        window.show();

    }

}