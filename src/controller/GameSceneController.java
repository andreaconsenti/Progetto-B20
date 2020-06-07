package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
	
	@FXML
	AnchorPane tanksPane;
	
	RisikoGame game; 
	HashMap<Territory, ArrayList<Pixel>> mappa;
	PixelReader pixelReader;
	PixelWriter pixelWriter;
	WritableImage wImage;
	FileHandler fileH = new FileHandler();
	
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
		
		initTanks();

		
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
	
	private void initTanks() throws IOException {
	   
	    
		
		ArrayList<Pixel> posList = fileH.addPosizione("assets/confiniEPosizioni.txt");
		
		int i = 0;
		for(Pixel p : posList) {
			
			Territory t = game.getTerritories().get(i);
			File file = new File(getTankPath(t));
			Image image = new Image(file.toURI().toString());
			ImageView tank = new ImageView(image);
			tank.setImage(image);
		    tank.setX(p.getX());
		    tank.setY(p.getY());
		    tank.setFitWidth(30);
		    tank.setFitHeight(35);
		    Label tanksNumber = new Label();
		    Integer tanksN = t.getTanks();
		    tanksNumber.setText(tanksN.toString());
		    tanksNumber.relocate(p.getX(),p.getY());
		    tanksPane.getChildren().add(tank);
		    tanksPane.getChildren().add(tanksNumber);
			i++;
		}
		
		
	}
	
	private String getTankPath(Territory t) {
		Player tempPlayer = t.getOwner();
		
		switch(tempPlayer.getColor()) {
		case RED:
			return "src/view/fxmls/images/tanks/tankrosso.png";
		case YELLOW:
			return "src/view/fxmls/images/tanks/tank_giallo.png";
		case BLACK:
			return "src/view/fxmls/images/tanks/tank_nero.png";
		case BLUE:
			return "src/view/fxmls/images/tanks/tank_blu.png";
		case GREEN:
			return "src/view/fxmls/images/tanks/tank_verde.png";
		case PINK:
			return "src/view/fxmls/images/tanks/tank_rosa.png";
		
		}
		
		return null;
		
	}
	
}
