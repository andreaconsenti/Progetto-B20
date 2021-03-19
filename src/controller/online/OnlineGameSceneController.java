package controller.online;

import controller.PlayerSceneController;
import controller.RemoteJoin;
import controller.RemotePlay;
import controller.online.mouseFuction.*;
import controller.online.OnlineAttackSceneController;
//import controller.mouseFunction.*;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import model.entities.COLOR;
import model.entities.Player;
import model.entities.PlayersList;
//funz
//import model.entities.RisikoGame;
import model.entities.online.Attacco;
import model.entities.online.RisikoGame;

//funz
//import model.entities.RisikoGame.GAME_PHASE;
import model.entities.online.RisikoGame.GAME_PHASE;
import model.entities.Territory;
import model.entities.online.Update;
import model.util.FileHandler;
import model.util.ImageAssets;
import model.util.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    @FXML
    private TextField myColorLabel;


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

    public static Territory lastServerTerritory;
    //lista territori aggiornati (es +4 bonus)
    public static ArrayList<Territory> serverTerritoryList = new ArrayList<>();

    private HashMap<GAME_PHASE, FunctionExecutor> executors;


    private static OnlineGameSceneController instance;

    private static RemotePlay playStub;

    private boolean serverTerrStatus;

    private boolean forzaReinforcement;

    protected static boolean nextClient;

    private Player antecPlayer = new Player("FITTIZIO", COLOR.PINK, false);

    private boolean bandiera, updatedFromList;

    protected static ArrayList<Attacco> myAttacks = new ArrayList<>();

    //nuove variabili di fase
    protected static boolean serverAttackClosed; //se server ha attaccato = true;
    protected static int[] serverAtkResults;
    protected static int[] serverDefResults;
    protected static int serverAtkNumber;
    protected static int serverDefNumber;
    protected static Territory serverAtkTerritory;
    protected static Territory serverDefTerritory;

    protected static boolean serverTurnClosed; //se server finisce turno = true


    protected static int serverAtkNewTankNum;
    protected static int serverDefNewTankNum;


    protected static Territory clientAtk;
    protected static Territory clientDef;
    protected static int clientAtkTanks;
    protected static int clientDefTanks;

    private boolean moved;

    ArrayList<Update> updates = new ArrayList<>();

    private int lastUpdateSize = 0;

    private String realNextTurn = new String();


    /**
     * Sets the instance to this instance of GameSceneController
     */
    public OnlineGameSceneController() {
        instance = this;
    }

    /**
     * Instance getter
     *
     * @return instance
     */
    public static OnlineGameSceneController getInstance() {
        return instance;
    }


    private class territoryStatus implements Serializable {
        private ImageView image;
        private Label number;

        /**
         * Constructor of a territory linked with its image and its name
         *
         * @param img is the image of the territory
         * @param lbl is the number of the territory that corresponds with the name
         */
        public territoryStatus(ImageView img, Label lbl) {
            setImage(img);
            setNumber(lbl);
        }

        /**
         * Returns the number of the territory
         *
         * @return number
         */
        public Label getNumber() {
            return number;
        }

        /**
         * Sets the number of the territory
         *
         * @param number is the number
         */
        public void setNumber(Label number) {
            this.number = number;
        }

        /**
         * Returns the image of the territory
         *
         * @return image
         */
        public ImageView getImage() {
            return image;
        }

        /**
         * Sets the image of the territory
         *
         * @param image is the image
         */
        public void setImage(ImageView image) {
            this.image = image;
        }

    }

    /**
     * Initializes the controller
     *
     * @throws NumberFormatException
     * @throws IOException
     */
    public void initialize() throws NumberFormatException, IOException, NotBoundException {
        if (OnlineSceneController.isOnlineMultiplayer == true) {
            initializeOnline();
        } else {
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

        for (Player p : PlayersList.getPlayers()) {
            if (p.isAI()) {
                aiRecap();
                break;
            }
        }
    }

    public void initializeOnline() throws IOException, NotBoundException {

        if (OnlineSceneController.amIaServer) {
            game = new RisikoGame(PlayersList.getPlayers(), OnlineSceneController.terrFile, OnlineSceneController.continentsFile, OnlineSceneController.missions);
            //Codice da eseguire se sono un server (aggiungo metodi allo stub)
            RemotePlay playStub = (RemotePlay) UnicastRemoteObject.exportObject(this, 1);
            //Per rete pavia mettere
            //			System.setProperty("java.rmi.server.hostname", "192.168.1.107");
            System.setProperty("java.rmi.server.hostname", "192.168.1.104");
            OnlineSceneController.registry.rebind("Play", playStub);
        }

        if (OnlineSceneController.amIaClient) {
            playStub = (RemotePlay) OnlineSceneController.registry.lookup("Play");
            game = playStub.getCurrentGame();
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
        myColorLabel.setText(OnlineSceneController.myColor);

        if (OnlineSceneController.amIaClient) {
            playStub = (RemotePlay) OnlineSceneController.registry.lookup("Play");
            //game = playStub.getCurrentGame();
        }
    }

    /**
     * Manages every movement of the mouse controlled by the player
     *
     * @param e is the event
     */
    public void mouseMoved(MouseEvent e) {

        int x = (int) e.getX();
        int y = (int) e.getY();

        executors.get(game.getGamePhase()).executeMove(x, y);

    }

    /**
     * Manages every click of the mouse
     *
     * @param e is the event
     * @throws IOException
     */
    public void mouseClicked(MouseEvent e) throws IOException {

        executors.get(game.getGamePhase()).executeClick();

    }

    /**
     * Loads the victory scene when the mission is completed
     *
     * @throws IOException
     */
    public void missionControl() {
        if (game.verifyMission() == true) {
            try {
                windowLoader("view/fxmls/OnlineMissionCompletedScene.fxml", "Vittoria", true);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads the Card scene when the button cardButton is pressed
     *
     * @param e is the event
     * @throws IOexception
     */

    public void cardButtonPressed(ActionEvent e) throws IOException {
        windowLoader("view/fxmls/OnlineSelectCardScene.fxml", "Carte", false);
    }

    /**
     * Loads the Mission scene when the button missionButton is pressed
     *
     * @param e is the event
     * @throws IOexception
     */
    public void missionButtonPressed(ActionEvent e) throws IOException {
        windowLoader("view/fxmls/OnlineMissionScene.fxml", "Missione", false);
    }

    /**
     * Loads the Menu scene when the button menuButton is pressed
     *
     * @param e is the event
     * @throws IOexception
     */
    public void menuPressed(ActionEvent e) throws IOException {
        windowLoader("view/fxmls/MenuScene.fxml", "Menu", false);
    }

    /**
     * Loads the attack scene when the attacker and defender are chosen
     *
     * @throws IOException
     */
    public void attackerAndDefenderChosen() {
        try {
            windowLoader("view/fxmls/OnlineAttackScene.fxml", "Attacco", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Loads the Move scene when entered the "FINALMOVE" game phase
     *
     * @throws IOException
     */
    public void moveSceneLoader() {
        try {
            windowLoader("view/fxmls/OnlineMoveScene.fxml", "Spostamento", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Starts a new game
     *
     * @throws IOException
     */
    public void newGame() throws IOException {
        Parent playerSceneParent = FXMLLoader.load(getClass().getClassLoader().getResource("view/fxmls/NewPlayerScene.fxml"));
        Scene playerScene = new Scene(playerSceneParent);
        Stage window = (Stage) (gamePane).getScene().getWindow();
        window.setScene(playerScene);
        window.show();
    }

    /**
     * Switches to the next phase when nextPhaseButton is pressed
     *
     * @param e is the event
     */
    public void nextPhasePressed(ActionEvent e) {
        //Si entra quando si preme Spostamento in alto a dx
        nextPhase();
        serverAttackClosed = true;
    }

    /**
     * Switches to the next turn when the endTurnButton is pressed
     *
     * @param e is the event
     * @throws InterruptedException
     */
    public void endTurnPressed(ActionEvent e) throws InterruptedException {
        //try {
            //System.out.println(Inet4Address.getLocalHost().getHostAddress());
        //} catch (UnknownHostException e1) {}

        if (OnlineSceneController.amIaServer) {
            serverTurnClosed = true;
            System.out.println("Turno server chiuso =" + serverTurnClosed);
        }
        if(OnlineSceneController.amIaClient && !OnlineSceneController.amIaServer) {
            Iterator<Attacco> i = myAttacks.iterator();
            while(i.hasNext()) {
                Attacco attacco = i.next();
                try {
                    playStub.remoteAttack(attacco.getAttaccante(), attacco.getDifensore(), attacco.getCarriAggiornatiAttaccante(),attacco.getCarriAggiornatiDifensore(), attacco.getAttaccante().getOwner().getColor(), attacco.getDifensore().getOwner().getColor());
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                try {
                    playStub.remoteChangeTurn();
                }   catch (RemoteException ect) {
                    System.out.println("problema qui");
                }

            }
            myAttacks.clear();
        }

        //potrebbe essere qui il problema
        //myAttacks.clear();
        nextTurn();
    }



    /**
     * Switches the game turn to the next one
     */
    public void nextTurn() {

        missionControl();

        if(OnlineSceneController.amIaServer) {
            //altrimenti client non preleva correttamente atk e def da server al 2 turno
            serverAttackClosed = false;
        }

        game.nextTurn();

        realNextTurn = game.getCurrentTurn().getColor().toString();
        //provo a fetchare arraylist qui

        try {
            if (!OnlineSceneController.amIaServer && OnlineSceneController.amIaClient
                    && !getCurrentPlayer().getColor().equals(OnlineSceneController.myColor)) {
                if (playStub.getBandiera() && !updatedFromList) {
                    //attenzione non deve essere 4 ma numero di bonus tank che ha avversario
                    while (playStub.getUpdatedTerritory().size() != 4) {
                        System.out.println("Server ha posizionato nTank = " + playStub.getUpdatedTerritory().size());
                        Thread.sleep(1000);
                    }
                    System.out.println("ottengo arraylist da server...");
                    serverTerritoryList = playStub.getUpdatedTerritory();
                    System.out.println("arraylist scaricato");
                    //qui chiamare funzione che aggiorna uno per uno i territori
                    Iterator<Territory> lista = serverTerritoryList.iterator();
                    while (lista.hasNext()) {
                        updateGuiFromList(lista.next());
                    }
                    System.out.println("Allineamento eseguito");
                    //resetto mio arraylist scaricato
                    resetArrayList();
                    playStub.resetArrayList();
                    bandiera = false;
                    updatedFromList = true;

                    //mod
                    while(!playStub.getServerAttackClosed()) {
                        System.out.println("server non ha attaccato");
                        Thread.sleep(1000);
                    }

                    System.out.println("Server ha attaccato. Scarico info attacco...");
                    //processNewClientTankPostAtk();
                    //clientUpdateGuiPostAtk();


                    serverTurnClosed = playStub.getServerTurnClosed();

                    while(serverTurnClosed == false) {
                        System.out.println("Attendo fine turno server...");
                        Thread.sleep(1500);
                        serverTurnClosed = playStub.getServerTurnClosed();
                    }

                    Attacco[] attacchi = playStub.getServerAttacks();
                    for (Attacco a: attacchi
                         ) {
                        processServerAttack(a);
                    }


                    /*Questa porzione abilita il secondo turno del client per rinforzo-attacco*/
                    forzaReinforcement = true;
                    nextClient = true;
                    nextPhase();
                    System.out.println("Fase ricezione terminata in " + game.getGamePhase());

                    playStub.forceClosePostMove();
                }
            }
        } catch (IOException | InterruptedException exception) {

        }

        //sta parte la devo skippare forse se sono client e non è mio turno
        if (game.getGamePhase() == GAME_PHASE.FIRSTTURN) {
            int i = 0;
            while (game.getCurrentTurn().getBonusTanks() == 0) {
                if (i == PlayersList.players.length) {
                    break;
                }
                game.nextTurn();
                i++;
            }
        }
        if (!(game.getGamePhase() == GAME_PHASE.FIRSTTURN))
            nextPhase();

        setStatusBar();
        setPlayerStatus();
        setPlayerLabel();
        territory1 = null;
        territory2 = null;

        if (game.getGamePhase() != GAME_PHASE.FIRSTTURN && game.getCurrentTurn().isAI()) {
            game.getCurrentTurn().playTurn();
        }

        //permette al client di impostare il giusto turno al primo giro di rinforzo
        if(game.firstPhaseEnded()) {
            game.setGamePhase(GAME_PHASE.FINALMOVE);
        }

        if(forzaReinforcement && game.getGamePhase() == GAME_PHASE.BATTLE) {
            game.nextTurn();
            game.setGamePhase(GAME_PHASE.REINFORCEMENT);
            game.giveBonus(game.getCurrentTurn());

            /*Dovrei inserirlo a inizio placeTank()*/
            setStatusBar();
            setPlayerStatus();
            setPlayerLabel();
        }
    }

    /**
     * Switches the game phase to the next one
     */
    public void nextPhase() {
        switch (game.getGamePhase()) {
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
     *
     * @param p is the player
     * @return color
     */
    private Color returnPlayerColor(Player p) {
        switch (p.getColor()) {
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
     *
     * @param list is the list of all the Pixels of the image
     */
    public void changeColor(ArrayList<Pixel> list) {
        WritableImage tempWImage = genWritableMap();
        for (Pixel p : list) {
            Color color = pixelReader.getColor(p.getX(), p.getY());
            double r = color.getRed() / 1.5;
            double g = color.getGreen() / 1.5;
            double b = color.getBlue() / 1.5;
            int red = (int) (r * 255);
            int green = (int) (g * 255);
            int blue = (int) (b * 255);
            color = Color.rgb(red, green, blue);
            PixelWriter pxlWriter = tempWImage.getPixelWriter();
            pxlWriter.setColor(p.getX(), p.getY(), color);
        }
        map.setImage(tempWImage);
    }

    /**
     * Returns the text about a territory
     *
     * @param t is the territory
     * @return String
     */
    private String territoryText(Territory t) {
        return "Territorio: " + t.getName();
    }

    /**
     * Generates a writable image
     *
     * @return WritableImage
     */
    private WritableImage genWritableMap() {
        pixelReader = map.getImage().getPixelReader();
        WritableImage wImg = new WritableImage((int) map.getImage().getWidth(), (int) map.getImage().getHeight());
        pixelWriter = wImg.getPixelWriter();
        for (int y = 0; y < map.getImage().getHeight(); y++) {

            for (int x = 0; x < map.getImage().getWidth(); x++) {

                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }

        }
        return wImg;
    }

    /**
     * Initializes the position of each tank image on the map image
     *
     * @throws IOException
     */
    private void initTanks() throws IOException {
        ArrayList<Pixel> posList;

        if (OnlineSceneController.isOnlineMultiplayer == true) {
            posList = fileH.addPosizione(OnlineSceneController.terrFile);
        } else {
            posList = fileH.addPosizione(PlayerSceneController.terrFile);
        }


        int i = 0;
        for (Pixel p : posList) {

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
            tanksNumber.relocate(p.getX(), p.getY());
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
     *
     * @param t is the territory on which the coloured tank image will be displayed
     * @return String
     */
    private String getTankPath(Territory t) {
        Player tempPlayer = t.getOwner();

        switch (tempPlayer.getColor()) {
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
     *
     * @return Player
     */
    public Player getCurrentPlayer() {
        return game.getCurrentTurn();
    }

    /**
     * Fills the TextArea with a specified text
     */
    public void setStatusBar() {
        switch (game.getGamePhase()) {
            case FIRSTTURN:
                statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
                break;
            case REINFORCEMENT:
                statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un Territorio sul quale posizionare un'armata" + "\n" + "Hai ancora " + game.getCurrentTurn().getBonusTanks() + " armate da posizionare.");
                break;
            case BATTLE:
                if (territory1 == null) {
                    statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un territorio con cui attaccare");
                } else if (territory2 == null) {
                    statusBar.setText("Territorio selezionato: " + territory1.getName() + "\n" + game.getCurrentTurn().getName() + ": seleziona un territorio da attaccare");
                } else if ((territory1 != null) && (territory2 != null)) {
                    statusBar.setText("Attacco in corso");
                }
                break;
            case FINALMOVE:
                if (territory1 == null) {
                    statusBar.setText(game.getCurrentTurn().getName() + ": seleziona un territorio dal quale spostare armate");
                } else if (territory2 == null) {
                    statusBar.setText("Territorio selezionato: " + territory1.getName() + "\n" + game.getCurrentTurn().getName() + ": seleziona un territorio sul quale spostare armate");
                } else if ((territory1 != null) && (territory2 != null)) {
                    statusBar.setText("Spostamento");
                }
                break;
        }
    }

    /**
     * Sets the current number of tanks,territories and continents owned by a player
     */
    public void setPlayerStatus() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Integer tmp;
                tmp = game.getPlayer(game.getCurrentTurn()).getTanks();
                plTanks.setText(tmp.toString());
                tmp = game.getCurrentTurn().getTerritories();
                plTerritories.setText(tmp.toString());
                tmp = game.getCurrentTurn().getContinents();
                plContinents.setText(tmp.toString());
            }
        });

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
     *
     * @return game
     */
    public RisikoGame getGame() {
        return game;
    }

    /**
     * Sets the selected territory
     *
     * @param t is the territory
     */
    public void setSelTerritory(Territory t) {
        territorySelected = t;
    }

    /**
     * Sets two selected territories
     *
     * @param t1 is the first territory
     * @param t2 is the second territory
     */
    public void setTerritory12(Territory t1, Territory t2) {
        territory1 = t1;
        territory2 = t2;
    }

    /**
     * Sets a specified territory
     *
     * @param t is the territory
     */
    public void setTerritory2(Territory t) {
        territory2 = t;
    }

    /**
     * Method that allows to load a scene in a new window
     *
     * @param scene     is the path of the scene to load
     * @param title     is the title of the window
     * @param cantclose specifies if the window can be closed until an event occurs
     * @throws IOException
     */
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
        if (game.firstPhaseEnded()) {
            nextPhase();
        }

    }

    /**
     * Manages the click of the mouse during the reinforcement game phase, it's called only from AI
     *
     * @throws IOException
     */
    public void reinforcementClick() throws IOException {
        placeTank();
        setStatusBar();
        setPlayerStatus();

        if (game.getCurrTurnBonusTanks() == 0) {
            nextPhase();
        }
    }

    /**
     * Loads a window showing all the actions made by AI
     *
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

    public ArrayList<Pixel> getPixelMap(Territory t) {
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

    public void placeTank() throws IOException {

        updates.add(new Update(game.getTerritory(territorySelected), game.getTerritory(territorySelected).getOwner(), game.getTerritory(territorySelected).getTanks()));


        game.getCurrentTurn().placeTank(1);
        game.addTerritoryTanks(territorySelected);
        missionControl();
        Integer n = game.getTerritory(territorySelected).getTanks();
        mappaImgTanks.get(territorySelected).getNumber().setText(n.toString());
        lastServerTerritory = territorySelected;
        //Se serverTerrStatus = true -> client fetcha il territorio dal server a fine turno
        serverTerrStatus = true;

        if (OnlineSceneController.amIaClient && !OnlineSceneController.amIaServer) {
            //Se sono un client e non server -> faccio il posizionamento su server con remotePlaceTankCaller()
            remotePlaceTankCaller();
        }

        //aggiunge ad arraylist di territori, i territori rafforzati da passare agli altri giocatori
        //if(getCurrentPlayer().getColor().toString().equals(OnlineSceneController.myColor) && bandiera == true)
        if (bandiera == true) {
            serverTerritoryList.add(territorySelected);
            System.out.println("FASE 2 -> AGGIUNTO");
        }


        if (OnlineSceneController.amIaClient && !OnlineSceneController.amIaServer) {
            try {
                waitForUpdate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void firstPhaseEnded() {
        if (game.firstPhaseEnded()) {
            bandiera = true;
            System.out.println("Ho impostato bandiera = " + bandiera);
            nextPhase();
        }
    }

    public void clientUpdateGuiPostAtk() throws RemoteException {
        Integer temp = OnlineGameSceneController.territory1.getTanks();
        mappaImgTanks.get(territory1).getNumber().setText(temp.toString());

        temp = OnlineGameSceneController.territory2.getTanks();
        mappaImgTanks.get(territory2).getNumber().setText(temp.toString());

    }

    public void processNewClientTankPostAtk() {
        try {
            System.out.println("Atk: " + playStub.getServerAtkTerritory());
            System.out.println("Def: " + playStub.getServerDefTerritory());

            territory1 = game.getTerritory(playStub.getServerAtkTerritory());
            territory2 = game.getTerritory(playStub.getServerDefTerritory());

            int nuovoValoreAtk = playStub.getServerAtkNewTankNum();
            int nuovoValoreDef = playStub.getServerDefNewTankNum();

            int vecchioValAtk = game.getTerritory(territory1).getTanks();
            int vecchioValDef = game.getTerritory(territory2).getTanks();


            if(nuovoValoreAtk > vecchioValAtk) {
                int tankDaAgg = nuovoValoreAtk - vecchioValAtk;
                game.getTerritory(territory1).addTanks(tankDaAgg);
            }
            if(nuovoValoreAtk < vecchioValAtk) {
                int tankDaRim = vecchioValAtk - nuovoValoreAtk;
                game.getTerritory(territory1).removeTanks(tankDaRim);
            }
            if(nuovoValoreDef > vecchioValDef) {
                int tankDaAgg = nuovoValoreDef - vecchioValDef;
                game.getTerritory(territory2).addTanks(tankDaAgg);
            }
            if(nuovoValoreDef < vecchioValDef) {
                int tankDaRim = vecchioValDef - nuovoValoreDef;
                game.getTerritory(territory2).removeTanks(tankDaRim);
            }
        }   catch (RemoteException exp) {
            exp.printStackTrace();
        }

    }

    public void processServerAttack(Attacco atkServer) {
        try {
            territory1 = game.getTerritory(atkServer.getAttaccante());
            territory2 = game.getTerritory(atkServer.getDifensore());


            int nuovoValoreAtk = atkServer.getCarriAggiornatiAttaccante();
            int nuovoValoreDef = atkServer.getCarriAggiornatiDifensore();

            int vecchioValAtk = game.getTerritory(territory1).getTanks();
            int vecchioValDef = game.getTerritory(territory2).getTanks();

            if(atkServer.getCarriAggiornatiDifensore() == 0) {
                nuovoValoreAtk = playStub.getServerTerrTank(territory1);
                nuovoValoreDef = playStub.getServerTerrTank(territory2);

                game.getTerritory(territory2).setOwner(game.getTerritory(territory1).getOwner());

                File file = new File(getTankPath(territory1));
                Image image = new Image(file.toURI().toString());
                mappaImgTanks.get(territory2).getImage().setImage(image);
            }


            if(nuovoValoreAtk > vecchioValAtk) {
                int tankDaAgg = nuovoValoreAtk - vecchioValAtk;
                game.getTerritory(territory1).addTanks(tankDaAgg);
            }
            if(nuovoValoreAtk < vecchioValAtk) {
                int tankDaRim = vecchioValAtk - nuovoValoreAtk;
                game.getTerritory(territory1).removeTanks(tankDaRim);
            }
            if(nuovoValoreDef > vecchioValDef) {
                int tankDaAgg = nuovoValoreDef - vecchioValDef;
                game.getTerritory(territory2).addTanks(tankDaAgg);
            }
            if(nuovoValoreDef < vecchioValDef) {
                int tankDaRim = vecchioValDef - nuovoValoreDef;
                game.getTerritory(territory2).removeTanks(tankDaRim);
            }

            clientUpdateGuiPostAtk();

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

    public static void remoteMoveCaller(Territory t1, Territory t2, int value) {
        try {
            playStub.remoteMove(t1, t2, value);
            if(game.getGamePhase() == GAME_PHASE.FINALMOVE) {
                playStub.remoteChangeTurn();
            }
        } catch (RemoteException ex2) {
            ex2.printStackTrace();
        }
    }


    /*Metodi JAVA RMI*/

    @Override
    public void remoteMove(Territory t1, Territory t2, int value) throws RemoteException {

        Attacco temp = new Attacco(t1, t2, t1.getTanks(), t2.getTanks());
        remoteAttack(t1,t2,t1.getTanks()-value, t2.getTanks()+value,t1.getOwner().getColor(), t2.getOwner().getColor());

    }

    @Override
    public int getServerTerrTank(Territory recTerr) throws RemoteException{
        return game.getTerritory(recTerr).getTanks();
    }

    @Override
    public void callCard() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                try {
                    cardButtonPressed(new ActionEvent());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void remoteCardCaller() throws IOException {
        playStub.callCard();
    }

    @Override
    public void remotePlaceTank(Territory remoteTerritory) throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                serverTerrStatus = false; //per fare in modo che il server debba riscegliere un territorio
                game.getCurrentTurn().placeTank(1);
                game.addTerritoryTanks(remoteTerritory);
                //Introduco territorioLocale perchè remoteTerritory ricevuto tramite RMI non è rilevato
                //come un oggetto locale e crea problemi con "mappaImgTanks.get(territorioLocale).getNumber().setText(n.toString())"
                Territory territorioLocale = game.getTerritory(remoteTerritory);
                missionControl();
                Integer n = game.getTerritory(remoteTerritory).getTanks();
                mappaImgTanks.get(territorioLocale).getNumber().setText(n.toString());

                if(bandiera) {
                    return;
                }

                setStatusBar();
                setPlayerStatus();
                nextTurn();

                if (game.getCurrTurnBonusTanks() == 0) {
                    bandiera = true;
                    nextPhase();
                }

            updates.add(new Update(game.getTerritory(territorioLocale), game.getTerritory(territorioLocale).getOwner(), game.getTerritory(territorioLocale).getTanks()));

            }
        });
    }

    public void remotePlaceTankCaller() throws IOException {
        playStub.remotePlaceTank(getSelTerr());
        playStub.getBandiera();
        if (bandiera) {
            nextPhase();
        }
    }

    public void clientPlaceTank(Territory territoryFromServer) throws IOException {

        game.getCurrentTurn().placeTank(1);
        game.addTerritoryTanks(territoryFromServer);
        Territory territorioLocale = game.getTerritory(territoryFromServer);
        missionControl();
        Integer n = game.getTerritory(territorioLocale).getTanks();
        mappaImgTanks.get(territorioLocale).getNumber().setText(n.toString());


        setStatusBar();
        setPlayerStatus();

        //mod 19.01.21
        if (game.getCurrTurnBonusTanks() == 0) {
            //nextPhase();
            nextTurn();
        }


    }

    @Override
    public RisikoGame getCurrentGame() {
        return game;
    }

    public void waitForUpdate() throws InterruptedException, IOException {
        if (!playStub.getBandiera()) {
            boolean terrSelFromServer = false;
            while (!terrSelFromServer) {
                Thread.sleep(1000);
                terrSelFromServer = playStub.getServerTerrStatus();
            }

            //fetch territorio singolo
            lastServerTerritory = playStub.getLastServerTerritory();
            clientPlaceTank(lastServerTerritory);

        }

    }

    public void updateGuiFromList(Territory t) {
        game.getCurrentTurn().placeTank(1);
        game.addTerritoryTanks(t);
        Territory territorioLocale = game.getTerritory(t);
        missionControl();
        Integer n = game.getTerritory(territorioLocale).getTanks();
        mappaImgTanks.get(territorioLocale).getNumber().setText(n.toString());
    }

    public void processaUpdate() throws RemoteException {
        updates = playStub.getUpdate();
        Iterator<Update> temp = updates.iterator();
        while(temp.hasNext()) {
            Update tmpUpdate = temp.next();
            System.out.println(tmpUpdate.getTerritory());
        }
    }

    @Override
    public Territory getLastServerTerritory() throws RemoteException {
        return lastServerTerritory;
    }

    @Override
    public boolean getServerTerrStatus() throws IOException {
        return serverTerrStatus;
    }

    @Override
    public ArrayList<Territory> getUpdatedTerritory() throws RemoteException {
        return serverTerritoryList;
    }

    @Override
    public boolean getBandiera() throws IOException {
        return bandiera;
    }

    @Override
    public void resetArrayList() {
        serverTerritoryList.clear();
    }

    @Override
    public boolean getServerTurnClosed() throws RemoteException {
        return serverTurnClosed;
    }


    @Override
    public int[] getServerAtkResults() throws RemoteException {
        System.out.println("servAtkRes = " + serverAtkResults);
        return serverAtkResults;
    }

    @Override
    public int[] getServerDefResults() throws RemoteException {
        System.out.println("servDefRes = " + serverDefResults);
        return serverDefResults;
    }

    @Override
    public int getServerAtkNumber() throws RemoteException {
        System.out.println("servAtkNum = " + serverAtkNumber);
        return serverAtkNumber;
    }

    @Override
    public int getServerDefNumber() throws RemoteException {
        System.out.println("servDefRes = " + serverDefResults);
        return serverDefNumber;
    }

    @Override
    public Territory getServerAtkTerritory() throws RemoteException {
        return serverAtkTerritory;
    }

    @Override
    public Territory getServerDefTerritory() throws RemoteException {
        return serverDefTerritory;
    }

    @Override
    public boolean getServerAttackClosed() throws RemoteException {
        return serverAttackClosed;
    }

    @Override
    public int getServerAtkNewTankNum() throws RemoteException {
        return serverAtkNewTankNum;
    }

    @Override
    public int getServerDefNewTankNum() throws RemoteException {
        return serverDefNewTankNum;
    }

    @Override
    public void remoteSetOwner() {

    }


    @Override
    public void remoteAttack(Territory terrCl1, Territory terrCl2, int nuovoValAtk, int nuovoValDef, COLOR c1, COLOR c2) {



        Territory t1 = game.getTerritory(terrCl1);
        Territory t2 = game.getTerritory(terrCl2);

        if(c1 == c2) {
            System.out.println("rilevata conquista o spostamento");
            //invoco metodo che ricava i miei player in base al colore
            System.out.println(game.getPlayerByColor(c1).getColorName() + " e " + game.getPlayerByColor(c2).getColorName());

            game.getTerritory(t1).setOwner(game.getPlayerByColor(c1));
            game.getTerritory(t2).setOwner(game.getPlayerByColor(c2));

        }


        int vecchioValAtk = game.getTerritory(terrCl1).getTanks();
        int vecchioValDef = game.getTerritory(terrCl2).getTanks();

        if(nuovoValAtk > vecchioValAtk)  {
            int tankDaAgg = nuovoValAtk - vecchioValAtk;
            game.getTerritory(t1).addTanks(tankDaAgg);
        }
        if(nuovoValAtk < vecchioValAtk) {
            int tankDaRim = vecchioValAtk - nuovoValAtk;
            game.getTerritory(t1).removeTanks(tankDaRim);
        }

        if(nuovoValDef > vecchioValDef)  {
            int tankDaAgg = nuovoValDef - vecchioValDef;
            game.getTerritory(t2).addTanks(tankDaAgg);
        }
        if(nuovoValDef < vecchioValDef) {
            int tankDaRim = vecchioValDef - nuovoValDef;
            game.getTerritory(t2).removeTanks(tankDaRim);
        }

        //aggiorno interfaccia server dopo attacco di client

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                File file = new File(getTankPath(t1));
                Image image = new Image(file.toURI().toString());
                mappaImgTanks.get(t1).getImage().setImage(image);
                file = new File(getTankPath(t2));
                image = new Image(file.toURI().toString());
                mappaImgTanks.get(t2).getImage().setImage(image);

                Integer temp = t1.getTanks();
                mappaImgTanks.get(t1).getNumber().setText(temp.toString());
                temp = t2.getTanks();
                mappaImgTanks.get(t2).getNumber().setText(temp.toString());

            }
        });
        game.getCurrentTurn().giveBonusTanks(-(game.getCurrentTurn().getBonusTanks()));

        //resetto serverTurnClosed per preparare il client a ricevere prossime mosse
        serverTurnClosed = false;

        game.setGamePhase(GAME_PHASE.FINALMOVE);

    }

    @Override
    public void remoteChangeTurn() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nextTurn();
            }
        });
    }

    @Override
    public Attacco[] getServerAttacks() throws RemoteException {
        Attacco[] attacchi = new Attacco[myAttacks.size()];
        Iterator<Attacco> it = myAttacks.iterator();
        for(int i = 0; it.hasNext(); i++) {
            attacchi[i] = it.next();
            System.out.println(attacchi[i]);
        }
        myAttacks.clear();
        return attacchi;
    }

    @Override
    public void forceClosePostMove() throws RemoteException {
        System.out.println("chiudo turno server");
        serverTurnClosed = false;
    }

    @Override
    public ArrayList<Update> getUpdate() throws RemoteException {
        return updates;
    }

    @Override
    public String getCurrentColor() throws RemoteException {
        return game.getCurrentTurn().getColor().toString();
    }

    @Override
    public String getRealCurrentColor() throws RemoteException {
        return realNextTurn;
    }
}