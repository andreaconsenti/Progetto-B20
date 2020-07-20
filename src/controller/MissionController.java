package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MissionController {

	@FXML
	TextArea missionText;
	
	/**
	 * Initializes the MissionController
	 */
	public void initialize(){
		
		missionText.setText(GameSceneController.game.getCurrentTurn().getMissionDescription());
		
	}
	
}
