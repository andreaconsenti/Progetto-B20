package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MissionCompletedController {

    @FXML
    private TextArea winnerPlayerText;

	public void initialize(){
		
		winnerPlayerText.setText(GameSceneController.game.getCurrentTurn().getName());
//		GameSceneController.game.                   per terminare partita
		
	}
    
}
