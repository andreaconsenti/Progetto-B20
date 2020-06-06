package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.PlayersList;
import model.entities.RisikoGame;
import model.entities.Territory;
import model.util.FileHandler;
import model.util.ImageAssets;
import model.util.Pixel;

public class GameSceneController {
	
	@FXML
	ImageView map;
	
	@FXML
	Pane gamePane;
	
	@FXML
	Label territoryLabel;
	
	RisikoGame game; 
	HashMap<Territory, ArrayList<Pixel>> mappa;
	PixelReader pixelReader;
	PixelWriter pixelWriter;
	
	public void initialize() throws NumberFormatException, IOException{
		game = new RisikoGame(PlayersList.getPlayers());
		File img = new File("src/view/fxmls/images/Territory_Color2.png");
		BufferedImage image = ImageIO.read(img ); 
		
		territoryLabel.setOpacity(0);
		
		mappa = ImageAssets.imageProcess(image, game.getTerritories());
		
//		pixelReader = map.getImage().getPixelReader();
//		WritableImage wImage = new WritableImage(
//				 (int) map.getImage().getWidth(),
//				(int) map.getImage().getHeight());
//		pixelWriter = wImage.getPixelWriter();
//		for (int y = 0; y < image.getHeight(); y++){
//			
//				for (int x = 0; x < image.getWidth(); x++){
//					
//					Color color = pixelReader.getColor(x, y);
//					pixelWriter.setColor(x,y,color);
//				}
//				
//		}
//		map.setImage(wImage);

		
	}
	
	
	
	public void mouseMoved(MouseEvent e) {
		int x =  (int)e.getX();
		int y =  (int)e.getY();
		int check;
		
		for(Territory t : game.getTerritories()) {
			
			check = 0;
			for(Pixel p : mappa.get(t)) {
				
				if((p.getX() == x) && (p.getY() == y)) {
					check = 1;
					territoryLabel.setOpacity(100);
					territoryLabel.setText(territoryText(t));
					break;
				} else {
					territoryLabel.setOpacity(0);
				}
				
			}
			if(check == 1) {
				break;
			}
			
			
			
		}
	
	}
	
	
	
	
//	private void changeColor(ArrayList<Pixel> list) {
//		for (int y = 0; y < getMaxY(list); y++)
//		{
//		for (int x = 0; x < getMaxX(list); x++)
//		{
//		Color color = pixelReader.getColor(x, y);
//		double r = color.getRed() / 1.5;
//		double g = color.getGreen() / 1.5;
//		double b = color.getBlue() / 1.5;
//		int red = (int) (r * 255);
//		int green = (int) (g * 255);
//		int blue = (int) (b * 255);
//		color = Color.rgb(red,green,blue);
//		pixelWriter.setColor(x,y,color);
//		}
//		}
//		imageView.setImage(wImage);
//
//	}
	
	private String territoryText(Territory t) {
		return "Territorio: " + t.getName();
	}
	
	private int getMaxX(ArrayList<Pixel> l) {
		int x = 0;
		for(Pixel p : l) {
			if (p.getX() > x) {
				x = p.getX();
			}
		}
		return x;
	}
	
	private int getMaxY(ArrayList<Pixel> l) {
		int y = 0;
		for(Pixel p : l) {
			if (p.getX() > y) {
				y = p.getY();
			}
		}
		return y;
	}
	
	
}
