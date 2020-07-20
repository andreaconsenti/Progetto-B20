package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.PlayersList;
import model.entities.RisikoGame;
import model.entities.Territory;
import model.entities.RisikoGame.GAME_PHASE;
import model.util.FileHandler;
import model.util.ImageAssets;
import model.util.Observer;
import model.util.Pixel;

public class GameSceneController {
	
	@FXML
	private ImageView mapBackground;
	
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
	
	@FXML
	private Label plTerritories;
	
	@FXML
	private Label plContinents;
	
	@FXML
	private Label plTanks;
	
	
	protected static RisikoGame game; 
	private HashMap<Territory, ArrayList<Pixel>> mappa;
	private HashMap<Territory, territoryStatus> mappaImgTanks;
	private PixelReader pixelReader;
	private PixelWriter pixelWriter;
	private WritableImage wImage;
	private FileHandler fileH = new FileHandler();
	
	private Territory territorySelected;
	public static Territory territory1;
	public static Territory territory2;
	
	
	private static GameSceneController instance;
	
	public GameSceneController() {
		instance = this;
	}
	
	public static GameSceneController getInstance() {
		return instance;
	}
	
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
		game = new RisikoGame(PlayersList.getPlayers(), PlayerSceneController.terrFile, PlayerSceneController.continentsFile, PlayerSceneController.missions);
		
		File file = new File(PlayerSceneController.map);
		Image temp = new Image(file.toURI().toString());
		map.setImage(temp);
		
		File img = new File(PlayerSceneController.territories);
		BufferedImage image = ImageIO.read(img); 	
		
		territoryLabel.setOpacity(0);
		
		mappa = ImageAssets.imageProcess(image, game.getTerritories());
		
		wImage = genWritableMap();
		map.setImage(wImage);
		
		mappaImgTanks = new HashMap<Territory, territoryStatus>();
		initTanks();
		
		statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
		setPlayerLabel();
		setPlayerStatus();
		endTurn.setDisable(true);
		nextPhase.setDisable(true);
		nextPhase.setText("POSIZIONAMENTO");
		
		for(Player p : PlayersList.getPlayers()) {
			if(p.isAI()) {
				aiRecap();
				break;
			}
		}
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
						if((p.getX() == x) && (p.getY() == y) && !t.equals(territory1)) {
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
							if(checkSpostabile(t)) {
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
				setPlayerStatus();
				nextTurn();
				territorySelected = null;
				map.setImage(wImage);
				if(game.firstPhaseEnded()) {
					nextPhase();
				}
			}
			break;
			
		case REINFORCEMENT:
			
			if(territorySelected != null) {
				reinforcementClick();
			}
			break;
			
		case BATTLE:
			
			if(territory1 == null) {
				territory1 = territorySelected;
				setStatusBar();
				setPlayerStatus();
				
			} else if (territory2 == null) {
				if(territorySelected == null || territorySelected.equals(territory1)) {
					territory1 = territorySelected;
					setStatusBar();
					setPlayerStatus();
					break;
				}
				territory2 = territorySelected;
				attackerAndDefenderChosen ();
				updateTanks();
				if (game.verifyMission() == true) {
					missionCompleted();
				};
				territory1 = null;
				territory2 = null;
				setStatusBar();
				setPlayerStatus();
			}
			break;
			
		case FINALMOVE:
			if(territory1 == null) {
				territory1 = territorySelected;
				setStatusBar();
				setPlayerStatus();
				
			} else if (territory2 == null) {
				if(territorySelected == null || territorySelected.equals(territory1)) {
					territory1 = territorySelected;
					setStatusBar();
					setPlayerStatus();
					break;
				}
				territory2 = territorySelected;
				setStatusBar();
				setPlayerStatus();
				moveSceneLoader();
				Integer n = territory1.getTanks();
				updateTanks();
				if (game.verifyMission() == true) {
					missionCompleted();
				};
				nextTurn();
			} else {
				territory1 = null;
				territory2 = null;
				setStatusBar();
				setPlayerStatus();
			}
			break;
		}
	}
	
	public void missionCompleted() throws IOException {
		windowLoader("view/fxmls/MissionCompletedScene.fxml", "Vittoria", true);
	}
	
	public void cardButtonPressed(ActionEvent e) throws IOException {
		windowLoader("view/fxmls/SelectCardScene.fxml", "Carte", false);
	}
	
	public void missionButtonPressed(ActionEvent e) throws IOException {
		windowLoader("view/fxmls/MissionScene.fxml", "Missione", false);
	}
	
	public void menuPressed(ActionEvent e) throws IOException {
		windowLoader("view/fxmls/MenuScene.fxml", "Menu", false);
	}
	
	public void attackerAndDefenderChosen () throws IOException {
		windowLoader("view/fxmls/AttackScene.fxml", "Attacco", false);
	}
	
	public void moveSceneLoader() throws IOException {
		windowLoader("view/fxmls/MoveScene.fxml", "Spostamento", false);
	}
	
	public void newGame() throws IOException {
		Parent playerSceneParent= FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/NewPlayerScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		Stage window = (Stage)(gamePane).getScene().getWindow();
		window.setScene(playerScene);
		window.show();
	}
	
	public void nextPhasePressed(ActionEvent e) {
		nextPhase();
	}
	
	public void endTurnPressed(ActionEvent e) throws InterruptedException {
		nextTurn();
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
	
	private boolean checkSpostabile(Territory t) {
		
		for(Territory t1 : territory1.getConfinanti()) {
			
			if(t1.getId() == t.getId()) {
				
				if(t.getOwner().getName().equals(game.getCurrentTurn().getName())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	public void nextTurn() {
	
		game.nextTurn();
		if(game.getGamePhase() == GAME_PHASE.FIRSTTURN) {
			int i = 0;
			while(game.getCurrentTurn().getBonusTanks() == 0) {
				if(i == PlayersList.players.length) {
					break;
				}
				game.nextTurn();
				i++;
			}
		}
		if(!(game.getGamePhase() == GAME_PHASE.FIRSTTURN))
			nextPhase();
		setStatusBar();
		setPlayerStatus();
		setPlayerLabel();
		territory1 = null;
		territory2 = null;
		if(game.getGamePhase() != GAME_PHASE.FIRSTTURN && game.getCurrentTurn().isAI()) {
			game.getCurrentTurn().playTurn();
		}
	}
	
	public void nextPhase() {
		switch(game.getGamePhase()) {
		case FINALMOVE:
			nextPhase.setText("POSIZIONAMENTO");
			endTurn.setDisable(true);
			nextPhase.setDisable(true);
			break;
		case REINFORCEMENT:
			nextPhase.setText("SPOSTAMENTO");
			nextPhase.setDisable(false);
			endTurn.setDisable(true);
			break;
		case BATTLE:
			nextPhase.setDisable(true);
			endTurn.setDisable(false);
			break;
		}
		game.nextPhase();
		setStatusBar();
		setPlayerLabel();
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
	   
		ArrayList<Pixel> posList = fileH.addPosizione(PlayerSceneController.terrFile);
		
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
		    Label tanksNumber = new Label();
		    Integer tanksN = t.getTanks();
		    tanksNumber.setText(tanksN.toString());
		    tanksNumber.relocate(p.getX(),p.getY());
		    tanksNumber.setStyle("-fx-text-fill: white smoke; -fx-font-weight: bold;");
		    tanksPane.getChildren().add(tank);
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
	
	public Player getCurrentPlayer() {
		return game.getCurrentTurn();
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
	
	private void setPlayerStatus() {
		Integer tmp;
		tmp = game.getPlayer(game.getCurrentTurn()).getTanks();
		plTanks.setText(tmp.toString());
		tmp = game.getCurrentTurn().getTerritories();
		plTerritories.setText(tmp.toString());
		tmp = game.getCurrentTurn().getContinents();
		plContinents.setText(tmp.toString());
	}
	
	public void updateTanks() {
    	Integer n = territory1.getTanks();
		mappaImgTanks.get(territory1).getNumber().setText(n.toString());
		n = territory2.getTanks();
		mappaImgTanks.get(territory2).getNumber().setText(n.toString());
		
		File file = new File(getTankPath(territory1));
		Image image = new Image(file.toURI().toString());
		mappaImgTanks.get(territory1).getImage().setImage(image);
		file = new File(getTankPath(territory2));
		image = new Image(file.toURI().toString());
		mappaImgTanks.get(territory2).getImage().setImage(image);
	}
	
	public RisikoGame getGame() {
		return game;
	}
	
	public void setSelTerritory(Territory t) {
		territorySelected = t;
	}
	
	public void setTerritory12(Territory t1, Territory t2) {
		territory1 = t1;
		territory2 = t2;
	}
	
	public void setTerritory2(Territory t) {
		territory2 = t;
	}
	
	private void windowLoader(String scene, String title, boolean cantclose) throws IOException {
		Parent sceneParent = FXMLLoader.load(getClass().getClassLoader().getResource(scene));
		Scene mScene = new Scene(sceneParent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle(title);
		window.setScene(mScene);
		if (cantclose) {
			window.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					// consume event
					event.consume();
				}
			});
		}
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
		setStatusBar();
	}
	
	public void firstTurn() {
		game.getCurrentTurn().placeTank(1);
		game.addTerritoryTanks(territorySelected);
		Integer n = game.getTerritory(territorySelected).getTanks();
		mappaImgTanks.get(territorySelected).getNumber().setText(n.toString());
		setStatusBar();
		nextTurn();
		territorySelected = null;
		map.setImage(wImage);
		if(game.firstPhaseEnded()) {
			nextPhase();
		}
	}
	
	public void reinforcementClick() throws IOException {
		game.getCurrentTurn().placeTank(1);
		game.addTerritoryTanks(territorySelected);
		Integer n = game.getTerritory(territorySelected).getTanks();
		mappaImgTanks.get(territorySelected).getNumber().setText(n.toString());
		setStatusBar();
		setPlayerStatus();
		if (game.verifyMission() == true) {
			missionCompleted();
		}
		if(game.getCurrentTurn().getBonusTanks() == 0) {
			nextPhase();
		}
	}

	private void aiRecap() throws IOException {
		Parent aiRecapParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/AIRecapScene.fxml"));
		Scene aiRecapScene = new Scene(aiRecapParent);
		Stage window = new Stage();
		window.setResizable(false);
		window.setTitle("Azioni AI");
		window.setScene(aiRecapScene);
		window.setResizable(true);
		window.show();
	}	
}