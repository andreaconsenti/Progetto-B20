package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.PlayersList;
import model.entities.RisikoGame;
import model.entities.Territory;
import model.entities.RisikoGame.GAME_PHASE;
import model.util.FileHandler;
import model.util.ImageAssets;
import model.util.Pixel;

public class GameSceneController {
	
	@FXML
	private ImageView map;
	
	@FXML
	private Pane gamePane;
	
	@FXML
	private Label territoryLabel;
	
	@FXML
	private AnchorPane tanksPane;
	
	@FXML
	private TextArea statusBar;
	
	@FXML
	private Button cardButton;
	
	@FXML
	private Label turnLabel;
	
	@FXML
	private Button nextPhase;
	
	@FXML
	private Button endTurn;
	
	
	protected static RisikoGame game; 
	private HashMap<Territory, ArrayList<Pixel>> mappa;
	private HashMap<Territory, territoryStatus> mappaImgTanks;
	private PixelReader pixelReader;
	private PixelWriter pixelWriter;
	private WritableImage wImage;
	private FileHandler fileH = new FileHandler();
	
	private Territory territorySelected;
	protected static Territory territory1;
	protected static Territory territory2;
	
	
	private class territoryStatus{
		private ImageView image;
		private Label number;
		
		public territoryStatus(ImageView img, Label lbl) {
			setImage(img);
			setNumber(lbl);
		}

		public Label getNumber() {
			return number;
		}

		public void setNumber(Label number) {
			this.number = number;
		}

		public ImageView getImage() {
			return image;
		}

		public void setImage(ImageView image) {
			this.image = image;
		}
		
	}
	
	
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
		
		mappaImgTanks = new HashMap<Territory, territoryStatus>();
		initTanks();
		
		statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
		setPlayerLabel();
		endTurn.setDisable(true);
		nextPhase.setDisable(true);
		nextPhase.setText("POSIZIONAMENTO");
	}
	
	
	
	public void mouseMoved(MouseEvent e) {
		
		int x =  (int)e.getX();
		int y =  (int)e.getY();
		int check;
		
		switch(game.getGamePhase()) {
		
		case FIRSTTURN:
			for(Territory t : game.getTerritories()) {
				
				check = 0;
				for(Pixel p : mappa.get(t)) {
					
					if((p.getX() == x) && (p.getY() == y)) {
						check = 1;
						territoryLabel.setOpacity(100);
						territoryLabel.setText(territoryText(t));
						if(game.getCurrentTurn().equals(t.getOwner())) {
							changeColor(mappa.get(t));
							territorySelected = t;
						}
						break;
					} else {
						map.setImage(wImage);
						territoryLabel.setOpacity(0);
						territorySelected = null;
					}
					
				}
				if(check == 1) {
					break;
				}
			}
			break;
			
		case REINFORCEMENT:
			for(Territory t : game.getTerritories()) {
				
				check = 0;
				for(Pixel p : mappa.get(t)) {
					
					if((p.getX() == x) && (p.getY() == y)) {
						check = 1;
						territoryLabel.setOpacity(100);
						territoryLabel.setText(territoryText(t));
						if(game.getCurrentTurn().equals(t.getOwner())) {
							changeColor(mappa.get(t));
							territorySelected = t;
						}
						break;
					} else {
						map.setImage(wImage);
						territoryLabel.setOpacity(0);
						territorySelected = null;
					}
					
				}
				if(check == 1) {
					break;
				}
			}
			break;
			
		case BATTLE:
			if(territory1 == null) {
				for(Territory t : game.getTerritories()) {
					check = 0;
					for(Pixel p : mappa.get(t)) {
						if((p.getX() == x) && (p.getY() == y)) {
							check = 1;
							territoryLabel.setOpacity(100);
							territoryLabel.setText(territoryText(t));
							if(game.getCurrentTurn().equals(t.getOwner()) && t.getTanks()>1) {
								changeColor(mappa.get(t));
								territorySelected = t;
							}
							break;
						} else {
							map.setImage(wImage);
							territoryLabel.setOpacity(0);
							territorySelected = null;
						}
					}
					if(check == 1) {
						break;
					}
				}
			} else if(territory2 == null) {
				for(Territory t : game.getTerritories()) {
					check = 0;
					for(Pixel p : mappa.get(t)) {
						if((p.getX() == x) && (p.getY() == y)) {
							check = 1;
							territoryLabel.setOpacity(100);
							territoryLabel.setText(territoryText(t));
							if(checkAttaccabile(t)) {
								changeColor(mappa.get(t));
								territorySelected = t;
							}
							break;
						} else {
							map.setImage(wImage);
							territoryLabel.setOpacity(0);
							territorySelected = null;
						}
					}
					if(check == 1) {
						break;
					}
				}
				
			} else {
				map.setImage(wImage);
			}
			break;
			
		case FINALMOVE:
			if(territory1 == null) {
				for(Territory t : game.getTerritories()) {
					check = 0;
					for(Pixel p : mappa.get(t)) {
						if((p.getX() == x) && (p.getY() == y)) {
							check = 1;
							territoryLabel.setOpacity(100);
							territoryLabel.setText(territoryText(t));
							if(game.getCurrentTurn().equals(t.getOwner()) && t.getTanks()>1) {
								changeColor(mappa.get(t));
								territorySelected = t;
							}
							break;
						} else {
							map.setImage(wImage);
							territoryLabel.setOpacity(0);
							territorySelected = null;
						}
					}
					if(check == 1) {
						break;
					}
				}
			} else if(territory2 == null) {
				for(Territory t : game.getTerritories()) {
					check = 0;
					for(Pixel p : mappa.get(t)) {
						if((p.getX() == x) && (p.getY() == y)) {
							check = 1;
							territoryLabel.setOpacity(100);
							territoryLabel.setText(territoryText(t));
							if(game.getCurrentTurn().equals(t.getOwner()) && !t.equals(territory1)) {
								changeColor(mappa.get(t));
								territorySelected = t;
							}
							break;
						} else {
							map.setImage(wImage);
							territoryLabel.setOpacity(0);
							territorySelected = null;
						}
					}
					if(check == 1) {
						break;
					}
				}
				
			} else {
				map.setImage(wImage);
			}
			break;
			
		}
	}
	
	
	
	public void mouseClicked(MouseEvent e) throws IOException {
		
		switch(game.getGamePhase()) {
		
		case FIRSTTURN:
			
			if(territorySelected != null) {
				game.getCurrentTurn().placeTank(1);
				game.addTerritoryTanks(territorySelected);
				Integer n = game.getTerritory(territorySelected).getTanks();
				mappaImgTanks.get(territorySelected).getNumber().setText(n.toString());
				setStatusBar();
				nextTurn();
				territorySelected = null;
				map.setImage(wImage);
				if(game.getBonusTanksSum() == 0) {
					nextPhase();
				}
			}
			break;
			
		case REINFORCEMENT:
			
			if(territorySelected != null) {
				game.getCurrentTurn().placeTank(1);
				game.addTerritoryTanks(territorySelected);
				Integer n = game.getTerritory(territorySelected).getTanks();
				mappaImgTanks.get(territorySelected).getNumber().setText(n.toString());
				setStatusBar();
				if(game.getCurrentTurn().getBonusTanks() == 0) {
					nextPhase();
				}
			}
			break;
			
		case BATTLE:
			
			if(territory1 == null) {
				territory1 = territorySelected;
				setStatusBar();
				
			} else if (territory2 == null) {
				if(territorySelected == null) {
					territory1 = territorySelected;
				}
				territory2 = territorySelected;
				setStatusBar();
				attackerAndDefenderChosen ();
			} else {
				territory1 = null;
				territory2 = null;
				setStatusBar();
			}
			break;
			
		case FINALMOVE:
			if(territory1 == null) {
				territory1 = territorySelected;
				setStatusBar();
				
			} else if (territory2 == null) {
				if(territorySelected == null) {
					territory1 = territorySelected;
				}
				territory2 = territorySelected;
				setStatusBar();
				moveSceneLoader();
			} else {
				territory1 = null;
				territory2 = null;
				setStatusBar();
			}
			break;
		}
		
	
	
	}
	
	public void cardButtonPressed(ActionEvent e) throws IOException {
		Parent cardSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/SelectCardScene.fxml"));
		Scene cardScene = new Scene(cardSceneParent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Carte");
		window.setScene(cardScene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
		
	}
	
	public void missionButtonPressed(ActionEvent e) throws IOException {
		Parent cardSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/MissionScene.fxml"));
		Scene cardScene = new Scene(cardSceneParent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Missione");
		window.setScene(cardScene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
	}
	
	public void nextPhasePressed(ActionEvent e) {
		nextPhase();
	}
	

	public void attackerAndDefenderChosen () throws IOException {
		Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/AttackScene.fxml"));
		Scene scene = new Scene(parent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Attacco");
		window.setScene(scene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
	}
	
	public void moveSceneLoader() throws IOException {
		Parent moveSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/MoveScene.fxml"));
		Scene moveScene = new Scene(moveSceneParent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Spostamento");
		window.setScene(moveScene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
	}
	
	private boolean checkAttaccabile(Territory t) {
		
		for(Territory t1 : territory1.getConfinanti()) {
			
			if(t1.getId() == t.getId()) {
				
				if(!t.getOwner().getName().equals(game.getCurrentTurn().getName())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
//	private boolean checkSpostabile(Territory t) {
//		
//		for(Territory t1 : territory1.getConfinanti()) {
//			
//			if(t1.getId() == t.getId()) {
//				
//				if(t.getOwner().getName().equals(game.getCurrentTurn().getName())) {
//					return true;
//				} else {
//					return false;
//				}
//			}
//		}
//		return false;
//	}
	
	private void nextTurn() {
		game.nextTurn();
		setStatusBar();
		setPlayerLabel();
	}
	
	private void nextPhase() {
		game.nextPhase();
		setStatusBar();
		setPlayerLabel();
		switch(game.getGamePhase()) {
		case BATTLE:
			nextPhase.setText("SPOSTAMENTO");
			nextPhase.setDisable(false);
			break;
		case FINALMOVE:
			nextPhase.setDisable(true);
			endTurn.setDisable(false);
			break;
		}
	}
	
	private Color returnPlayerColor(Player p) {
		switch(p.getColor()) {
		case RED:
			return Color.DARKRED;
		case BLACK:
			return Color.BLACK;
		case YELLOW:
			return Color.YELLOW;
		case PINK:
			return Color.DEEPPINK;
		case GREEN:
			return Color.GREEN;
		case BLUE:
			return Color.DARKSLATEBLUE;
		}
		return Color.BLACK;
		
	}
	
	private void setPlayerLabel() {
		turnLabel.setTextFill(returnPlayerColor(game.getCurrentTurn()));
		turnLabel.setText(game.getCurrentTurn().getName());
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
	   
	    
		
		ArrayList<Pixel> posList = fileH.addPosizione("assets/infoTerritori.txt");
		
		int i = 0;
		for(Pixel p : posList) {
			
			Territory t = game.getTerritories().get(i);
			File file = new File(getTankPath(t));
			Image image = new Image(file.toURI().toString());
			ImageView tank = new ImageView(image);
			tank.setImage(image);
		    tank.setX(p.getX());
		    tank.setY(p.getY());
		    tank.setFitWidth(42.5);
		    tank.setFitHeight(47.5);
//		    Circle circle = new Circle();
//		    circle.relocate(p.getX()+4,p.getY()+7);
//		    circle.setRadius(10);
//		    circle.setOpacity(0.4);
//		    circle.setStyle("-fx-fill: black;");
		    Label tanksNumber = new Label();
		    Integer tanksN = t.getTanks();
		    tanksNumber.setText(tanksN.toString());
		    tanksNumber.relocate(p.getX(),p.getY());
//		    tanksNumber.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
		    tanksNumber.setStyle("-fx-text-fill: white smoke; -fx-font-weight: bold;");
		    tanksPane.getChildren().add(tank);
//		    tanksPane.getChildren().add(circle);
		    tanksPane.getChildren().add(tanksNumber);
		    territoryStatus status = new territoryStatus(tank, tanksNumber);
		    mappaImgTanks.put(t, status);
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
	
	
	
	private void swapTerritories() {
		
		Player temp = territory1.getOwner();
		territory1.setOwner(territory2.getOwner());
		territory2.setOwner(temp);
		
		File file = new File(getTankPath(territory1));
		Image image = new Image(file.toURI().toString());
		mappaImgTanks.get(territory1).getImage().setImage(image);
		
		File file2 = new File(getTankPath(territory2));
		Image image2 = new Image(file2.toURI().toString());
		mappaImgTanks.get(territory2).getImage().setImage(image2);
		
		
		
	}
	
	private void setStatusBar() {
		switch(game.getGamePhase()) {
		case FIRSTTURN:
			statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
			break;
		case REINFORCEMENT:
			statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
			break;
		case BATTLE:
			if(territory1 == null) {
				statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un territorio con cui attaccare");
			} else if(territory2 == null) {
				statusBar.setText("Territorio selezionato: " + territory1.getName() + "\n" + game.getCurrentTurn().getName() + ": seleziona un territorio da attaccare");
			} else if((territory1 != null) && (territory2 != null)) {
				statusBar.setText("Attacco in corso");
			}
			break;
		case FINALMOVE:
			if(territory1 == null) {
				statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un territorio dal quale spostare armate");
			} else if(territory2 == null) {
				statusBar.setText("Territorio selezionato: " + territory1.getName() + "\n" + game.getCurrentTurn().getName() + ": seleziona un territorio sul quale spostare armate");
			} else if((territory1 != null) && (territory2 != null)) {
				statusBar.setText("Spostamento");
			}
			break;
		
		}
	}
	
	
}
