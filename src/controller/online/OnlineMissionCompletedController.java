package controller.online;

import controller.online.OnlineGameSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class OnlineMissionCompletedController {

    @FXML
    private TextArea winnerPlayerText;

	public void initialize(){

		try {
			OnlineGameSceneController.getInstance().closeGameRequest();
		} catch (RemoteException ex) {ex.printStackTrace();}

		winnerPlayerText.setText(OnlineGameSceneController.game.getCurrentTurn().getName());
		
	}
    

	/**
	 * Manages the pressure of the Esci button, closing the game
	 * @param event is the event generated
	 * @throws IOException
	 */
	public void esciPressed(ActionEvent e) {
		System.exit(0);
	}
	
	
}
