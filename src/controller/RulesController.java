package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RulesController {
	
	/**
	 * Manages the pressure of the Indietro button, exiting the rules
	 * @param event is the event generated
	 * @throws IOException
	 */
	public void backPressed(ActionEvent event) throws IOException {
		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/view.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(playerScene);
		window.show();
	}
	
}
