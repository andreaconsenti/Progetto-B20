package controller;

import java.awt.event.ActionListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MoveSceneController {

	@FXML
	Label territory1;
	
	@FXML
	Label territory2;
	
	@FXML
	Label number2;
	
	@FXML
	Label number1;
	
	@FXML
	Slider slider;
	
	@FXML
	Button move;
	
	Integer t1;
	
	Integer t2;
	
	
	public void initialize() {
		
		t1 = GameSceneController.territory1.getTanks();
 		t2 = GameSceneController.territory2.getTanks();
		territory1.setText(GameSceneController.territory1.getName());
		territory2.setText(GameSceneController.territory2.getName());
		number1.setText(t1.toString());
		number2.setText(t2.toString());
		
		int n = GameSceneController.territory1.getTanks() - 1;
		
		slider.setMax(n);
		slider.setMin(1);
		slider.setBlockIncrement(1);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setShowTickLabels(true);
		slider.setSnapToTicks(true);
		
		slider.setMouseTransparent(true);
		
	}
	
	
	public void movePressed(ActionEvent e) {
		GameSceneController.game.moveTanks(GameSceneController.territory1, GameSceneController.territory2, (int)slider.getValue());
		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
	}
	
	public void moveEverythingPressed(ActionEvent e) {
		
	}
	
	public void plusPressed(ActionEvent e) {
		slider.setValue(slider.getValue() + 1);
		Integer temp = t1 - (int)slider.getValue();
		number1.setText(temp.toString());
		temp = t2 + (int)slider.getValue();
		number2.setText(temp.toString());
	}
	
	public void minusPressed(ActionEvent e) {
		slider.setValue(slider.getValue() - 1);
		Integer temp = t1 - (int)slider.getValue();
		number1.setText(temp.toString());
		temp = t2 + (int)slider.getValue();
		number2.setText(temp.toString());
	}
	
	
	public void dragDone(MouseEvent e) {
//		Integer temp = t1 - (int)slider.getValue();
//		number1.setText(temp.toString());
//		temp = t2 + (int)slider.getValue();
//		number2.setText(temp.toString());
		
	}
	
	
	
	
	
	
}
