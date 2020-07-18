package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MissionCompletedController {

    @FXML
    private TextArea winnerPlayerText;

	public void initialize(){
		
		winnerPlayerText.setText(GameSceneController.game.getCurrentTurn().getName());
//		GameSceneController.game.                   per terminare partita
		
	}
    
	
	public void esciPressed(ActionEvent e) {
		System.exit(0);
	}
	
	public void nuovaPressed(ActionEvent e) throws IOException {
		GameSceneController.getInstance().newGame();
		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
	}
	
	
}
