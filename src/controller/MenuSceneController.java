package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MenuSceneController {
	
	
	public void continuaPressed(ActionEvent e) {
		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
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
