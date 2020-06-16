package controller;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.entities.RisikoGame;
import model.entities.RisikoGame.GAME_PHASE;
import model.entities.Territory;
import model.util.Pixel;

public class SelectCardController {
	
	@FXML
	private ImageView carta1;

	@FXML
	private ImageView carta2;
	
	@FXML
	private ImageView carta3;

	@FXML
	private Button scambiaButton;

	@FXML
	private Button annullaButton;
	
	@FXML
	private ImageView cardImage0;

	@FXML
	private Label territoryName0;

	@FXML
	private ImageView cardImage01;

	@FXML
	private Label territoryName01;

	@FXML
    private ImageView cardImage02;

    @FXML
    private Label territoryName02;

    @FXML
    private ImageView cardImage021;

    @FXML
    private Label territoryName021;

    @FXML
    private ImageView cardImage022;

    @FXML
    private Label territoryName022;

    @FXML
    private ImageView cardImage023;

    @FXML
    private Label territoryName023;

    @FXML
    private ImageView cardImage024;

    @FXML
    private Label territoryName024;

    @FXML
    private ImageView cardImage025;

    @FXML
    private Label territoryName025;
    
    @FXML
    private Pane paneScambioCarte;
    
//    private RisikoGame game;


    @FXML
    void initialize() {
    	paneScambioCarte.setOpacity(0.35);
    	scambiaButton.setDisable(true);
    	annullaButton.setDisable(true);
    	if(GameSceneController.game.getGamePhase()==GAME_PHASE.REINFORCEMENT) {
    		paneScambioCarte.setOpacity(1);
    	}
    }
    
//	void addCard() {
//		//aggiungere stackPane
//		//la prima è cardImage0 e territoryName0, la seconda è cardImage01 e territoryName01, poi 02 e 02, etc...
//		
//	}
	
	
	
	@FXML
	void onAnnullaPressed(ActionEvent event) throws IOException {
		
	}


	@FXML
	void onScambiaPressed(ActionEvent event) throws IOException {
	
	}
}