import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Controller {
    //Graphic elements:
    @FXML
    private Button startBtn;
    @FXML
    private Button backBtn;
    @FXML
    private TabPane tabPane;
    @FXML
    private AnchorPane rootPane;
    public static Pane layerPane;
    public Pane finishPane;
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
    private void startButtonPressed() throws java.io.IOException {
        Stage Menu = (Stage)startBtn.getScene().getWindow();
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
        gameTime = (int)(GameSettings.GameTime);
        scene.setOnKeyPressed(key-> ControllerSetter.setPressedKey(key.getCode()));
        scene.setOnKeyReleased(key-> ControllerSetter.setReleasedKey(key.getCode()));

        gameLoop = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long currentTime) {
                if (gameTime == 0) {
                    showFinishPane(false);
                }
                if(currentTime - lastTime >= 1000000000) {
                    timerLabel.setVisible(true);
                    if (gameTime == GameSettings.GameTime) {
                        timerLabel.setVisible(false);
                    }
                    if (gameTime == 0) {
                        gameLoop.stop();
                    } else if (gameTime >= 600 && gameTime %60 <10) {
                        timerLabel.setText((gameTime - gameTime %60)/60 + ":" + "0" + gameTime %60);
                    } else if (gameTime >= 60 && gameTime < 600 && gameTime %60 >= 10) {
                        timerLabel.setText("0" + (gameTime - gameTime %60)/60 + ":" + gameTime %60);
                    } else if (gameTime >= 60 && gameTime < 600 && gameTime %60 < 10) {
                        timerLabel.setText("0" + (gameTime - gameTime %60)/60 + ":0" + gameTime %60);
                    } else if (gameTime > 0 && gameTime < 60 && gameTime %60 >= 10) {
                        timerLabel.setText("00" + ":" + (gameTime %60));
                    } else if (gameTime > 0 && gameTime < 60) {
                        timerLabel.setText("00" + ":0" + (gameTime %60));
                    } else {
                        timerLabel.setText((gameTime - gameTime %60)/60 + ":" + gameTime %60);
                    }
                    timerLabel.setTranslateX(GameSettings.WindowWidth /2 - timerLabel.getWidth()/2);
                    lastTime = currentTime;
                    gameTime--;
                }
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
                //TANKS
                gameBoard.leftPlayer.getTank().draw(layerPane);
                if (leftMoveUpPressed && !leftMoveDownPressed) {
                    gameBoard.leftPlayer.getTank().move(GameSettings.LeftPlayerMoveUp);
                }
                if (leftMoveDownPressed && !leftMoveUpPressed) {
                    gameBoard.leftPlayer.getTank().move(GameSettings.LeftPlayerMoveDown);
                }
                if (leftBarrelDownPressed && !leftBarrelUpPressed) {
                    gameBoard.leftPlayer.getTank().rotateBarrel(GameSettings.LeftPlayerBarrelDown);
                }
                if (leftBarrelUpPressed && !leftBarrelDownPressed) {
                    gameBoard.leftPlayer.getTank().rotateBarrel(GameSettings.LeftPlayerBarrelUp);
                }
                if (keySpaceIsPressed && leftPlayerAllowedToShoot) {
                    gameBoard.leftPlayer.getTank().shoot();
                    leftPlayerAllowedToShoot = false;
                }

                gameBoard.rightPlayer.getTank().draw(layerPane);
                if (rightMoveUpPressed && !rightMoveDownPressed) {
                    gameBoard.rightPlayer.getTank().move(GameSettings.RightPlayerMoveUp);
                }
                if (rightMoveDownPressed && !rightMoveUpPressed) {
                    gameBoard.rightPlayer.getTank().move(GameSettings.RightPlayerMoveDown);
                }
                if (rightBarrelUpPressed && !rightBarrelDownPressed) {
                    gameBoard.rightPlayer.getTank().rotateBarrel(GameSettings.RightPlayerBarrelUp);
                }
                if (rightBarrelDownPressed && !rightBarrelUpPressed) {
                    gameBoard.rightPlayer.getTank().rotateBarrel(GameSettings.RightPlayerBarrelDown);
                }
                if (keyShiftIsPressed && rightPlayerAllowedToShoot) {
                    gameBoard.rightPlayer.getTank().shoot();
                    rightPlayerAllowedToShoot = false;
                }

                //GAME BOARD
                if(gameBoard.updateGame(currentTime, layerPane)){
                    showFinishPane(true);
                }
            }
        };
        gameLoop.start();
        PlayerInfo.setErrorList(layerPane);
    }

    private void setGameBoard() {
        Line rightLine = ControllerSetter.setLine(GameSettings.WidthOfTankBorder, 0, GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line leftLine = ControllerSetter.setLine(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, 0, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line horizontalLine = ControllerSetter.setLine(GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.1); -fx-font-weight: bold;");
        timerLabel.setTranslateY(GameSettings.WindowHeight /3);
        timerLabel.setVisible(false);
        layerPane.setId("gameBackground");
        layerPane.getStylesheets().add("css/backgrounds.css");
        ControllerSetter.addChildren(layerPane, rightLine, leftLine, horizontalLine, timerLabel);
        Bomb.draw(layerPane);
    }

    @FXML
    private void settingsButtonPressed() throws IOException {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings.fxml")));
            Stage stage = new Stage();
            stage.setTitle("SnakeFX - Settings");
            stage.setScene(new Scene(root, GameSettings.WindowWidth, GameSettings.WindowHeight));
            stage.setResizable(false);
            stage.show();
            Stage stage1 = (Stage) startBtn.getScene().getWindow();
            stage1.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setGamePropertiesTab(){
        Tab tabGameProperties = tabPane.getTabs().get(0);
        Pane pane = ControllerSetter.setPane(0, 0 ,800, "mainBackground", "css/backgrounds.css");

        Label musicLabel = ControllerSetter.setLabel("Set Volume Of Music:", 50,50, "css/tabLabel.css");
        Label soundLabel = ControllerSetter.setLabel("Set Volume Of Sounds:", 50, 150, "css/tabLabel.css");
        Label configLabel = ControllerSetter.setLabel("Load Your Configuration File:", 50, 250, "css/tabLabel.css");
        Label currentConfigLabel = ControllerSetter.setLabel("Current Configuration: " + GameSettings.ConfigFileName, 50, 280, "minLabel", "css/tabLabel.css");
        Label currentScreenshotExt = ControllerSetter.setLabel("Current Extension File: " + GameSettings.ImageExtension, 50, 380, "minLabel", "css/tabLabel.css");
        Label screenshotLabel = ControllerSetter.setLabel("Make Screenshot After Game:", 50, 350, "css/tabLabel.css");

        Slider musicSlider = ControllerSetter.setSlider(0, 1, 50, 100, 700, GameSettings.VolumeOfMusic);
        musicSlider.setOnMouseReleased(mouseEvent -> GameSettings.VolumeOfMusic = musicSlider.getValue());
        Slider soundSlider = ControllerSetter.setSlider(0, 1, 50, 200, 700, GameSettings.VolumeOfMusicEffects);
        soundSlider.setOnMouseReleased(mouseEvent -> GameSettings.VolumeOfMusicEffects = soundSlider.getValue());

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Button configFileBtn = ControllerSetter.setButton(500, 250, "Load File", "css/tabPaneButtons.css");
        configFileBtn.setOnMouseClicked(mouseEvent -> {
            File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
            if(file!=null){
                GameSettings.PathConfigFile = file.toString();
                GameSettings.loadConfigFile();
                currentConfigLabel.setText("Current Configuration File: " + GameSettings.ConfigFileName);
                currentScreenshotExt.setText("Current Extension: " + GameSettings.ImageExtension);
            }
        });

        CheckBox checkBox = new CheckBox();
        checkBox.setLayoutX(500);
        checkBox.setLayoutY(350);
        checkBox.getStylesheets().add("css/checkBox.css");
        checkBox.setOnMouseReleased(mouseEvent -> GameSettings.MakeScreenshot = checkBox.isSelected());
        ControllerSetter.addChildren(pane, musicLabel, musicSlider, soundLabel, soundSlider, configLabel, currentConfigLabel, configFileBtn, screenshotLabel, currentScreenshotExt, checkBox);
        tabGameProperties.setContent(pane);
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

    private void setControlsTab(){
        Tab tabGameProperties = tabPane.getTabs().get(2);
        Pane pane = ControllerSetter.setPane(0, 0, 800, "mainBackground", "css/backgrounds.css");
        Label topRightLabel = ControllerSetter.setLabel("Left Player:", 100, 30, "css/tabLabel.css");
        Label topLeftLabel = ControllerSetter.setLabel("Right Player:", 500, 30, "css/tabLabel.css");
        Label moveLabel = ControllerSetter.setLabel("Move:", 50, 100, "css/tabLabel.css");
        Label shootLabel = ControllerSetter.setLabel("Shoot:", 50, 370, "css/tabLabel.css");
        List<Button> controlButtons = new ArrayList<>();
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 100,200, GameSettings.LeftPlayerBarrelDown.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 170,200, GameSettings.LeftPlayerMoveDown.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 240,200, GameSettings.LeftPlayerBarrelUp.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 170,130, GameSettings.LeftPlayerMoveUp.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 500,200, GameSettings.RightPlayerBarrelDown.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 570,200, GameSettings.RightPlayerMoveDown.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 640,200, GameSettings.RightPlayerBarrelUp.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 570,130, GameSettings.RightPlayerMoveUp.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 170,400, GameSettings.LeftPlayerFire.toString()));
        controlButtons.add(ControllerSetter.setGroupOfButtons(60,60, 570,400, GameSettings.RightPlayerFire.toString()));
        for(Button x: controlButtons){
            x.setOnMouseClicked(keyEvent -> {
                for(Button y: controlButtons){
                    y.setId("button");
                }
                x.setId("chosenButton");
            });
            x.setOnKeyPressed(keyEvent -> {
                boolean keyIsUsed = false;
                for(Button y: controlButtons){
                    if(y.getText().equals(keyEvent.getCode().toString())){
                        keyIsUsed = true;
                        if(y!=x)
                            y.setId("usedButton");
                    }
                }
                if(!keyIsUsed) {
                    if(GameSettings.LeftPlayerBarrelDown.toString().equals(x.getText()))
                        GameSettings.LeftPlayerBarrelDown = keyEvent.getCode();
                    else if(GameSettings.LeftPlayerBarrelUp.toString().equals(x.getText()))
                        GameSettings.LeftPlayerBarrelUp = keyEvent.getCode();
                    else if(GameSettings.LeftPlayerMoveDown.toString().equals(x.getText()))
                        GameSettings.LeftPlayerMoveDown = keyEvent.getCode();
                    else if(GameSettings.LeftPlayerMoveUp.toString().equals(x.getText()))
                        GameSettings.LeftPlayerMoveUp = keyEvent.getCode();
                    else if(GameSettings.RightPlayerBarrelDown.toString().equals(x.getText()))
                        GameSettings.RightPlayerBarrelDown = keyEvent.getCode();
                    else if(GameSettings.RightPlayerBarrelUp.toString().equals(x.getText()))
                        GameSettings.RightPlayerBarrelUp = keyEvent.getCode();
                    else if(GameSettings.RightPlayerMoveDown.toString().equals(x.getText()))
                        GameSettings.RightPlayerMoveDown = keyEvent.getCode();
                    else if(GameSettings.RightPlayerMoveUp.toString().equals(x.getText()))
                        GameSettings.RightPlayerMoveUp = keyEvent.getCode();
                    else if(GameSettings.RightPlayerFire.toString().equals(x.getText()))
                        GameSettings.RightPlayerFire = keyEvent.getCode();
                    else if(GameSettings.LeftPlayerFire.toString().equals(x.getText()))
                        GameSettings.LeftPlayerFire = keyEvent.getCode();
                    else
                        System.out.println("Warning: This key is unhandled!");
                    x.setText(keyEvent.getCode().toString());
                    x.setStyle("-fx-font-size:" + 30.0/Math.sqrt(keyEvent.getCode().toString().length()) +"px;");
                }
            });
            pane.getChildren().add(x);
        }

    }
    @FXML
    private void exitButtonPressed() {
        System.exit(1);
    }
    @FXML
    public void backButtonAction() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
            Stage mainStage = new Stage();
            mainStage.setTitle("Snake");
            mainStage.setScene(new Scene(root, GameSettings.WindowWidth, GameSettings.WindowHeight));
            mainStage.show();
            stage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showFinishPane(boolean bombCollision){
        gameLoop.stop();
        finishPane = ControllerSetter.setPane(100, 50, 600, "finishPaneBackground", "css/backgrounds.css");
        finishPane.setPrefHeight(650);
        finishPane.setVisible(true);
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
            startButtonPressed();
            //TODO: Obsługa tego zdarzenia - nie usuwają się komórki i kule z layerPane
        });
        layerPane.getChildren().clear();
        ControllerSetter.addChildren(finishPane, finishLabel, pieChart, quitButton, againButton);
        ControllerSetter.addChildren(layerPane, finishPane);
        if(GameSettings.MakeScreenshot) {
            ControllerSetter.makeScreenshot(layerPane);
        }
    }

    private void pauseButtonPressed() {}

}
