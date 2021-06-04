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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
    public static Pane layerPane;
    public Pane finishPane;
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int gameTime;
    private GameBoard gameBoard;
    private Stage primaryStage;
    //Game Control:
    public static boolean gamePaused;
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
        scene.setOnKeyPressed(key-> {
            if(key.getCode().equals(GameSettings.Pause)){
                pauseKeyPressed(layerPane);
                gamePaused = true;
            }
            ControllerSetter.setPressedKey(key.getCode());
        });
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
                if (leftPlayerShootPressed && leftPlayerAllowedToShoot) {
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
                if (rightPlayerShootPressed && rightPlayerAllowedToShoot) {
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

    private void setManualConfigurationTab(){
        Tab tabConfiguration = tabPane.getTabs().get(1);
        List<TextField> listOfTextFields = new ArrayList<>();
        ScrollPane scrollPane = ControllerSetter.setScrollPane(0, 0 , 800);
        GridPane gridConfigurationPane = ControllerSetter.setGridPane(0, 0, 800);

        for(int i = 0; i < GameSettings.getConfigurationList().size(); i++){
            Label tmpLabel = ControllerSetter.setLabel(GameSettings.getConfigurationList().get(i)[0], 20, 0);
            tmpLabel.setStyle("-fx-font-size: 20px;-fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");

            Button tmpBtn = ControllerSetter.setButton(200, 0, "Save " + GameSettings.getConfigurationList().get(i)[1], "css/tabPaneButtons.css");

            TextField tmpTextField = ControllerSetter.setTextField(100, 50, 0, GameSettings.getConfigurationList().get(i)[2], GameSettings.getConfigurationList().get(i)[1]);

            gridConfigurationPane.add(tmpLabel,0, i+1);
            gridConfigurationPane.add(tmpTextField,1, i+1);
            gridConfigurationPane.add(tmpBtn,2, i+1);
            listOfTextFields.add(tmpTextField);
            tmpBtn.setOnAction(event-> {
                for(TextField tx: listOfTextFields){
                    if(tx.getId().equals(tmpBtn.getText().substring(5))){
                        try {
                            if (!GameSettings.setGameSettings(tmpBtn.getText().substring(5), tx.getText()))
                                PlayerInfo.addInformation("[ERROR]This value is unhandled: " + tx.getText());
                        }
                        catch (NumberFormatException e){
                            tx.clear();
                        }
                    }
                }
            });
        }
        Button saveConfigurationBtn = new Button("Save Configuration File");

        TextField configNameTextField = ControllerSetter.setTextField(100,0,0, "", "");
        configNameTextField.setVisible(false);

        Label configNameLabel = ControllerSetter.setLabel("Enter your config name:",0, 0, "css/tabLabel.css");
        configNameLabel.setVisible(false);
        Label successLabel = ControllerSetter.setLabel("Configuration File Saved", 0, 0, "css/tabLabel.css");

        gridConfigurationPane.add(configNameLabel, 0, GameSettings.getConfigurationList().size() + 2);
        gridConfigurationPane.add(configNameTextField, 0, GameSettings.getConfigurationList().size() + 3);
        final int position = GameSettings.getConfigurationList().size() + 2;
        saveConfigurationBtn.setOnMouseClicked(mouseEvent -> {
            if(saveConfigurationBtn.getText().equals("Accept Name")){
                configNameTextField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
                if(configNameTextField.getText().length() == 0) {
                    configNameTextField.setStyle("-fx-background-color: rgba(200, 2, 2, 0.3)");
                }
                else {
                    for (TextField textField : GameSettings.saveConfigFile(listOfTextFields, configNameTextField.getText())) {
                        textField.setStyle("-fx-background-color: rgba(200, 2, 2, 0.3)");
                    }
                    gridConfigurationPane.getChildren().remove(saveConfigurationBtn);
                    saveConfigurationBtn.setText("Save Configuration File");
                    gridConfigurationPane.add(saveConfigurationBtn, 0, position);
                    configNameLabel.setVisible(false);
                    configNameTextField.setVisible(false);
                    gridConfigurationPane.add(successLabel, 0, position + 1);
                }
            }
            else if(saveConfigurationBtn.getText().equals("Save Configuration File")) {
                for(TextField textField : listOfTextFields){
                    textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
                }
                gridConfigurationPane.getChildren().remove(saveConfigurationBtn);
                saveConfigurationBtn.setText("Accept Name");
                saveConfigurationBtn.setPrefWidth(saveConfigurationBtn.getWidth());
                gridConfigurationPane.getChildren().remove(successLabel);
                configNameLabel.setVisible(true);
                configNameTextField.setVisible(true);
                gridConfigurationPane.add(saveConfigurationBtn, 2, position+1);
            }
        });
        gridConfigurationPane.add(saveConfigurationBtn, 0, GameSettings.getConfigurationList().size() + 1);
        gridConfigurationPane.getStylesheets().add("css/tabPaneButtons.css");
        scrollPane.setContent(gridConfigurationPane);
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
        ControllerSetter.addChildren(pane, topRightLabel, topLeftLabel, moveLabel, shootLabel);
        tabGameProperties.setContent(pane);
    }

    @FXML
    private void exitButtonPressed() {
        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void backButtonAction() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
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

    public void pauseKeyPressed(Pane pane) {
        gameLoop.stop();
        Pane pausePane = ControllerSetter.setPane(100,150, 600, "finishPaneBackground", "css/backgrounds.css");
        pausePane.setPrefHeight(300);
        Label pauseLabel = ControllerSetter.setLabel("Game Paused!", 20, 20);
        pauseLabel.setPrefWidth(600);
        pauseLabel.setAlignment(Pos.CENTER);
        pauseLabel.setStyle("-fx-font-size: 60 px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        Button resumeButton = ControllerSetter.setButton(400, 200, "Play", "css/buttons.css");
        resumeButton.setOnAction(e -> {
            pausePane.setVisible(false);
            gameLoop.start();
            gamePaused = false;
        });
        Button settingsButton = ControllerSetter.setButton(5, 200, "Settings", "css/buttons.css");
        settingsButton.setOnAction(e->{
            settingsButtonPressed();
        });
        ControllerSetter.addChildren(pausePane, pauseLabel, resumeButton, settingsButton);
        ControllerSetter.addChildren(pane, pausePane);
    }

}
