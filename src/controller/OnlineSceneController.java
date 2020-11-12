package controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OnlineSceneController implements Hello{
    @FXML
    private Button backButton;
    @FXML
    private TextField nameField;
    @FXML
    private Button serverButton;
    @FXML
    private Button partecipaButton;
    @FXML
    private ListView<String> playerList;
    @FXML
    private TextField serverStatusField;
    @FXML
    private Button avviaButton;
    @FXML
    private Label labelInit;

    /**
     * Manages the pressure of the Indietro button, exiting the rules
     * @param event is the event generated
     * @throws IOException
     */
    public void backPressed(ActionEvent event) throws IOException {
        Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/view.fxml"));
        Scene playerScene = new Scene(playerSceneParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

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
            Hello stub = (Hello) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1888);
            System.setProperty("java.rmi.server.hostname","192.168.1.107");
            registry.rebind("Hello", stub);
            System.out.println("Server ready.");
            serverStatus("ok");

        } catch (Exception e) {
            serverStatus("err");
            e.printStackTrace();
        }
    }

    public void serverStatus(String activationResponse) {
        if(activationResponse.equals("ok")) {
            serverStatusField.setText("Attivo");
            serverStatusField.setStyle("-fx-text-fill: green");
            serverButton.setDisable(true);
        }
        if(activationResponse.equals("err")) {
            serverStatusField.setText("Errore");
            serverStatusField.setStyle("-fx-text-fill: red");
        }

    }

    public void partecipaPressed(ActionEvent event) {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.1.107",1888);
            Hello stub = (Hello) registry.lookup("Hello");
            String nameFieldValue = nameField.getText();
            String response = stub.joinRequest(nameFieldValue);
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void avviaPressed(ActionEvent event) {
        labelInit.setVisible(true);
        ArrayList<String> lista = new ArrayList<>(playerList.getItems());
        labelInit.setText(labelInit.getText() + " " + lista.toString());
    }

    @Override
    public String sayHello() throws RemoteException {
        return "hello funziona";
    }

    @Override
    public String joinRequest(String clientInput) throws RemoteException {
        System.out.println("Richiesta da client ricevuta");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                playerList.getItems().add(clientInput);
            }
        });

        return "Server ha ricevuto la tua richiesta";
    }
}