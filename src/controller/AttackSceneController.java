package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class AttackSceneController {

    @FXML
    private Button attackButton;

    @FXML
    private Button annichilisciButton;

    @FXML
    private Button annullaButton;

    @FXML
    private ChoiceBox<?> scegliNumAttack;
    

    
    public void initialize() {
        assert attackButton != null : "fx:id=\"attackButton\" was not injected: check your FXML file 'AttackScene.fxml'.";
        assert annichilisciButton != null : "fx:id=\"annichilisciButton\" was not injected: check your FXML file 'AttackScene.fxml'.";
        assert annullaButton != null : "fx:id=\"annullaButton\" was not injected: check your FXML file 'AttackScene.fxml'.";
        assert scegliNumAttack != null : "fx:id=\"scegliNumAttack\" was not injected: check your FXML file 'AttackScene.fxml'.";

    }
    
    
}
