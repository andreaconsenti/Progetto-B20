package controller;

import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AIRecapSceneController {
	@FXML
	TextFlow textFlow;
	
	private static AIRecapSceneController instance;
	
	/**
	 * Creates an instance of this controller
	 */
	public AIRecapSceneController() {
		instance = this;
	}
	
	/**
	 * Instance getter
	 * @return instance
	 */
	public static AIRecapSceneController getInstance() {
		return instance;
	}

	/**
	 * Sets the text in the window
	 * @param text is the text
	 */
	public void setText(String text) {
		Text t = new Text(text);
		t.setFill(Paint.valueOf(getColor()));
		textFlow.getChildren().add(t);
		t.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
	}
	
	/**
	 * Gets the color of the player
	 * @return Color
	 */
	private String getColor() {

		
		switch(GameSceneController.getInstance().getCurrentPlayer().getColor()) {
		case YELLOW: 
			return "#e3ce12";
		case BLUE:
			return "#1254e3";
		case BLACK:
			return "#000000";
		case PINK:
			return "#d918f2";
		case RED:
			return "#b50000";
		case GREEN:
			return "#44cf38";
		}
		
		return null;
	}
	
	
}
