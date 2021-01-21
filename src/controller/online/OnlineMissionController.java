package controller.online;

import controller.online.OnlineGameSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class OnlineMissionController {

	@FXML
	TextArea missionText;
	
	/**
	 * Initializes the MissionController
	 */
	public void initialize(){
		
		missionText.setText(OnlineGameSceneController.game.getCurrentTurn().getMissionDescription());
		
	}
	
}
