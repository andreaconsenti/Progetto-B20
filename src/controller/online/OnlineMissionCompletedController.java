package controller.online;

import controller.online.OnlineGameSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class OnlineMissionCompletedController {

    @FXML
    private TextArea winnerPlayerText;

	public void initialize(){
		
		winnerPlayerText.setText(OnlineGameSceneController.game.getCurrentTurn().getName());
//		GameSceneController.game.                   per terminare partita
		
	}
    

	/**
	 * Manages the pressure of the Esci button, closing the game
	 * @param event is the event generated
	 * @throws IOException
	 */
	public void esciPressed(ActionEvent e) {
		System.exit(0);
	}
	

	/**
	 * Manages the pressure of the Nuova button, starting a new game
	 * @param event is the event generated
	 * @throws IOException
	 */
	public void nuovaPressed(ActionEvent e) throws IOException {
		OnlineGameSceneController.getInstance().newGame();
		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
	}
	
	
}
