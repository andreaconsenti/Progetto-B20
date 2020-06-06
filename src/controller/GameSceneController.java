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
	WritableImage wImage;
	
	public void initialize() throws NumberFormatException, IOException{
		game = new RisikoGame(PlayersList.getPlayers());
		File img = new File("src/view/fxmls/images/Territory_Color2.png");
		BufferedImage image = ImageIO.read(img ); 
		
		territoryLabel.setOpacity(0);
		
		mappa = ImageAssets.imageProcess(image, game.getTerritories());
		
		wImage = genWritableMap();
		map.setImage(wImage);
//		map.setX(gamePane.getLayoutX());
//		map.setY(gamePane.getLayoutY());

		
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
					changeColor(mappa.get(t));
					break;
				} else {
					map.setImage(wImage);
					territoryLabel.setOpacity(0);
				}
				
			}
			if(check == 1) {
				break;
			}
			
			
			
		}
	
	}
	
	
	
	
	private void changeColor(ArrayList<Pixel> list) {
		WritableImage tempWImage = genWritableMap();
		for(Pixel p : list) {
			Color color = pixelReader.getColor(p.getX(), p.getY());
			double r = color.getRed() / 1.5;
			double g = color.getGreen() / 1.5;
			double b = color.getBlue() / 1.5;
			int red = (int) (r * 255);
			int green = (int) (g * 255);
			int blue = (int) (b * 255);
			color = Color.rgb(red,green,blue);
			PixelWriter pxlWriter = tempWImage.getPixelWriter();
			pxlWriter.setColor(p.getX(),p.getY(),color);
		}
		map.setImage(tempWImage);

	}
	
	private String territoryText(Territory t) {
		return "Territorio: " + t.getName();
	}
	
	
	private WritableImage genWritableMap() {
		pixelReader = map.getImage().getPixelReader();
		WritableImage wImg = new WritableImage((int) map.getImage().getWidth(),(int) map.getImage().getHeight());
		pixelWriter = wImg.getPixelWriter();
		for (int y = 0; y < map.getImage().getHeight(); y++){
			
				for (int x = 0; x < map.getImage().getWidth(); x++){
					
					Color color = pixelReader.getColor(x, y);
					pixelWriter.setColor(x,y,color);
				}
				
		}
		return wImg;
	}
	
	
}
