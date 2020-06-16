package controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    
    


    @FXML
    void initialize() {
      
    }

    
    
    
	/*void addCard() {
		//aggiungere stackPane
		//la prima è cardImage0 e territoryName0, la seconda è cardImage01 e territoryName01, poi 02 e 02, etc...
		
	}
	
	
	
	@FXML
	void onAnnullaPressed(ActionEvent event) {
		//cancella le 3 carte dal posto
		 * 
		 * 
		 * switch(game.getGamePhase()) {
		
		case FIRSTTURN:
			pulsanti non attivi e paneScambioCarte.setOpacity(0.35);
			break;
			
		case REINFORCEMENT:
			pulsanti attivi
			break;
			
		case BATTLE:
			pulsanti non attivi e paneScambioCarte.setOpacity(0.35);
			break;
			
		case FINALMOVE:
			pulsanti non attivi e paneScambioCarte.setOpacity(0.35);
			break;
		}
	}


	@FXML
	void onScambiaPressed(ActionEvent event) {
		//verifica e fa combinazione
		 * 
		 * 
		 * switch(game.getGamePhase()) {
		
		case FIRSTTURN:
			pulsanti non attivi e paneScambioCarte.setOpacity(0.35);
			break;
			
		case REINFORCEMENT:
			pulsanti attivi
			break;
			
		case BATTLE:
			pulsanti non attivi e paneScambioCarte.setOpacity(0.35);
			break;
			
		case FINALMOVE:
			pulsanti non attivi e paneScambioCarte.setOpacity(0.35);
			break;
			}
	}*/


}

