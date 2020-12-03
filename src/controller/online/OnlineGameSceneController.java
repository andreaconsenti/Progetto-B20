package controller.online;

import controller.PlayerSceneController;
import controller.RemoteJoin;
import controller.RemotePlay;
import controller.online.mouseFuction.*;
//import controller.mouseFunction.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entities.Player;
import model.entities.PlayersList;
import model.entities.RisikoGame;
import model.entities.RisikoGame.GAME_PHASE;
import model.entities.Territory;
import model.util.FileHandler;
import model.util.ImageAssets;
import model.util.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class OnlineGameSceneController implements RemotePlay {

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
	private HashMap<GAME_PHASE, FunctionExecutor> executors;


	private static OnlineGameSceneController instance;

	private static RemotePlay playStub;

	/**
     * Sets the instance to this instance of GameSceneController
     */
	public OnlineGameSceneController() {
		instance = this;
	}
	
	/**
	 * Instance getter
	 * @return instance
	 */
	public static OnlineGameSceneController getInstance() {
		return instance;
	}


	private class territoryStatus{
		private ImageView image;
		private Label number;
		
		/**
	     * Constructor of a territory linked with its image and its name
	     * @param img is the image of the territory
	     * @param lbl is the number of the territory that corresponds with the name
	     */
		public territoryStatus(ImageView img, Label lbl) {
			setImage(img);
			setNumber(lbl);
		}
		
		/**
	     * Returns the number of the territory
	     * @return number
	     */
		public Label getNumber() {
			return number;
		}
		
		/**
	     * Sets the number of the territory
	     * @param number is the number
	     */
		public void setNumber(Label number) {
			this.number = number;
		}
		
		/**
	     * Returns the image of the territory
	     * @return image
	     */
		public ImageView getImage() {
			return image;
		}
		
		/**
	     * Sets the image of the territory
	     * @param image is the image
	     */
		public void setImage(ImageView image) {
			this.image = image;
		}
		
	}
	
	/**
     * Initializes the controller
     * @throws NumberFormatException
     * @throws IOException
     */
	public void initialize() throws NumberFormatException, IOException, NotBoundException {
		if (OnlineSceneController.isOnlineMultiplayer == true) {
			initializeOnline();
		}
		else {
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

			executors = new HashMap<GAME_PHASE, FunctionExecutor>();
			initExecutors();

			statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
			setPlayerLabel();
			setPlayerStatus();
			endTurn.setDisable(true);
			nextPhase.setDisable(true);
			nextPhase.setText("POSIZIONAMENTO");
		}

		for(Player p : PlayersList.getPlayers()) {
			if(p.isAI()) {
				aiRecap();
				break;
			}
		}
	}

	public void initializeOnline() throws IOException, NotBoundException {

		game = new RisikoGame(PlayersList.getPlayers(), OnlineSceneController.terrFile, OnlineSceneController.continentsFile, OnlineSceneController.missions);

		if(OnlineSceneController.amIaServer) {
			//Codice da eseguire se sono un server (aggiungo metodi allo stub)
			RemotePlay playStub = (RemotePlay) UnicastRemoteObject.exportObject(this, 1);
			System.setProperty("java.rmi.server.hostname", "192.168.1.107");
			OnlineSceneController.registry.rebind("Play", playStub);
		}


		RemotePlay playStub = (RemotePlay) UnicastRemoteObject.exportObject(this, 1);
		System.setProperty("java.rmi.server.hostname", "192.168.1.107");
		OnlineSceneController.registry.rebind("Play", playStub);

		if(!OnlineSceneController.amIaServer) {
			//Codice da eseguire se sono un client (fetch da stub)
			playStub = (RemotePlay) OnlineSceneController.registry.lookup("Play");
		}

		File file = new File(OnlineSceneController.map);
		Image temp = new Image(file.toURI().toString());
		map.setImage(temp);

		File img = new File(OnlineSceneController.territories);
		BufferedImage image = ImageIO.read(img);

		territoryLabel.setOpacity(0);

		mappa = ImageAssets.imageProcess(image, game.getTerritories());

		wImage = genWritableMap();
		map.setImage(wImage);

		mappaImgTanks = new HashMap<Territory, territoryStatus>();
		initTanks();

		executors = new HashMap<GAME_PHASE, FunctionExecutor>();
		initExecutors();

		statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
		setPlayerLabel();
		setPlayerStatus();
		endTurn.setDisable(true);
		nextPhase.setDisable(true);
		nextPhase.setText("POSIZIONAMENTO");
	}
	
	/**
     * Manages every movement of the mouse controlled by the player
     * @param e is the event
     */
	public void mouseMoved(MouseEvent e) {
		
		int x =  (int)e.getX();
		int y =  (int)e.getY();
		
		executors.get(game.getGamePhase()).executeMove(x, y);
		
	}

	/**
     * Manages every click of the mouse 
     * @param e is the event
     * @throws IOException
     */
	public void mouseClicked(MouseEvent e) throws IOException {
		
		executors.get(game.getGamePhase()).executeClick();

	}
	
	/**
     * Loads the victory scene when the mission is completed
     * @throws IOException
     */
	public void missionControl(){
		if (game.verifyMission() == true) {
			try {
				windowLoader("view/fxmls/MissionCompletedScene.fxml", "Vittoria", true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
     * Loads the Card scene when the button cardButton is pressed
     * @param e is the event
     * @throws IOexception
     */

	public void cardButtonPressed(ActionEvent e) throws IOException {
		windowLoader("view/fxmls/OnlineSelectCardScene.fxml", "Carte", false);
	}
	
	/**
     * Loads the Mission scene when the button missionButton is pressed
     * @param e is the event
     * @throws IOexception
     */
	public void missionButtonPressed(ActionEvent e) throws IOException {
		windowLoader("view/fxmls/MissionScene.fxml", "Missione", false);
	}
	
	/**
     * Loads the Menu scene when the button menuButton is pressed
     * @param e is the event
     * @throws IOexception
     */
	public void menuPressed(ActionEvent e) throws IOException {
		windowLoader("view/fxmls/MenuScene.fxml", "Menu", false);
	}
	
	/**
	 * Loads the attack scene when the attacker and defender are chosen
	 * @throws IOException
	 */
	public void attackerAndDefenderChosen (){
		try {
			windowLoader("view/fxmls/AttackScene.fxml", "Attacco", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the Move scene when entered the "FINALMOVE" game phase
	 * @throws IOException
	 */
	public void moveSceneLoader(){
		try {
			windowLoader("view/fxmls/MoveScene.fxml", "Spostamento", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts a new game
	 * @throws IOException
	 */
	public void newGame() throws IOException {
		Parent playerSceneParent= FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/NewPlayerScene.fxml"));
		Scene playerScene = new Scene(playerSceneParent);
		Stage window = (Stage)(gamePane).getScene().getWindow();
		window.setScene(playerScene);
		window.show();
	}
	
	/**
	 * Switches to the next phase when nextPhaseButton is pressed
	 * @param e is the event
	 */
	public void nextPhasePressed(ActionEvent e) {
		nextPhase();
	}
	
	/**
	 * Switches to the next turn when the endTurnButton is pressed
	 * @param e is the event
	 * @throws InterruptedException
	 */
	public void endTurnPressed(ActionEvent e) throws InterruptedException {
		nextTurn();
	}

	/**
	 * Switches the game turn to the next one
	 */
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
	
	/**
	 * Switches the game phase to the next one
	 */
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
	
	/**
	 * Returns the exact color of a Player
	 * @param p is the player
	 * @return color
	 */
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
	
	/**
	 * Fills the TextArea with the name of the player of the current turn
	 */
	public void setPlayerLabel() {
		turnLabel.setTextFill(returnPlayerColor(game.getCurrentTurn()));
		turnLabel.setText(game.getCurrentTurn().getName());
	}
	
	/**
	 * Changes the color of the image
	 * @param list is the list of all the Pixels of the image
	 */
	public void changeColor(ArrayList<Pixel> list) {
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
	
	/**
	 * Returns the text about a territory
	 * @param t is the territory
	 * @return String
	 */
	private String territoryText(Territory t) {
		return "Territorio: " + t.getName();
	}
	
	/**
	 * Generates a writable image
	 * @return WritableImage
	 */
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
	
	/**
	 * Initializes the position of each tank image on the map image
	 * @throws IOException
	 */
	private void initTanks() throws IOException {
		ArrayList<Pixel> posList;

		if(OnlineSceneController.isOnlineMultiplayer == true) {
			posList = fileH.addPosizione(OnlineSceneController.terrFile);
		}
		else {
			posList = fileH.addPosizione(PlayerSceneController.terrFile);
		}

		
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
	
	/**
	 * Returns the path of the coloured tank images 
	 * @param t is the territory on which the coloured tank image will be displayed
	 * @return String
	 */
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
	
	/**
	 * Returns the player of the current turn
	 * @return Player
	 */	
	public Player getCurrentPlayer() {
		return game.getCurrentTurn();
	}
	
	/**
	 * Fills the TextArea with a specified text
	 */
	public void setStatusBar() {
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
	
	/**
	 * Sets the current number of tanks,territories and continents owned by a player
	 */
	public void setPlayerStatus() {
		Integer tmp;
		tmp = game.getPlayer(game.getCurrentTurn()).getTanks();
		plTanks.setText(tmp.toString());
		tmp = game.getCurrentTurn().getTerritories();
		plTerritories.setText(tmp.toString());
		tmp = game.getCurrentTurn().getContinents();
		plContinents.setText(tmp.toString());
	}
	
	/**
	 * Updates the number and colour of the tanks after a battle
	 */
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
	
	/**
	 * Returns the current game
	 * @return game
	 */
	public RisikoGame getGame() {
		return game;
	}
	
	/**
	 * Sets the selected territory
	 * @param t is the territory
	 */
	public void setSelTerritory(Territory t) {
		territorySelected = t;
	}
	
	/**
	 * Sets two selected territories
	 * @param t1 is the first territory
	 * @param t2 is the second territory
	 */
	public void setTerritory12(Territory t1, Territory t2) {
		territory1 = t1;
		territory2 = t2;
	}
	
	/**
	 * Sets a specified territory
	 * @param t is the territory
	 */
	public void setTerritory2(Territory t) {
		territory2 = t;
	}
	
	/**
	 * Method that allows to load a scene in a new window
	 * @param scene is the path of the scene to load
	 * @param title is the title of the window
	 * @param cantclose specifies if the window can be closed until an event occurs
	 * @throws IOException
	 */
	private void windowLoader(String scene, String title, boolean cantclose) throws IOException{
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
	
	/**
	 * Initalizes everything for the first turn
	 */
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
	
	/**
	 * Manages the click of the mouse during the reinforcement game phase, it's called only from AI
	 * @throws IOException
	 */
	public void reinforcementClick() throws IOException {
		placeTank();
		setStatusBar();
		setPlayerStatus();

		if(game.getCurrTurnBonusTanks() == 0) {
			nextPhase();
		}
	}
	
	/**
	 * Loads a window showing all the actions made by AI
	 * @throws IOException
	 */
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
	
	private void initExecutors() {
		executors.put(GAME_PHASE.FIRSTTURN, new FirstturnExecutor());
		executors.put(GAME_PHASE.REINFORCEMENT, new ReinforcementExecutor());
		executors.put(GAME_PHASE.BATTLE, new BattleExecutor());
		executors.put(GAME_PHASE.FINALMOVE, new FinalmoveExecutor());
	}
	
	
	public ArrayList<Territory> getTerritories() {
		return game.getTerritories();
	}
	
	public ArrayList<Pixel> getPixelMap(Territory t){
		return mappa.get(t);
	}
	
	public void setTerritoryLabel(int opacity, Territory t) {
		territoryLabel.setOpacity(opacity);
		territoryLabel.setText(territoryText(t));
	}
	
	public void resetImage() {
		map.setImage(wImage);
	}
	
	public Territory getTerritory1() {
		return territory1;
	}
	
	public Territory getTerritory2() {
		return territory2;
	}
	
	public Territory getSelTerr() {
		return territorySelected;
	}
	
	public void placeTank() {
		game.getCurrentTurn().placeTank(1);
		game.addTerritoryTanks(territorySelected);
		missionControl();
		Integer n = game.getTerritory(territorySelected).getTanks();
		mappaImgTanks.get(territorySelected).getNumber().setText(n.toString());
	}
	
	public void firstPhaseEnded() {
		if(game.firstPhaseEnded()) {
			nextPhase();
		}
	}

	@Override
	public void callCard() throws IOException {
		cardButtonPressed(new ActionEvent());
	}

	/*Metodi JAVA RMI*/

	public void remoteCardCaller() throws IOException {
		playStub.callCard();
	}
}