package controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MenuSceneController {
	
	
	public void continuaPressed(ActionEvent e) {
		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
	}
	
	public void esciPressed(ActionEvent e) {
		
	}
	
	public void salvaPressed(ActionEvent e) {
		
	}
	
	public void nuovaPressed(ActionEvent e) {
		
	}
	
	
}
