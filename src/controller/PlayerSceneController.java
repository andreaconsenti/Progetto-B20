package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.PlayersList;
import model.entities.RisikoGame;

public class PlayerSceneController {
	

    @FXML
    private Button restoreButton;

    @FXML
    private Button startGameButton;

    @FXML
    private Button backButton;
    
    @FXML
    private MenuButton mapinput;
    
    @FXML
    private MenuItem map1;
    
    @FXML
    private MenuItem map2;

    @FXML
    private ListView<String> playerList;
    
    @FXML
    private TextField nameInputBlue;

    @FXML
    private RadioButton aiPlayerBlue;

    @FXML
    private Button addPlayerBtnBlue;

    @FXML
    private TextField nameInputPink;

    @FXML
    private RadioButton aiPlayerPink;

    @FXML
    private Button addPlayerBtnPink;

    @FXML
    private TextField nameInputYellow;

    @FXML
    private RadioButton aiPlayerYellow;

    @FXML
    private Button addPlayerBtnYellow;

    @FXML
    private TextField nameInputRed;

    @FXML
    private RadioButton aiPlayerRed;

    @FXML
    private Button addPlayerBtnRed;

    @FXML
    private TextField nameInputGreen;

    @FXML
    private RadioButton aiPlayerGreen;

    @FXML
    private Button addPlayerBtnGreen;

    @FXML
    private TextField nameInputBlack;

    @FXML
    private RadioButton aiPlayerBlack;

    @FXML
    private Button addPlayerBtnBlack;
    
    @FXML
    private ImageView mapPreview;

	private COLOR tempColor;	
	private ArrayList<Player> list;
	private boolean mapChosed;
	private boolean aiBlack;
	private boolean aiRed;
	private boolean aiYellow;
	private boolean aiGreen;
	private boolean aiBlue;
	private boolean aiPink;
	private boolean oneRealPlayer;
	
	
	public static String map;
	public static String territories;
	public static String terrFile;
	public static String continentsFile;
	public static String missions;
	public boolean isOnlineMultiplayer;
	
	/**
	 * Initializes the controller
	 */
	public void initialize() {
		startGameButton.setDisable(true);
		nameInputBlack.setText("");
		playerList.getItems().clear();
		list = new ArrayList<Player>();
		mapChosed = false;
		oneRealPlayer = false;
		
		map1.setOnAction(e -> {
			mapSelected("src/view/fxmls/images/Maps/RisikoClassic/map_preview.png", "src/view/fxmls/images/Maps/RisikoClassic/map.jpg", 
			"src/view/fxmls/images/Maps/RisikoClassic/territories.png", "assets/RisikoClassic/territori.txt", "assets/RisikoClassic/continenti.txt", "assets/RisikoClassic/obiettivi.txt");
		});
		
		map2.setOnAction(e -> {
			mapSelected("src/view/fxmls/images/Maps/SPQRisiko/map_preview.png", "src/view/fxmls/images/Maps/SPQRisiko/map.jpg", 
					"src/view/fxmls/images/Maps/SPQRisiko/territories.png", "assets/SPQRisiko/territori.txt", "assets/SPQRisiko/continenti.txt", "assets/SPQRisiko/obiettivi.txt");
		});
		
		aiPlayerRed.setOnAction(e -> {
			aiRed = true;
		});
		aiPlayerYellow.setOnAction(e -> {
			aiYellow = true;
		});
		aiPlayerBlack.setOnAction(e -> {
			aiBlack = true;
		});
		aiPlayerGreen.setOnAction(e -> {
			aiGreen = true;
		});
		aiPlayerBlue.setOnAction(e -> {
			aiBlue = true;
		});
		aiPlayerPink.setOnAction(e -> {
			aiPink = true;
		});
		
	}
	
	/**
	 * Loads the initial scene when the backButton is pressed
	 * @param event is the event
	 * @throws IOException
	 */
	public void backPressed(ActionEvent event) throws IOException {
		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/view.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(playerScene);
		window.show();
	}
	
	/**
	 * Adds the black player to the playerlist if it is possible
	 * @param event is the event
	 */
	public void addBtnBlackPressed(ActionEvent event) {
		if (nameInputBlack.getText().isBlank() == false) {
			if (nameNotExists(nameInputBlack.getText())) {
				list.add(new Player(nameInputBlack.getText(), COLOR.BLACK, aiBlack));
				playerList.getItems().add(nameInputBlack.getText() + " --> " + "BLACK");
				addPlayerBtnBlack.setDisable(true);
				if(!aiBlack) {
					oneRealPlayer = true;
				}
				startHandler();
			} else playerList.getItems().add(nameInputBlack.getText() + " nome gi� usato.");
		} else  playerList.getItems().add("Nome inesistente.");
	}
	
	/**
	 * Adds the blue player to the playerlist if it is possible
	 * @param event is the event
	 */
	public void addBtnBluePressed(ActionEvent event) {
		if (nameInputBlue.getText().isBlank() == false) {
			if (nameNotExists(nameInputBlue.getText())) {
				list.add(new Player(nameInputBlue.getText(), COLOR.BLUE, aiBlue));
				playerList.getItems().add(nameInputBlue.getText() + " --> " + "BLUE");
				addPlayerBtnBlue.setDisable(true);
				if(!aiBlue) {
					oneRealPlayer = true;
				}
				startHandler();
			} else playerList.getItems().add(nameInputBlue.getText() + " nome gi� usato.");
		} else playerList.getItems().add("Nome inesistente.");
	}
	
	/**
	 * Adds the green player to the playerlist if it is possible
	 * @param event is the event
	 */
	public void addBtnGreenPressed(ActionEvent event) {
		if (nameInputGreen.getText().isBlank() == false) {
	    	if (nameNotExists(nameInputGreen.getText())) {
	    		list.add(new Player(nameInputGreen.getText(), COLOR.GREEN, aiGreen));
	    		playerList.getItems().add(nameInputGreen.getText() + " --> " + "GREEN");
	    		addPlayerBtnGreen.setDisable(true);
				if(!aiGreen) {
					oneRealPlayer = true;
				}
				startHandler();
	    	} else playerList.getItems().add(nameInputGreen.getText() + " nome gi� usato.");
		} else playerList.getItems().add("Nome inesistente.");
	}

	/**
	 * Adds the pink player to the playerlist if it is possible
	 * @param event is the event
	 */
	public void addBtnPinkPressed(ActionEvent event) {
		if (nameInputPink.getText().isBlank() == false) {
	    	if (nameNotExists(nameInputPink.getText())) {
	    		list.add(new Player(nameInputPink.getText(), COLOR.PINK, aiPink));
	    		playerList.getItems().add(nameInputPink.getText() + " --> " + "PINK");
	    		addPlayerBtnPink.setDisable(true);
				if(!aiPink) {
					oneRealPlayer = true;
				}
				startHandler();
	    	} else playerList.getItems().add(nameInputPink.getText() + " nome gi� usato.");	
		} else playerList.getItems().add("Nome inesistente.");
	}
	
	/**
	 * Adds the red player to the playerlist if it is possible
	 * @param event is the event
	 */
	public void addBtnRedPressed(ActionEvent event) {
		if (nameInputRed.getText().isBlank() == false) {
			if (nameNotExists(nameInputRed.getText())) {
				list.add(new Player(nameInputRed.getText(), COLOR.RED, aiRed));
				playerList.getItems().add(nameInputRed.getText() + " --> " + "RED");
				addPlayerBtnRed.setDisable(true);
				if(!aiRed) {
					oneRealPlayer = true;
				}
				startHandler();
			} else playerList.getItems().add(nameInputRed.getText() + " nome gi� usato.");
		} else playerList.getItems().add("Nome inesistente.");
	}
	
	/**
	 * Adds the yellow player to the playerlist if it is possible
	 * @param event is the event
	 */
	public void addBtnYellowPressed(ActionEvent event) {
		if (nameInputYellow.getText().isBlank() == false) {
			if (nameNotExists(nameInputYellow.getText())) {
				list.add(new Player(nameInputYellow.getText(), COLOR.YELLOW, aiYellow));
				playerList.getItems().add(nameInputYellow.getText() + " --> " + "YELLOW");
				addPlayerBtnYellow.setDisable(true);
				if(!aiYellow) {
					oneRealPlayer = true;
				}
				startHandler();
			} else playerList.getItems().add(nameInputYellow.getText() + " nome gi� usato.");
		} else playerList.getItems().add("Nome inesistente.");
	}
	
	/**
	 * Restores everything to the initial value and eliminates the playerlist when the restoreButton is pressed
	 */
	public void restorePressed() {
		initialize();
		nameInputBlue.setText("");
		nameInputGreen.setText("");
		nameInputBlack.setText("");
		nameInputRed.setText("");
		nameInputPink.setText("");
		nameInputYellow.setText("");
		addPlayerBtnBlack.setDisable(false);
		addPlayerBtnBlue.setDisable(false);
		addPlayerBtnPink.setDisable(false);
		addPlayerBtnRed.setDisable(false);
		addPlayerBtnYellow.setDisable(false);
		addPlayerBtnGreen.setDisable(false);
	}
	
	/**
	 * Starts the game and loads the game scene when the startGameButton is pressed 	
	 * @param event is the event
	 * @throws IOException
	 */
	public void startGamePressed(ActionEvent event) throws IOException {
		isOnlineMultiplayer = false;
		PlayersList.setPlayers(list);
		Parent playerSceneParent= FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/GameScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(playerScene);
		window.show();
	}
	
	/**
	 * Checks if a name is a valid name for a player
	 * @param name is the text to check
	 * @return boolean
	 */
	private boolean nameNotExists(String name) {
		for(Player p : list) {
			if(p.getName().equals(name)) {
				return false;
			}
		} 
		return true;
	}
	
	private void startHandler() {
		if (list.size() > 2 && mapChosed && oneRealPlayer) {
			startGameButton.setDisable(false);
		}
	}
	
	private void mapSelected(String image, String mapImage, String terrImage, String terrFiles, String contsFile, String missFile) {
		mapinput.setText(map1.getText());
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
		startHandler();
	}
}