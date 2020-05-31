package controller;



import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Controller {
	@FXML
	ImageView start;
	@FXML
	ImageView rules;
	
	@FXML
	Button startButton;
	
	
	public void startPressed(ActionEvent event) throws IOException {
		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/NewPlayerScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(playerScene);
		window.show();
	}
	
	public void rulesPressed() {
		
	}
	
	public void rulesPressedImage() {
		
	}
	

}
