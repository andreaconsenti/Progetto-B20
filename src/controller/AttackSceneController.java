package controller;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AttackSceneController {

	
	@FXML
    private Button attackButton;

    @FXML
    private Button annichilisciButton;

    @FXML
    private Button annullaButton;

    @FXML
    private MenuButton scegliNumeroArmate;
    
    @FXML
    private MenuItem one;
    
    @FXML
    private MenuItem two;
    
    @FXML
    private MenuItem three;
    
    @FXML
    private Label atkLabel;
    
    @FXML
    private Label defLabel;
    
    @FXML
    private Label atkN;
    
    @FXML
    private Label defN;
    
    private int[] atkResults;
    private int[] defResults;
    private int atkNumber;
    private int defNumber;
    
    private Integer temp;
    

    
    public void initialize() {
    	
    	attackButton.setDisable(true);
    	atkLabel.setText(GameSceneController.territory1.getName());
    	defLabel.setText(GameSceneController.territory2.getName());
    	temp = GameSceneController.territory1.getTanks();
    	atkN.setText(temp.toString());
    	temp = GameSceneController.territory2.getTanks();
    	defN.setText(temp.toString());
    	
    	atkResults = new int[3];
    	defResults = new int[3];
    	
    	one.setOnAction(e -> {
    		atkNumber = 1;
    		attackButton.setDisable(false);
    		scegliNumeroArmate.setText(one.getText());
		});
    	
    	two.setOnAction(e -> {
    		atkNumber = 2;
    		attackButton.setDisable(false);
    		scegliNumeroArmate.setText(two.getText());
		});
    	
    	three.setOnAction(e -> {
    		atkNumber = 3;
    		attackButton.setDisable(false);
    		scegliNumeroArmate.setText(three.getText());
		});
    	
    	menuHandler();
    
    }
    
    
    public void attackButtonPressed(ActionEvent e) throws IOException {
    	
    	atkResults = GameSceneController.territory1.getOwner().rollDices(atkNumber);
    	defNumber();
    	defResults = GameSceneController.territory2.getOwner().rollDices(defNumber);
    	
    	GameSceneController.game.battle(atkResults, defResults, atkNumber, defNumber);
    	updateGUI();
    	if(GameSceneController.territory2.getTanks() == 0) {
    		territoryConquered();
    	}
    	if(GameSceneController.territory1.getTanks() == 1) {
    		// Disabilitare le opzioni per l'attacco
    	}
    	
    	
    	
    }
    
    
    
    
    private void defNumber() {
    	if(GameSceneController.territory2.getTanks() > 2) {
    		defNumber = 3;
    	}
    	else
    		defNumber = GameSceneController.territory2.getTanks();
    }
    
    private void menuHandler() {
    	
    	int tanks = GameSceneController.territory1.getTanks() - 1;
    	if(tanks < 3) {
    		three.setDisable(true);
    		if(tanks < 2) {
    			two.setDisable(true);
    		}
    	}
    }
    
    private void updateGUI() {
    	temp = GameSceneController.territory1.getTanks();
    	atkN.setText(temp.toString());
    	temp = GameSceneController.territory2.getTanks();
    	defN.setText(temp.toString());
    }
    
    public void territoryConquered () throws IOException {
		Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/MoveScene.fxml"));
		Scene scene = new Scene(parent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Spostamento");
		window.setScene(scene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
	}

}
