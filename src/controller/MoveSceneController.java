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
	private Label territory1;
	
	@FXML
	private Label territory2;
	
	@FXML
	private Label number2;
	
	@FXML
	private Label number1;
	
	@FXML
	private Slider slider;
	
	@FXML
	private Button move;
	
	private Integer t1;
	
	private Integer t2;
	
	private Integer tn;
	
	private boolean conquer;
	
	
	public void initialize(){
		
		t1 = GameSceneController.territory1.getTanks();
 		t2 = GameSceneController.territory2.getTanks();
		territory1.setText(GameSceneController.territory1.getName());
		territory2.setText(GameSceneController.territory2.getName());
		tn = t1 -  1;
		number1.setText(tn.toString());
		tn = t2 + 1;
		number2.setText(tn.toString());
		
		int n = GameSceneController.territory1.getTanks() - 1;
		if(GameSceneController.territory2.getTanks() == 0) {
			conquer = true;
		}
		
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
		GameSceneController.game.moveTanks(GameSceneController.territory1, GameSceneController.territory2, GameSceneController.territory1.getTanks() - 1);
		Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
		window.close();
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
	
	
	public void minPressed(ActionEvent e) {
		slider.setValue(1);
		Integer temp = t1 - (int)slider.getValue();
		number1.setText(temp.toString());
		temp = t2 + (int)slider.getValue();
		number2.setText(temp.toString());
	}
	
	public void maxPressed(ActionEvent e) {
		slider.setValue(GameSceneController.territory1.getTanks() - 1);
		Integer temp = t1 - (int)slider.getValue();
		number1.setText(temp.toString());
		temp = t2 + (int)slider.getValue();
		number2.setText(temp.toString());
	}
	
	
	
	
	
	
}
