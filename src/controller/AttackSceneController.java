package controller;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
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
import javafx.scene.image.ImageView;
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
    @FXML 
    private ImageView aDie1;    
    @FXML 
    private ImageView aDie2;  
    @FXML 
    private ImageView aDie3;
    @FXML 
    private ImageView dDie1;
    @FXML 
    private ImageView dDie2;
    @FXML 
    private ImageView dDie3;
    
    private int[] atkResults;
    private int[] defResults;
    private int atkNumber;
    private int defNumber;
    
    private Integer temp;
    
    
    private class Roller extends AnimationTimer{
    	
    	
    	private long fps = 50L;
    	private long interval = 1000000000L / fps;
    	private int maxRolls = 20;
    	
    	private long last = 0;
    	private int count = 0;
    	
    	@Override
    	public void handle(long now) {
    		if((now - last) > interval) {
    			GameSceneController.territory1.getOwner().rollDices(atkNumber);
    			GameSceneController.territory2.getOwner().rollDices(defNumber);
    	    	setDiceImage();
    	    	last = now;
    	    	count++;
    	    	if(count > maxRolls) {
    	    		clock.stop();
    	    		atkResults = GameSceneController.territory1.getOwner().rollDices(atkNumber);
        	    	defResults = GameSceneController.territory2.getOwner().rollDices(defNumber);
    	    		count = 0;
    	    	}
    		}
    	}
    }
    
    private Roller clock;
    
    
    
    public void initialize() {
    	
    	clock = new Roller();
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
    	
    	defNumber();
    	
    	menuHandler();
    
    }
    
    
    public void attackButtonPressed(ActionEvent e) throws IOException {
//    	rollAnimation();
		atkResults = GameSceneController.territory1.getOwner().rollDices(atkNumber);
    	defResults = GameSceneController.territory2.getOwner().rollDices(defNumber);
    	
    	GameSceneController.game.battle(atkResults, defResults, atkNumber, defNumber);
    	updateGUI();
    	menuHandler();
    	attackButton.setDisable(true);
    	attackingNumbers();

    	
    	if(GameSceneController.territory2.getTanks() == 0) {
    		territoryConquered();
    		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
    		window.close();
    	}
    	
    	if(GameSceneController.territory1.getTanks() < 2) {
    		attackButton.setDisable(true);
    		scegliNumeroArmate.setDisable(true);
    	}
    	
    }
    
    
    
    
    private void defNumber() {
    	if(GameSceneController.territory2.getTanks() > 2) {
    		defNumber = 3;
    	}
    	else
    		defNumber = GameSceneController.territory2.getTanks();
    }
    
    private void attackingNumbers() {
    	defNumber();
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
    	setDiceImage();
    	
    	for(int i = 0; i < 3; i++) {
    		setDiceOpacity(i);
    	}
    	removeUnusedDice();
    }
    
    private void territoryConquered () throws IOException {
		Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/MoveScene.fxml"));
		Scene scene = new Scene(parent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Spostamento");
		window.setScene(scene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
	}
    
    private void setDiceImage() {
    	
    	for(int i = 0; i < atkNumber; i++) {
    		switch(i) {
    		case 0:
    			aDie1.setImage(getImage("src/view/fxmls/images/Dadi/redDie" + atkResults[i] + ".png"));
    			break;
    		case 1:
    			aDie2.setImage(getImage("src/view/fxmls/images/Dadi/redDie" + atkResults[i] + ".png"));
    			break;
    		case 2:
    			aDie3.setImage(getImage("src/view/fxmls/images/Dadi/redDie" + atkResults[i] + ".png"));
    			break;
    		}
    	}
    	
    	for(int i = 0; i < defNumber; i++) {
    		switch(i) {
    		case 0:
    			dDie1.setImage(getImage("src/view/fxmls/images/Dadi/bluDie" + defResults[i] + ".png"));
    			break;
    		case 1:
    			dDie2.setImage(getImage("src/view/fxmls/images/Dadi/bluDie" + defResults[i] + ".png"));
    			break;
    		case 2:
    			dDie3.setImage(getImage("src/view/fxmls/images/Dadi/bluDie" + defResults[i] + ".png"));
    			break;
    		}
    	}
    	
    }
    
    private void removeUnusedDice() {
    	if(atkNumber < 3) {
    		if(atkNumber < 2) {
    			aDie2.setOpacity(0.0);
    		}
    		aDie3.setOpacity(0.0);
    	}
    	
    	if(defNumber < 3) {
    		if(defNumber < 2) {
    			dDie2.setOpacity(0.0);
    		}
    		dDie3.setOpacity(0.0);
    	}
    	
    }
    
    private void rollAnimation() {
    	clock.start();
    }
    
    private Image getImage(String path) {
    	File file = new File(path);
    	Image image = new Image(file.toURI().toString());
    	return image;
    }
    
    private void setDiceOpacity(int i) {
    	
    	switch(i) {
    	case 0:
        	if(atkResults[i] > defResults[i]) {
        		aDie1.setOpacity(1);
        		dDie1.setOpacity(0.35);
        	} else {
        		aDie1.setOpacity(0.35);
        		dDie1.setOpacity(1);
        	}
        	break;
    	case 1:
        	if(atkResults[i] > defResults[i]) {
        		aDie2.setOpacity(1);
        		dDie2.setOpacity(0.35);
        	} else {
        		aDie2.setOpacity(0.35);
        		dDie2.setOpacity(1);
        	}
        	break;
    	case 2:
        	if(atkResults[i] > defResults[i]) {
        		aDie3.setOpacity(1);
        		dDie3.setOpacity(0.35);
        	} else {
        		aDie3.setOpacity(0.35);
        		dDie3.setOpacity(1);
        	}
        	break;
    	}
    }
    

}
