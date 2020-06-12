package controller;

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


	private COLOR tempColor;	private ArrayList<Player> list;
	
	
	
	public void initialize() {
		startGameButton.setDisable(true);
		aiPlayerBlack.setDisable(true);
		aiPlayerBlue.setDisable(true);
		aiPlayerPink.setDisable(true);
		aiPlayerGreen.setDisable(true);
		aiPlayerRed.setDisable(true);
		aiPlayerYellow.setDisable(true);
		nameInputBlack.setText("");
		playerList.getItems().clear();
		list = new ArrayList<Player>();
		
	}
	
	
	public void backPressed(ActionEvent event) throws IOException {
		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/view.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(playerScene);
		window.show();
	}
	
	@FXML
	void addBtnBlackPressed(ActionEvent event) {
		if (nameInputBlack.getText().isBlank() == false) {
			if (nameNotExists(nameInputBlack.getText())) {
				list.add(new Player(nameInputBlack.getText(), COLOR.BLACK));
				playerList.getItems().add(nameInputBlack.getText() + " --> " + "BLACK");
				addPlayerBtnBlack.setDisable(true);
				if (list.size() > 2) {
					startGameButton.setDisable(false);
				}
			} else playerList.getItems().add(nameInputBlack.getText() + " nome già usato.");
		} else  playerList.getItems().add("nome inesistente.");
	}
	
	@FXML
	void addBtnBluePressed(ActionEvent event) {
		if (nameInputBlue.getText().isBlank() == false) {
			if (nameNotExists(nameInputBlue.getText())) {
				list.add(new Player(nameInputBlue.getText(), COLOR.BLUE));
				playerList.getItems().add(nameInputBlue.getText() + " --> " + "BLUE");
				addPlayerBtnBlue.setDisable(true);
				if (list.size() > 2) {
					startGameButton.setDisable(false);
				}
			} else playerList.getItems().add(nameInputBlue.getText() + " nome già usato.");
		} else playerList.getItems().add("nome inesistente.");
	}
	
	@FXML
	void addBtnGreenPressed(ActionEvent event) {
		if (nameInputGreen.getText().isBlank() == false) {
	    	if (nameNotExists(nameInputGreen.getText())) {
	    		list.add(new Player(nameInputGreen.getText(), COLOR.GREEN));
	    		playerList.getItems().add(nameInputGreen.getText() + " --> " + "GREEN");
	    		addPlayerBtnGreen.setDisable(true);
	    		if (list.size() > 2) {
	    			startGameButton.setDisable(false);
	    		}
	    	} else playerList.getItems().add(nameInputGreen.getText() + " nome già usato.");
		} else playerList.getItems().add("nome inesistente.");
	}

	@FXML
	void addBtnPinkPressed(ActionEvent event) {
		if (nameInputPink.getText().isBlank() == false) {
	    	if (nameNotExists(nameInputPink.getText())) {
	    		list.add(new Player(nameInputPink.getText(), COLOR.PINK));
	    		playerList.getItems().add(nameInputPink.getText() + " --> " + "PINK");
	    		addPlayerBtnPink.setDisable(true);
	    		if (list.size() > 2) {
	    			startGameButton.setDisable(false);
	    		}
	    	} else playerList.getItems().add(nameInputPink.getText() + " nome già usato.");	
		} else playerList.getItems().add("nome inesistente.");
	}
	
	@FXML
	void addBtnRedPressed(ActionEvent event) {
		if (nameInputRed.getText().isBlank() == false) {
			if (nameNotExists(nameInputRed.getText())) {
				list.add(new Player(nameInputRed.getText(), COLOR.RED));
				playerList.getItems().add(nameInputRed.getText() + " --> " + "RED");
				addPlayerBtnRed.setDisable(true);
				if (list.size() > 2) {
					startGameButton.setDisable(false);
				}
			} else playerList.getItems().add(nameInputRed.getText() + " nome già usato.");
		} else playerList.getItems().add("nome inesistente.");
	}
	
	@FXML
	void addBtnYellowPressed(ActionEvent event) {
		if (nameInputYellow.getText().isBlank() == false) {
			if (nameNotExists(nameInputYellow.getText())) {
				list.add(new Player(nameInputYellow.getText(), COLOR.YELLOW));
				playerList.getItems().add(nameInputYellow.getText() + " --> " + "YELLOW");
				addPlayerBtnYellow.setDisable(true);
				if (list.size() > 2) {
					startGameButton.setDisable(false);
				}
			} else playerList.getItems().add(nameInputYellow.getText() + " nome già usato.");
		} else playerList.getItems().add("nome inesistente.");
	}

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
	
	/*@FXML
	public String itemClicked(MouseEvent event) {
		String giocatore =playerList.getSelectionModel().getSelectedItem();
		cancelButton.setDisable(false);
		return giocatore;
	}
	
	@FXML
	public  void cancelPressed(ActionEvent event) {
		
		playerList.getItems().remove(playerList.getSelectionModel().getSelectedItem()); //elimina giocatore dalla lista
		cancelButton.setDisable(true);
	}*/
	
	
	
	
	public void startGamePressed(ActionEvent event) throws IOException {
		PlayersList.setPlayers(list);
		
		Parent playerSceneParent= FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/GameScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		
	//  Parent playerSceneParentProva = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/GameSceneProva.fxml"));
	//  Scene playerSceneProva = new Scene(playerSceneParentProva);
		
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(playerScene);
	//	window.setScene(playerSceneProva);
		window.show();
	}
	
	
		
	private boolean nameNotExists(String name) {
		for(Player p : list) {
			if(p.getName().equals(name)) {
				return false;
			}
		} 
		return true;
	}

}
