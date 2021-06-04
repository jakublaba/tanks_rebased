import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class Controller {
    //Graphic elements:
    @FXML
    private Button startButton;
    @FXML
    private Button backButton;
    @FXML
    private TabPane tabPane;
    public static Pane layerPane;
    public Pane finishPane;
    private Pane pausePane;
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int gameTime;
    private GameBoard gameBoard;
    private Stage primaryStage;
    //Movement:
    public static boolean leftBarrelUpPressed;
    public static boolean leftBarrelDownPressed;
    public static boolean leftMoveUpPressed;
    public static boolean leftMoveDownPressed;
    public static boolean rightMoveUpPressed;
    public static boolean rightMoveDownPressed;
    public static boolean rightBarrelDownPressed;
    public static boolean rightBarrelUpPressed;
    //Shooting:
    public static long lastTimeOfLeftPlayerShot, lastTimeOfRightPlayerShot;
    public static boolean leftPlayerAllowedToShoot, rightPlayerAllowedToShoot;
    public static boolean leftPlayerShootPressed;
    public static boolean rightPlayerShootPressed;

    @FXML
    private void initialize(){
        gameBoard = new GameBoard();
        if(tabPane != null) {
            setGamePropertiesTab();
            setManualConfigurationTab();
            setControlsTab();
        }
    }

    @FXML
    private void startButtonPressed() {
        Stage Menu = (Stage) startButton.getScene().getWindow();
        Menu.hide();
        Pane gameField = new Pane();
        primaryStage = new Stage();
        BorderPane root = new BorderPane();
        layerPane = new Pane();
        layerPane.getChildren().add(gameField);
        root.setCenter(layerPane);
        Scene scene = new Scene(root, GameSettings.WindowWidth, GameSettings.WindowHeight);
        primaryStage.setTitle("Tanks");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        setGameBoard();
        scene.setOnKeyPressed(key-> {
            if(key.getCode().equals(GameSettings.Pause)){
                gameLoop.stop();
                pausePane.setVisible(true);
            }
            ControllerSetter.setPressedKey(key.getCode());
        });
        scene.setOnKeyReleased(key-> ControllerSetter.setReleasedKey(key.getCode()));

        gameLoop = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long currentTime) {
                if (gameTime == 0) {
                    showEndPane(false);
                }
                //TIMER
                if(currentTime - lastTime >= 1000000000) {
                    ControllerSetter.updateTimer(timerLabel, gameTime);
                    lastTime = currentTime;
                    gameTime--;
                }
                //TANKS
                gameBoard.updateTankPosition(layerPane);
                //GAME BOARD
                if(gameBoard.updateGame(currentTime, layerPane)){
                    showEndPane(true);
                }
                shootingControl(currentTime);
            }
        };
        gameLoop.start();
    }

    private void shootingControl(long currentTime) {
        if (currentTime - lastTimeOfLeftPlayerShot >= 1000000000 * GameSettings.BulletFrequencyLimit) {
            leftPlayerAllowedToShoot = true;
            lastTimeOfLeftPlayerShot = currentTime;
        }
        if (currentTime - lastTimeOfRightPlayerShot >= 1000000000 * GameSettings.BulletFrequencyLimit) {
            rightPlayerAllowedToShoot = true;
            lastTimeOfRightPlayerShot = currentTime;
        }
        if (gameBoard.leftPlayer.getTank().getBullets().size() >= GameSettings.BulletNumberLimit) {
            leftPlayerAllowedToShoot = false;
        }
        if (gameBoard.rightPlayer.getTank().getBullets().size() >= GameSettings.BulletNumberLimit) {
            rightPlayerAllowedToShoot = false;
        }
    }

    private void setGameBoard() {
        Line rightLine = ControllerSetter.setLine(GameSettings.WidthOfTankBorder, 0, GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line leftLine = ControllerSetter.setLine(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, 0, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line horizontalLine = ControllerSetter.setLine(GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        timerLabel = ControllerSetter.setLabel("", 0, GameSettings.WindowHeight /3);
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.1); -fx-font-weight: bold;");
        timerLabel.setVisible(false);
        layerPane.setId("gameBackground");
        layerPane.getStylesheets().add("css/backgrounds.css");
        setPausePane(layerPane);
        ControllerSetter.addChildren(layerPane, rightLine, leftLine, horizontalLine, timerLabel);
        Bomb.draw(layerPane);
        gameTime = (int)(GameSettings.GameTime);
        PlayerInfo.setErrorList(layerPane);
    }

    @FXML
    private void settingsButtonPressed() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings.fxml")));
            Stage stage = new Stage();
            stage.setTitle("SnakeFX - Settings");
            stage.setScene(new Scene(root, GameSettings.WindowWidth, GameSettings.WindowHeight));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitButtonPressed() {
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void backButtonAction() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private void setGamePropertiesTab(){
        Tab tabGameProperties = tabPane.getTabs().get(0);
        Pane mainPane = ControllerSetter.setPane(0, 0 ,800, "mainBackground", "css/backgrounds.css");
        ControllerSetter.setGamePropertiesTabLabels(mainPane);
        ControllerSetter.setGamePropertiesTabControlElements(mainPane);
        CheckBox checkBox = ControllerSetter.setCheckBox(500, 350, "css/checkBox.css");
        checkBox.setOnMouseReleased(mouseEvent -> GameSettings.MakeScreenshot = checkBox.isSelected());
        ControllerSetter.addChildren(mainPane, checkBox);
        tabGameProperties.setContent(mainPane);
    }

    private void setManualConfigurationTab(){
        Tab tabConfiguration = tabPane.getTabs().get(1);
        ScrollPane scrollPane = ControllerSetter.setScrollPane(0, 0 , 800);
        GridPane mainGridPane = ControllerSetter.setGridPane(0, 0, 800);
        mainGridPane.getStylesheets().add("css/tabPaneButtons.css");
        List<TextField> listOfTextFields = ControllerSetter.setLineOfElements(mainGridPane);
        TextField configNameTextField = ControllerSetter.setTextField(100,0,0, "", "");
        configNameTextField.setVisible(false);
        Label configNameLabel = ControllerSetter.setLabel("Enter your config name:",0, 0, "css/tabLabel.css");
        configNameLabel.setVisible(false);
        Label successLabel = ControllerSetter.setLabel("Configuration File Saved", 0, 0, "css/tabLabel.css");
        ControllerSetter.setManualConfigurationTabButtons(mainGridPane, configNameTextField, configNameLabel, successLabel, listOfTextFields);
        mainGridPane.add(configNameLabel, 0, GameSettings.getConfigurationList().size() + 2);
        mainGridPane.add(configNameTextField, 0, GameSettings.getConfigurationList().size() + 3);
        scrollPane.setContent(mainGridPane);
        tabConfiguration.setContent(scrollPane);
    }

    private void setControlsTab(){
        Tab tabGameProperties = tabPane.getTabs().get(2);
        Pane mainPane = ControllerSetter.setPane(0, 0, 800, "mainBackground", "css/backgrounds.css");
        ControllerSetter.setControlsTabLabels(mainPane);
        ControllerSetter.setControlsTabButtons(mainPane);
        tabGameProperties.setContent(mainPane);
    }

    public void showEndPane(boolean bombCollision){
        gameLoop.stop();
        finishPane = ControllerSetter.setPane(100, 50, 600, "finishPaneBackground", "css/backgrounds.css");
        finishPane.setPrefHeight(650);
        Label finishLabel = ControllerSetter.setLabel("Time Over", 0, 30);
        if(bombCollision) {
            finishLabel.setText("Bomb Collision!");
        }
        finishLabel.setPrefWidth(600);
        finishLabel.setAlignment(Pos.CENTER);
        finishLabel.setStyle("-fx-font-size: 60 px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        PieChart pieChart = ControllerSetter.setPieChart(gameBoard);
        Button quitButton = ControllerSetter.setButton(5, 500, "Quit", "css/buttons.css");
        quitButton.setPrefWidth(290);
        quitButton.setOnAction(e -> exitButtonPressed());
        Button againButton = ControllerSetter.setButton(305, 500, "Again!", "css/buttons.css");
        againButton.setPrefWidth(290);
        againButton.setOnAction(e -> {
            primaryStage.close();
            gameBoard.removeAllCells();
            startButtonPressed();
        });
        layerPane.getChildren().clear();
        ControllerSetter.addChildren(finishPane, finishLabel, pieChart, quitButton, againButton);
        ControllerSetter.addChildren(layerPane, finishPane);
        if(GameSettings.MakeScreenshot) {
            ControllerSetter.makeScreenshot(layerPane);
        }
    }

    private void setPausePane(Pane pane) {
        pausePane = ControllerSetter.setPane(100,150, 600, "finishPaneBackground", "css/backgrounds.css");
        pausePane.setPrefHeight(300);
        pausePane.setVisible(false);
        Label pauseLabel = ControllerSetter.setLabel("Game Paused!", 20, 20);
        pauseLabel.setPrefWidth(600);
        pauseLabel.setAlignment(Pos.CENTER);
        pauseLabel.setStyle("-fx-font-size: 60 px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        Button resumeButton = ControllerSetter.setButton(400, 200, "Play", "css/buttons.css");
        resumeButton.setOnAction(e -> {
            pausePane.setVisible(false);
            gameLoop.start();
        });
        Button settingsButton = ControllerSetter.setButton(5, 200, "Settings", "css/buttons.css");
        settingsButton.setOnAction(e -> settingsButtonPressed());
        ControllerSetter.addChildren(pausePane, pauseLabel, resumeButton, settingsButton);
        ControllerSetter.addChildren(pane, pausePane);
    }

}
