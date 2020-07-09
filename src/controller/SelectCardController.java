package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private StackPane selPane1;
    
    @FXML
    private StackPane selPane2;
    
    @FXML
    private StackPane selPane3;
    
    
    private HashMap<Card, CardGui> cards;
    private Card card1;
    private Card card2;
    private Card card3;
    
    
    private class CardGui {
    	private ImageView img;
    	private StackPane pane;
    	private Label lbl;
    	
    	public CardGui(ImageView img, StackPane pane, Label lbl) {
    		this.img = img;
    		this.setPane(pane);
    		this.lbl = lbl;
    	}

		public ImageView getImg() {
			return img;
		}

		public StackPane getPane() {
			return pane;
		}

		public void setPane(StackPane pane) {
			this.pane = pane;
		}

		public Label getLbl() {
			return lbl;
		}
    }
    
    

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
    	
    	cards = new HashMap<Card, CardGui>();
    	
    	for(Card ca : GameSceneController.game.getCurrentTurn().getCards()) {
    		addCard(ca);
    	}
    	
    	card1 = null;
    	card2 = null;
    	card3 = null;
    	
    	
    	
    }
    
	void addCard(Card c) {
    	StackPane p1 = new StackPane();
    	File file = new File(genCardPath(c));
		Image image = new Image(file.toURI().toString());
		ImageView card = new ImageView(image);
		Label l = new Label(c.getTerritory().getName());
		l.setPrefWidth(90);
		l.setTranslateY(35);
		l.setAlignment(Pos.BOTTOM_CENTER);
		card.setFitHeight(180);
		card.setFitWidth(117);
		card.setStyle("-fx-cursor: hand;");
		card.setOnMouseClicked((MouseEvent e) -> {
			if(GameSceneController.game.getGamePhase() == GAME_PHASE.REINFORCEMENT) {
				if(cards.get(c).getPane().getParent() == cardBox) {
					moveCard(cards.get(c).getPane());
					useCard(c);
				} else {
					cardBox.getChildren().add(cards.get(c).getPane());
					unuseCard(c);
				}
			}
	    });
		p1.getChildren().add(card);
		p1.getChildren().add(l);
		cardBox.getChildren().add(p1);
		
		cards.put(c, new CardGui(card, p1, l));
	}
	
	@FXML
	void onAnnullaPressed(ActionEvent event) throws IOException {
		

	}

	@FXML
	void onScambiaPressed(ActionEvent event) throws IOException {
	
	}
	
	private void moveCard(StackPane p) {
		if(selPane1.getChildren().size() == 0) {
			selPane1.getChildren().add(p);
		} else if (selPane2.getChildren().size() == 0) {
			selPane2.getChildren().add(p);
		} else if (selPane3.getChildren().size() == 0) {
			selPane3.getChildren().add(p);
		}
	}
	
	private void useCard(Card ca) {
		if(card1 == null) {
			card1 = ca;
		} else if (card2 == null) {
			card2 = ca;
		} else if (card3 == null) {
			card3 = ca;
		}
	}
	
	private void unuseCard(Card ca) {
		if(ca.equals(card1)) {
			card1 = null;
		} else if (ca.equals(card2)) {
			card2 = null;
		} else if (ca.equals(card3)) {
			card3 = null;
		}
	}
	
	private String genCardPath(Card c) {
		switch(c.getFigure()) {
		case CANNONE:
			return "src/view/fxmls/images/Cards/carta_cannone.png";
			
		case FANTE:
			return "src/view/fxmls/images/Cards/carta_fante.png";

		case CAVALIERE:
			return "src/view/fxmls/images/Cards/carta_cavallo.png";
			
		case JOLLY:
			return "src/view/fxmls/images/Cards/carta_vuota.png";
			
		}
		return null;
	}
}