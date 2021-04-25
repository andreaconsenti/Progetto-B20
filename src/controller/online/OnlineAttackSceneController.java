package controller.online;

import controller.GameSceneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.entities.online.Attacco;

import java.io.File;
import java.io.IOException;

public class OnlineAttackSceneController {

	
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
    
    
    
    /**
     * Initializes the controller
     */
    public void initialize() {
    	
    	attackButton.setDisable(true);
    	atkLabel.setText(OnlineGameSceneController.territory1.getName());
    	defLabel.setText(OnlineGameSceneController.territory2.getName());
    	temp = OnlineGameSceneController.territory1.getTanks();
    	atkN.setText(temp.toString());
    	temp = OnlineGameSceneController.territory2.getTanks();
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
    
    /**
     * Manages the attack when the attack button is pressed
     * @param e is the event
     * @throws IOException
     */
    public void attackButtonPressed(ActionEvent e) throws IOException {


		OnlineGameSceneController.nextClient = false;

		System.out.println("ATK:" + OnlineGameSceneController.territory1);
		System.out.println("DEF:" + OnlineGameSceneController.territory2);
		OnlineGameSceneController.serverAtkTerritory = OnlineGameSceneController.territory1;
		OnlineGameSceneController.serverDefTerritory = OnlineGameSceneController.territory2;

		atkResults = OnlineGameSceneController.territory1.getOwner().rollDices(atkNumber);
    	defResults = OnlineGameSceneController.territory2.getOwner().rollDices(defNumber);
    	
    	OnlineGameSceneController.game.battle(atkResults, defResults, atkNumber, defNumber);


    	if(OnlineSceneController.amIaServer /*&& !OnlineSceneController.amIaClient*/) {

			OnlineGameSceneController.serverAtkNewTankNum = OnlineGameSceneController.territory1.getTanks();
			OnlineGameSceneController.serverDefNewTankNum = OnlineGameSceneController.territory2.getTanks();

		}

		Attacco attaccoTemp = new Attacco
				(OnlineGameSceneController.territory1, OnlineGameSceneController.territory2,
						OnlineGameSceneController.game.getTerritory(OnlineGameSceneController.territory1).getTanks(),
						OnlineGameSceneController.game.getTerritory(OnlineGameSceneController.territory2).getTanks());

		System.out.println(attaccoTemp.toString());

		OnlineGameSceneController.myAttacks.add(attaccoTemp);


    	updateGUI();
    	menuHandler();
    	attackButton.setDisable(true);
    	defNumber();

    	
    	if(OnlineGameSceneController.territory2.getTanks() == 0) {
    		territoryConquered();
    		annichilisciButton.setDisable(true);
    		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
    		window.close();
    	}
    	
    	if(OnlineGameSceneController.territory1.getTanks() < 2) {
    		attackButton.setDisable(true);
    		scegliNumeroArmate.setDisable(true);
    	}



    	//mod****
		//OnlineGameSceneController.serverAttackClosed = true;
		System.out.println("serverattack TRUE");
    	//*******
    }
    
    /**
     * Manages the attack when the annulla button is pressed
     * @param e is the event
     * @throws IOException
     */
    public void annullaButtonPressed(ActionEvent e) throws IOException {
    	Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
    }
    
    /**
     * Manages the attack when the assedia button is pressed
     * @param e is the event
     * @throws IOException
     */
    public void assediaPressed(ActionEvent e) throws IOException {
    	int n = 0;
    	while(n == 0) {
    		atkResults = OnlineGameSceneController.territory1.getOwner().rollDices(atkNumber);
        	defResults = OnlineGameSceneController.territory2.getOwner().rollDices(defNumber);

			OnlineGameSceneController.game.battle(atkResults, defResults, atkNumber, defNumber);
        	updateGUI();
        	menuHandler();
        	attackButton.setDisable(true);
        	defNumber();
        	atkNumber = OnlineGameSceneController.territory1.getTanks() - 1;
        	
        	if(OnlineGameSceneController.territory2.getTanks() == 0) {
        		territoryConquered();
        		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        		window.close();
        		break;
        	}
        	
        	if(OnlineGameSceneController.territory1.getTanks() < 2) {
        		attackButton.setDisable(true);
        		annichilisciButton.setDisable(true);
        		scegliNumeroArmate.setDisable(true);
        		break;
        	}
    	}
    	
    }
    
    
    
    /**
     * sets the number of defending tanks
     */
    private void defNumber() {
    	if(OnlineGameSceneController.territory2.getTanks() > 2) {
    		defNumber = 3;
    	}
    	else
    		defNumber = OnlineGameSceneController.territory2.getTanks();
    }

    /**
     * Manages the menu
     */
    private void menuHandler() {
    	
    	int tanks = OnlineGameSceneController.territory1.getTanks() - 1;
    	if(tanks < 3) {
    		three.setDisable(true);
    		if(tanks < 2) {
    			two.setDisable(true);
    		}
    	}
    }
    
    /**
     * Updates the GameSceneController GUI after the attack ends
     */
    private void updateGUI() {
    	temp = OnlineGameSceneController.territory1.getTanks();
    	atkN.setText(temp.toString());
    	temp = OnlineGameSceneController.territory2.getTanks();
    	defN.setText(temp.toString());
    	setDiceImage();
    	
    	for(int i = 0; i < 3; i++) {
    		setDiceOpacity(i);
    	}
    	removeUnusedDice();
    }
    
    /**
     * Manages the conquer of a territory
     * @throws IOException
     */
    private void territoryConquered () throws IOException {
		OnlineGameSceneController.game.conquer(OnlineGameSceneController.territory1, OnlineGameSceneController.territory2);
    	
		Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/OnlineMoveScene.fxml"));
		Scene scene = new Scene(parent);
		Stage window = new Stage();
		window.initStyle(StageStyle.UNDECORATED);
		window.setResizable(false);
		window.setTitle("Spostamento");
		window.setScene(scene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// consume event
				event.consume();
			}
		});
	}
    
    /**
     * Sets the dice images
     */
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
    
    /**
     * Removes Dice
     */
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
    
    /**
     * gets an image
     * @param path is the path of the image
     * @return image
     */
    private Image getImage(String path) {
    	File file = new File(path);
    	Image image = new Image(file.toURI().toString());
    	return image;
    }
    
    /**
     * Sets the opacity level 
     * @param i is the value
     */
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
    
    /**
     * Manages an attack made by an AI player
     */
    public static void aiAttack() {
    	
    	int atNumber;
    	int deNumber;
    	
    	atNumber = OnlineGameSceneController.territory1.getTanks() - 1;
    	if(OnlineGameSceneController.territory2.getTanks() > 2) {
    		deNumber = 3;
    	} else {
    		deNumber = OnlineGameSceneController.territory2.getTanks();
    	}

		OnlineGameSceneController.getInstance().getGame().battle(OnlineGameSceneController.territory1.getOwner().rollDices(atNumber), OnlineGameSceneController.territory2.getOwner().rollDices(deNumber), atNumber, deNumber);
    	
    	
    	
    }
    

}
