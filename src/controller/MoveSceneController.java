package controller;

import java.awt.event.ActionListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class MoveSceneController {

	@FXML
	Label territory1;
	
	@FXML
	Label territory2;
	
	@FXML
	Slider slider;
	
	@FXML
	Button move;
	
	
	
	
	public void initialize() {
		
		territory1.setText(GameSceneController.territory1.getName());
		territory2.setText(GameSceneController.territory2.getName());
		
		int n = GameSceneController.territory1.getTanks() - 1;
		
		slider.setMax(n);
		slider.setMin(1);
		slider.setBlockIncrement(1);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setShowTickLabels(true);
		slider.setSnapToTicks(true);
		
	}
	
	
	public void movePressed(ActionEvent e) {
		System.out.println(slider.getValue());
	}
	
	public void moveEverythingPressed(ActionEvent e) {
		
	}
	
	public void plusPressed(ActionEvent e) {
		slider.setValue(slider.getValue() + 1);
	}
	
	public void minusPressed(ActionEvent e) {
		slider.setValue(slider.getValue() - 1);
	}
	
	
	
	
	
	
	
	
	
}
