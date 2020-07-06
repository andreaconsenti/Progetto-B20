package controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.entities.Card;
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
    private HBox cardBox;

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
    
	void addCard(Card c) {
    	StackPane p1 = new StackPane();
    	File file = new File("src/view/fxmls/images/Cards/carta_cannone.png");
		Image image = new Image(file.toURI().toString());
		ImageView card = new ImageView(image);
		Label l = new Label("territorio");
		l.setPrefHeight(89);
		l.setPrefWidth(90);
		l.setAlignment(Pos.BOTTOM_CENTER);
		card.setFitHeight(170);
		card.setFitWidth(107);
		p1.getChildren().add(card);
		p1.getChildren().add(l);
		cardBox.getChildren().add(p1);
		
	}
	
	@FXML
	void onAnnullaPressed(ActionEvent event) throws IOException {
		

	}

	@FXML
	void onScambiaPressed(ActionEvent event) throws IOException {
	
	}
}