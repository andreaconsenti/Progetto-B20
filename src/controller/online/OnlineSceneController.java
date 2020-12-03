package controller.online;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import controller.RemoteJoin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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


    private ArrayList<Player> list;
    public static Registry registry;
    public static RemoteJoin stub;

    private boolean mapChosed;
    public static String map;
    public static String territories;
    public static String terrFile;
    public static String continentsFile;
    public static String missions;

    public static String myColor;
    public static boolean amIaServer;

    public static boolean isOnlineMultiplayer;

    public static ActionEvent event = new ActionEvent();


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

    public void serverPressed(ActionEvent event) {
        try {
            amIaServer = true;
            RemoteJoin stub = (RemoteJoin) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1888);
            System.setProperty("java.rmi.server.hostname", "192.168.1.107");
            registry.rebind("Hello", stub);
            System.out.println("Server ready.");
            serverStatus("ok");
            list = new ArrayList<Player>();

        } catch (Exception e) {
            serverStatus("err");
            e.printStackTrace();
        }

    }

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

    public void partecipaPressed(ActionEvent event) {
        try {
            amIaServer = false;
            registry = LocateRegistry.getRegistry("192.168.1.107", 1888);
            stub = (RemoteJoin) registry.lookup("Hello");
            String nameFieldValue = nameField.getText();
            String response = stub.joinRequest(nameFieldValue);

            //Salvo nella variabile statica myColor il colore
            //che il client avrà per tutta la partita
            myColor = response;

            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void mapChooseEnabler() {

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

    public void chiudiPressed(ActionEvent event) {
        /*ToDo: pulire il codice*/
        partecipaButton.setDisable(true);
        nameField.setEditable(false);

        defVisualPlayerList.setVisible(true);
        ArrayList<String> lista = new ArrayList<>(playerList.getItems());
        defVisualPlayerList.getItems().setAll(list);

        mapChooseEnabler();
    }

    public COLOR autoColorChooser() {
        /*Fare in modo di evitare lo switch e prendere
        direttamente i valori dalla sua enum
         */
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

    public void startGamePressed(ActionEvent event) throws IOException {
        // Adattare il controller del GameScene.fxml per poter caricare
        // il nuovo scenario!

        isOnlineMultiplayer = true;

        PlayersList.setPlayers(list);
        Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/GameScene.fxml"));

        Scene playerScene = new Scene(playerSceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(playerScene);
        window.show();


    }

    private void mapSelected(String image, String mapImage, String terrImage, String terrFiles, String contsFile, String missFile) {

        if (mapImage.equals("src/view/fxmls/images/Maps/RisikoClassic/map.jpg")) {
            mapinput.setText(map1.getText());
        }
        if (mapImage.equals("src/view/fxmls/images/Maps/SPQRisiko/map.jpg")) {
            mapinput.setText(map2.getText());
        }

        //mapinput.setText(map1.getText());
        mapinput.setStyle("-fx-text-fill: black;");
        File file = new File(image);
        Image temp = new Image(file.toURI().toString());
        mapPreview.setImage(temp);
        map = mapImage;
        territories = terrImage;
        terrFile = terrFiles;
        continentsFile = contsFile;
        missions = missFile;
        mapChosed = true;
    }

    //****************************//
    // Metodi relativi a Java RMI //
    //****************************//

    @Override
    public String sayHello() throws RemoteException {
        return "hello funziona";
    }

    @Override
    public String joinRequest(String clientInput) throws RemoteException {
        //System.out.println("Richiesta da client ricevuta");
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

    @Override
    public ArrayList<Player> getList() throws RemoteException {
        //ArrayList<Player> listaCopiata = new ArrayList<>(list);
        return list;
        //return listaCopiata;
    }

    @Override
    public String getMap() throws RemoteException {
        return map;
    }

    @Override
    public String getTerritories() throws RemoteException {
        return territories;
    }

    @Override
    public String getTerrFile() throws RemoteException {
        return terrFile;
    }

    @Override
    public String getContinentFile() throws RemoteException {
        return continentsFile;
    }

    @Override
    public String getMissions() throws RemoteException {
        return missions;
    }

    public void remotePlayerSetter() throws RemoteException {
    }

    public void startGamePressed2(ActionEvent event) throws IOException {
        // Adattare il controller del GameScene.fxml per poter caricare
        // il nuovo scenario!

        list = stub.getList();
        map = stub.getMap();
        territories = stub.getTerritories();
        terrFile = stub.getTerrFile();
        continentsFile = stub.getContinentFile();
        missions = stub.getMissions();

        isOnlineMultiplayer = true;

        PlayersList.setPlayers(list);
        Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/OnlineGameScene.fxml"));

        Scene playerScene = new Scene(playerSceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(playerScene);
        window.show();

    }

}