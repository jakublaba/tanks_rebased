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
    private GameBoard gameBoard;
    private Stage primaryStage;
    private GameSoundPlayer gameSoundPlayer;
    private int gameTime;
    private boolean actualizationTimeRequired = false;
    private boolean gamePaused = false;
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
    private void initialize() {
        gameBoard = new GameBoard();
        if (tabPane != null) {
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
        scene.setOnKeyPressed(key -> {
            if (key.getCode().equals(GameSettings.Pause) && !gamePaused) {
                gamePaused = true;
                gameLoop.stop();
                setPausePane(layerPane);
                gameSoundPlayer.stopBackgroundSound();
            }
            GraphicElementsSetter.setPressedKey(key.getCode());
        });
        scene.setOnKeyReleased(key -> GraphicElementsSetter.setReleasedKey(key.getCode()));

        gameLoop = new AnimationTimer() {
            long lastTime = 0;

            @Override
            public void handle(long currentTime) {
                if (actualizationTimeRequired) {
                    gameBoard.actualizeTime(currentTime);
                    actualizationTimeRequired = false;
                }
                if (gameTime <= 0) {
                    showEndPane(false);
                }
                //TIMER
                if (currentTime - lastTime >= 1000000000) {
                    GraphicElementsSetter.updateTimer(timerLabel, gameTime);
                    lastTime = currentTime;
                    gameTime--;
                }
                //TANKS
                gameBoard.updateTankPosition(layerPane);
                //GAME BOARD
                if (gameBoard.updateGame(currentTime, layerPane)) {
                    List<Bullet> leftBullets = gameBoard.leftPlayer.getTank().getBullets();
                    List<Bullet> rightBullets = gameBoard.rightPlayer.getTank().getBullets();
                    for (Bullet bullet : leftBullets) {
                        bullet.eraseFromPane(layerPane);
                    }
                    for (Bullet bullet : rightBullets) {
                        bullet.eraseFromPane(layerPane);
                    }
                    leftBullets.clear();
                    rightBullets.clear();
                    showEndPane(true);
                }
                PlayerInfo.updateErrorInformation();
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
        PlayerInfo.addInformation("DEFAULT CONTROLS:");
        PlayerInfo.addInformation("Left Player Movement - WSAD");
        PlayerInfo.addInformation("Left Player Fire - " + GameSettings.LeftPlayerFire.toString());
        PlayerInfo.addInformation("Right Player Movement: Arrows");
        PlayerInfo.addInformation("Right Player Fire - " + GameSettings.RightPlayerFire.toString());
        gameSoundPlayer = new GameSoundPlayer();
        Line rightLine = GraphicElementsSetter.setLine(GameSettings.WidthOfTankBorder, 0, GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line leftLine = GraphicElementsSetter.setLine(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, 0, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line horizontalLine = GraphicElementsSetter.setLine(GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        timerLabel = GraphicElementsSetter.setLabel("", 0, GameSettings.WindowHeight / 3);
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.1); -fx-font-weight: bold;");
        timerLabel.setVisible(false);
        layerPane.setId("gameBackground");
        layerPane.getStylesheets().add("css/backgrounds.css");
        GraphicElementsSetter.addChildren(layerPane, rightLine, leftLine, horizontalLine, timerLabel);
        Bomb.draw(layerPane);
        gameTime = (int) (GameSettings.GameTime);
        PlayerInfo.setErrorList(layerPane);
        gameSoundPlayer.playBackgroundSound();
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
    private void backButtonAction() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private void setGamePropertiesTab() {
        Tab tabGameProperties = tabPane.getTabs().get(0);
        Pane mainPane = GraphicElementsSetter.setPane(0, 0, 800, "mainBackground", "css/backgrounds.css");
        GraphicElementsSetter.setGamePropertiesTabLabels(mainPane);
        GraphicElementsSetter.setGamePropertiesTabControlElements(mainPane, tabPane);
        CheckBox checkBox = GraphicElementsSetter.setCheckBox(500, 350, "css/checkBox.css");
        checkBox.setOnMouseReleased(mouseEvent -> GameSettings.MakeScreenshot = checkBox.isSelected());
        GraphicElementsSetter.addChildren(mainPane, checkBox);
        tabGameProperties.setContent(mainPane);
    }

    private void setManualConfigurationTab() {
        Tab tabConfiguration = tabPane.getTabs().get(1);
        ScrollPane scrollPane = GraphicElementsSetter.setScrollPane(0, 0, 800);
        GridPane mainGridPane = GraphicElementsSetter.setGridPane(0, 0, 800);
        mainGridPane.getStylesheets().add("css/tabPaneButtons.css");
        List<TextField> listOfTextFields = GraphicElementsSetter.setLineOfElements(mainGridPane);
        TextField configNameTextField = GraphicElementsSetter.setTextField(100, 0, 0, "", "");
        configNameTextField.setVisible(false);
        Label configNameLabel = GraphicElementsSetter.setLabel("Enter your config name:", 0, 0, "css/tabLabel.css");
        configNameLabel.setVisible(false);
        Label successLabel = GraphicElementsSetter.setLabel("Configuration File Saved", 0, 0, "css/tabLabel.css");
        GraphicElementsSetter.setManualConfigurationTabButtons(mainGridPane, configNameTextField, configNameLabel, successLabel, listOfTextFields);
        mainGridPane.add(configNameLabel, 0, GameSettings.getConfigurationList().size() + 2);
        mainGridPane.add(configNameTextField, 0, GameSettings.getConfigurationList().size() + 3);
        scrollPane.setContent(mainGridPane);
        tabConfiguration.setContent(scrollPane);
    }

    private void setControlsTab() {
        Tab tabGameProperties = tabPane.getTabs().get(2);
        Pane mainPane = GraphicElementsSetter.setPane(0, 0, 800, "mainBackground", "css/backgrounds.css");
        GraphicElementsSetter.setControlsTabLabels(mainPane);
        GraphicElementsSetter.setControlsTabButtons(mainPane);
        tabGameProperties.setContent(mainPane);
    }

    private void showEndPane(boolean bombCollision) {
        gameLoop.stop();
        finishPane = GraphicElementsSetter.setPane(100, 50, 600, "finishPaneBackground", "css/backgrounds.css");
        finishPane.setPrefHeight(650);
        Label finishLabel = GraphicElementsSetter.setLabel("Time Over", 0, 30);
        if (bombCollision) {
            finishLabel.setText("Bomb Collision!");
        }
        finishLabel.setPrefWidth(600);
        finishLabel.setAlignment(Pos.CENTER);
        finishLabel.setStyle("-fx-font-size: 60 px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        PieChart pieChart = GraphicElementsSetter.setPieChart(gameBoard);
        Button quitButton = GraphicElementsSetter.setButton(5, 500, "Quit", "css/buttons.css");
        quitButton.setPrefWidth(290);
        quitButton.setOnAction(e -> System.exit(0));
        Button againButton = GraphicElementsSetter.setButton(305, 500, "Again!", "css/buttons.css");
        againButton.setPrefWidth(290);
        againButton.setOnAction(e -> {
            primaryStage.close();
            gameBoard.reset();
            startButtonPressed();
        });
        layerPane.getChildren().clear();
        GraphicElementsSetter.addChildren(finishPane, finishLabel, pieChart, quitButton, againButton);
        GraphicElementsSetter.addChildren(layerPane, finishPane);
        if (GameSettings.MakeScreenshot) {
            GraphicElementsSetter.makeScreenshot(layerPane);
        }
        gameSoundPlayer.stopBackgroundSound();
        gameSoundPlayer.playEndSound();
    }

    private void setPausePane(Pane pane) {
        pausePane = GraphicElementsSetter.setPane(100, 150, 600, "finishPaneBackground", "css/backgrounds.css");
        pausePane.setPrefHeight(300);
        Label pauseLabel = GraphicElementsSetter.setLabel("Game Paused!", 20, 20);
        pauseLabel.setPrefWidth(600);
        pauseLabel.setAlignment(Pos.CENTER);
        pauseLabel.setStyle("-fx-font-size: 60 px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        Button resumeButton = GraphicElementsSetter.setButton(400, 200, "Play", "css/buttons.css");
        resumeButton.setOnAction(e -> {
            gamePaused = false;
            pausePane.setVisible(false);
            actualizationTimeRequired = true;
            gameLoop.start();
            gameSoundPlayer.playBackgroundSound();
        });
        Button settingsButton = GraphicElementsSetter.setButton(5, 200, "Settings", "css/buttons.css");
        settingsButton.setOnAction(e -> settingsButtonPressed());
        GraphicElementsSetter.addChildren(pausePane, pauseLabel, resumeButton, settingsButton);
        GraphicElementsSetter.addChildren(pane, pausePane);
    }
}