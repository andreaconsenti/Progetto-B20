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
	private ImageView start;
	@FXML
	private ImageView rules;
	@FXML
	private Button startButton;


	/**
	 * Manages the pressure of the Start button, opening the playerScene
	 * @param event is the event generated
	 * @throws IOException
	 */
	public void startPressed(ActionEvent event) throws IOException {
		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/NewPlayerScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(playerScene);
		window.show();
	}


	/**
	 * Manages the pressure of the Rules button, opening the rules
	 * @param event is the event generated
	 * @throws IOException
	 */

	public void rulesPressed(ActionEvent event) throws IOException {
		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/RulesScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(playerScene);
		window.show();
	}

	/**
	 * Manages the pressure of the Online button, opening the onlineScene
	 * @param event is the event generated
	 * @throws IOException
	 */

	public void onlinePressed(ActionEvent event) throws IOException {

		Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/OnlineScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(playerScene);
		window.show();
	}

	public void rulesPressedImage() {

	}


}
