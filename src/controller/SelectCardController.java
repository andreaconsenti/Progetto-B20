package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.entities.RisikoGame.GAME_PHASE;

public class SelectCardController {

    @FXML
    private ImageView cardImage0;

    @FXML
    private Label territoryLabel0;

    @FXML
    private ImageView cardImage01;

    @FXML
    private Label territoryLabel1;

    @FXML
    private Pane paneScambioCarte;

    @FXML
    private ImageView carta3;

    @FXML
    private Button scambiaButton;

    @FXML
    private Button annullaButton;

    @FXML
    private ImageView carta1;

    @FXML
    private ImageView carta2;

    @FXML
    void initialize() {
    	paneScambioCarte.setOpacity(0.35);
    	scambiaButton.setDisable(true);
    	annullaButton.setDisable(true);
    	if(GameSceneController.game.getGamePhase()==GAME_PHASE.REINFORCEMENT) {
    		paneScambioCarte.setOpacity(1);
    		scambiaButton.setDisable(false);
    		annullaButton.setDisable(false);
    	}
    }
    
	void addCard() {
		//aggiungere stackPane
		//la prima è cardImage0 e territoryName0, la seconda è cardImage01 e territoryName01, poi 02 e 02, etc...
		
	}
	
	@FXML
	void onAnnullaPressed(ActionEvent event) throws IOException {
		

	}

	@FXML
	void onScambiaPressed(ActionEvent event) throws IOException {
	
	}
}