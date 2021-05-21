import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private Button startBtn;
    @FXML
    private Button backBtn;
    @FXML
    private TabPane tabPane;
    public static Pane layerPane;
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int gameTime;
    private final GameBoard gameBoard = new GameBoard();

    //movement
    private boolean leftBarrelUpPressed;
    private boolean leftBarrelDownPressed;
    private boolean leftMoveUpPressed;
    private boolean leftMoveDownPressed;
    private boolean rightMoveUpPressed;
    private boolean rightMoveDownPressed;
    private boolean rightBarrelDownPressed;
    private boolean rightBarrelUpPressed;
    //shooting
    private long lastTimeOfLeftPlayerShot, lastTimeOfRightPlayerShot;
    private boolean leftPlayerAllowedToShoot, rightPlayerAllowedToShoot;
    private boolean leftPlayerShootPressed;
    private boolean rightPlayerShootPressed;


    @FXML
    private void startButtonPressed() {
        Stage Menu = (Stage)startBtn.getScene().getWindow();
        Menu.hide();
        Pane gameField = new Pane();
        Stage primaryStage = new Stage();
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
            KeyCode keyCode = key.getCode();
            if (keyCode.equals(KeyCode.W)) {
                leftMoveUpPressed = true;
            }
            if (keyCode.equals(GameSettings.LeftPlayerMoveDown)) {
                leftMoveDownPressed = true;
            }
            if (keyCode.equals(GameSettings.LeftPlayerBarrelDown)) {
                leftBarrelDownPressed = true;
            }
            if (keyCode.equals(GameSettings.LeftPlayerBarrelUp)) {
                leftBarrelUpPressed = true;
            }
            if (keyCode.equals(GameSettings.RightPlayerMoveUp)) {
                rightMoveUpPressed = true;
            }
            if (keyCode.equals(GameSettings.RightPlayerMoveDown)) {
                rightMoveDownPressed = true;
            }
            if (keyCode.equals(GameSettings.RightPlayerBarrelUp)) {
                rightBarrelUpPressed = true;
            }
            if (keyCode.equals(GameSettings.RightPlayerBarrelDown)) {
                rightBarrelDownPressed = true;
            }
            if (keyCode.equals(GameSettings.LeftPlayerFire)) {
                leftPlayerShootPressed = true;
            }
            if (keyCode.equals(GameSettings.RightPlayerFire)) {
                rightPlayerShootPressed = true;
            }
        });
        scene.setOnKeyReleased(key-> {
            KeyCode keyCode = key.getCode();
            if (keyCode.equals(KeyCode.W)) {
                leftMoveUpPressed = false;
            }
            if (keyCode.equals(GameSettings.LeftPlayerMoveDown)) {
                leftMoveDownPressed = false;
            }
            if (keyCode.equals(GameSettings.LeftPlayerBarrelDown)) {
                leftBarrelDownPressed = false;
            }
            if (keyCode.equals(GameSettings.LeftPlayerBarrelUp)) {
                leftBarrelUpPressed = false;
            }
            if (keyCode.equals(GameSettings.RightPlayerMoveUp)) {
                rightMoveUpPressed = false;
            }
            if (keyCode.equals(GameSettings.RightPlayerMoveDown)) {
                rightMoveDownPressed = false;
            }
            if (keyCode.equals(GameSettings.RightPlayerBarrelUp)) {
                rightBarrelUpPressed = false;
            }
            if (keyCode.equals(GameSettings.RightPlayerBarrelDown)) {
                rightBarrelDownPressed = false;
            }
            if (keyCode.equals(GameSettings.LeftPlayerFire)) {
                leftPlayerShootPressed = false;
            }
            if (keyCode.equals(GameSettings.RightPlayerFire)) {
                rightPlayerShootPressed = false;
            }
        });

        gameLoop = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long currentTime) {
                if (gameTime == 0) {
                    System.exit(0);
                }
                if(currentTime - lastTime >= 1000000000) {
                    timerLabel.setVisible(true);
                    if (gameTime == GameSettings.GameTime) {
                        timerLabel.setVisible(false);
                    } else if (gameTime == 0) {
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
                gameBoard.updateGame(currentTime, layerPane);
            }
        };
        gameLoop.start();
    }

    private void setGameBoard() {
        Line rightLine = new Line();
        Line leftLine = new Line();
        Line horizontalLine = new Line();
        rightLine.setStartX(GameSettings.WidthOfTankBorder);
        rightLine.setStartY(0);
        rightLine.setEndX(GameSettings.WidthOfTankBorder);
        rightLine.setEndY(GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        leftLine.setStartX(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        leftLine.setStartY(0);
        leftLine.setEndX(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        leftLine.setEndY(GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        horizontalLine.setStartX(GameSettings.WidthOfTankBorder);
        horizontalLine.setStartY(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        horizontalLine.setEndX(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        horizontalLine.setEndY(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.03); -fx-font-weight: bold;");
        timerLabel.setTranslateY(GameSettings.WindowHeight /3);
        layerPane.getChildren().add(rightLine);
        layerPane.getChildren().add(leftLine);
        layerPane.getChildren().add(horizontalLine);
        layerPane.getChildren().add(timerLabel);
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
            stage.show();
            Stage stage1 = (Stage) startBtn.getScene().getWindow();
            stage1.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        if(tabPane!=null) {
            setGamePropertiesTab();
            setManualConfigurationTab();
            setControlsTab();
        }
    }

    private void setGamePropertiesTab(){
        //Game Properties
        Tab tabGameProperties = tabPane.getTabs().get(0);
        Pane pane = new Pane();
        pane.setTranslateX(0);
        pane.setTranslateY(0);
        pane.setPrefWidth(800);
        pane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
        Label musicLabel = setLabel("Set Volume Of Music:", 50,50);
        musicLabel.getStylesheets().add("css/tabLabel.css");
        Slider musicSlider = new Slider();
        musicSlider.setMin(0);
        musicSlider.setMax(1);
        musicSlider.setTranslateX(50);
        musicSlider.setTranslateY(100);
        musicSlider.setPrefWidth(700);
        musicSlider.setValue(GameSettings.VolumeOfMusic);
        musicSlider.setOnMouseReleased(mouseEvent -> GameSettings.VolumeOfMusic = musicSlider.getValue());
        Label soundLabel = setLabel("Set Volume Of Sounds:", 50, 150);
        soundLabel.getStylesheets().add("css/tabLabel.css");
        Slider soundSlider = new Slider();
        soundSlider.setMin(0);
        soundSlider.setMax(1);
        soundSlider.setTranslateX(50);
        soundSlider.setTranslateY(200);
        soundSlider.setPrefWidth(700);
        soundSlider.setValue(GameSettings.VolumeOfMusicEffects);
        soundSlider.setOnMouseReleased(mouseEvent -> GameSettings.VolumeOfMusicEffects = soundSlider.getValue());
        Label configLabel = setLabel("Load Your Configuration File:", 50, 250);
        configLabel.getStylesheets().add("css/tabLabel.css");
        Label currentConfigLabel = setLabel("Current Configuration: " + GameSettings.ConfigFileName, 50, 280);
        currentConfigLabel.setId("minLabel");
        currentConfigLabel.getStylesheets().add("css/tabLabel.css");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Button configFileBtn = new Button("Load File");
        Label currentScreenshotExt = setLabel("Current Extension File: " + GameSettings.ImageExtension, 50, 380);
        configFileBtn.setOnMouseClicked(mouseEvent -> {
            File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
            if(file!=null){
                GameSettings.PathConfigFile = file.toString();
                GameSettings.loadConfigFile();
                currentConfigLabel.setText("Current Configuration File: " + GameSettings.ConfigFileName);
                currentScreenshotExt.setText("Current Extension: " + GameSettings.ImageExtension);
            }
        });
        configFileBtn.setTranslateX(500);
        configFileBtn.setTranslateY(250);
        configFileBtn.getStylesheets().add("css/tabPaneButtons.css");
        Label screenshotLabel = setLabel("Make Screenshot After Game:", 50, 350);
        screenshotLabel.getStylesheets().add("css/tabLabel.css");
        currentScreenshotExt.setId("minLabel");
        currentScreenshotExt.getStylesheets().add("css/tabLabel.css");
        CheckBox checkBox = new CheckBox();
        checkBox.setLayoutX(500);
        checkBox.setLayoutY(350);
        checkBox.getStylesheets().add("css/checkBox.css");
        checkBox.setOnMouseReleased(mouseEvent -> GameSettings.MakeScreenshot = checkBox.isSelected());
        pane.getChildren().add(musicLabel);
        pane.getChildren().add(musicSlider);
        pane.getChildren().add(soundLabel);
        pane.getChildren().add(soundSlider);
        pane.getChildren().add(configLabel);
        pane.getChildren().add(currentConfigLabel);
        pane.getChildren().add(configFileBtn);
        pane.getChildren().add(screenshotLabel);
        pane.getChildren().add(currentScreenshotExt);
        pane.getChildren().add(checkBox);
        tabGameProperties.setContent(pane);
    }
    private void setManualConfigurationTab(){
        //Manual Configuration
        Tab tabConfiguration = tabPane.getTabs().get(1);
        List<TextField> listOfTextFields = new ArrayList<>();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setTranslateX(0);
        scrollPane.setTranslateY(0);
        scrollPane.setPrefWidth(800);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        GridPane gridConfigurationPane = new GridPane();
        gridConfigurationPane.setTranslateX(0);
        gridConfigurationPane.setTranslateY(0);
        gridConfigurationPane.setPrefWidth(800);
        gridConfigurationPane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
        gridConfigurationPane.setVgap(40);
        int i = 0;
        for(i = 0; i < GameSettings.getConfigurationList().size(); i++){
            Label tmpLabel = new Label(GameSettings.getConfigurationList().get(i)[0]);
            tmpLabel.setStyle("-fx-font-size: 20px;-fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
            tmpLabel.setTranslateX(20);
            Button tmpBtn = new Button("Save " + GameSettings.getConfigurationList().get(i)[1]);
            tmpBtn.getStylesheets().add("css/tabPaneButtons.css");
            tmpBtn.setTranslateX(200);
            TextField tmpTextField = new TextField();
            tmpTextField.setPrefWidth(100);
            tmpTextField.setTranslateX(50);
            tmpTextField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
            tmpTextField.setText(GameSettings.getConfigurationList().get(i)[2]);
            tmpTextField.setId(GameSettings.getConfigurationList().get(i)[1]);
            gridConfigurationPane.add(tmpLabel,0, i);
            gridConfigurationPane.add(tmpTextField,1, i);
            gridConfigurationPane.add(tmpBtn,2, i);
            listOfTextFields.add(tmpTextField);
            tmpBtn.setOnAction(event-> {
                for(TextField tx: listOfTextFields){
                    if(tx.getId().equals(tmpBtn.getText().substring(5))){
                        try {
                            if (!GameSettings.setGameSettings(tmpBtn.getText().substring(5), tx.getText()))
                                System.out.println("Warnings! This value is unhandled: " + tx.getText());
                        }
                        catch (NumberFormatException e){
                            System.out.println("Wrong format!");
                            tx.clear();
                        }
                    }
                }
            });
        }
        Button saveConfigurationBtn = new Button("Save Configuration File");
        TextField configNameTextField = new TextField();
        configNameTextField.setPrefSize(100, 30);
        configNameTextField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
        configNameTextField.setVisible(false);
        Label configNameLabel = setLabel("Enter your config name:",0, 0);
        configNameLabel.getStylesheets().add("css/tabLabel.css");
        configNameLabel.setVisible(false);
        Label successLabel = setLabel("Configuration File Saved", 0, 0);
        successLabel.getStylesheets().add("css/tabLabel.css");
        gridConfigurationPane.add(configNameLabel, 0, i);
        gridConfigurationPane.add(configNameTextField, 0, i+1);
        final int position = i;
        saveConfigurationBtn.setOnMouseClicked(mouseEvent -> {
            if(saveConfigurationBtn.getText().equals("Accept Name")){
                if(configNameTextField.getText().length() == 0)
                    System.out.println("There is no configuration Name");
                for(TextField textField : GameSettings.saveConfigFile(listOfTextFields,configNameTextField.getText())){
                    textField.setStyle("-fx-background-color: rgba(200, 2, 2, 0.3)");
                }
                gridConfigurationPane.getChildren().remove(saveConfigurationBtn);
                saveConfigurationBtn.setText("Save Configuration File");
                gridConfigurationPane.add(saveConfigurationBtn,0, position);
                configNameLabel.setVisible(false);
                configNameTextField.setVisible(false);
                gridConfigurationPane.add(successLabel, 0, position + 1);
            }
            else if(saveConfigurationBtn.getText().equals("Save Configuration File")) {
                for(TextField textField : listOfTextFields){
                    textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
                }
                saveConfigurationBtn.setPrefWidth(saveConfigurationBtn.getWidth());
                saveConfigurationBtn.setText("Accept Name");
                gridConfigurationPane.getChildren().remove(saveConfigurationBtn);
                gridConfigurationPane.add(saveConfigurationBtn, 2, position+1);
                gridConfigurationPane.getChildren().remove(successLabel);
                configNameLabel.setVisible(true);
                configNameTextField.setVisible(true);

            }
        });
        gridConfigurationPane.add(saveConfigurationBtn, 0, i);
        gridConfigurationPane.getStylesheets().add("css/tabPaneButtons.css");
        scrollPane.setContent(gridConfigurationPane);
        scrollPane.setStyle("-fx-background: transparent");
        tabConfiguration.setContent(scrollPane);
    }
    private void setControlsTab(){
        //Controls
        Tab tabGameProperties = tabPane.getTabs().get(2);
        Pane pane = new Pane();
        pane.setTranslateX(0);
        pane.setTranslateY(0);
        pane.setPrefWidth(800);
        pane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
        Label topRightLabel = setLabel("Left Player:", 100, 30);
        topRightLabel.getStylesheets().add("css/tabLabel.css");
        Label topLeftLabel = setLabel("Right Player:", 500, 30);
        topLeftLabel.getStylesheets().add("css/tabLabel.css");
        Label moveLabel = setLabel("Move:", 50, 100);
        moveLabel.getStylesheets().add("css/tabLabel.css");
        List<Button> controlButtons = new ArrayList<>();
        controlButtons.add(setButton(60,60, 100,200, GameSettings.LeftPlayerBarrelDown.toString()));
        controlButtons.add(setButton(60,60, 170,200, GameSettings.LeftPlayerMoveDown.toString()));
        controlButtons.add(setButton(60,60, 240,200, GameSettings.LeftPlayerBarrelUp.toString()));
        controlButtons.add(setButton(60,60, 170,130, GameSettings.LeftPlayerMoveUp.toString()));
        controlButtons.add(setButton(60,60, 500,200, GameSettings.RightPlayerBarrelDown.toString()));
        controlButtons.add(setButton(60,60, 570,200, GameSettings.RightPlayerMoveDown.toString()));
        controlButtons.add(setButton(60,60, 640,200, GameSettings.RightPlayerBarrelUp.toString()));
        controlButtons.add(setButton(60,60, 570,130, GameSettings.RightPlayerMoveUp.toString()));
        controlButtons.add(setButton(60,60, 170,400, GameSettings.LeftPlayerFire.toString()));
        controlButtons.add(setButton(60,60, 570,400, GameSettings.RightPlayerFire.toString()));
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
        Label shootLabel = setLabel("Shoot:", 50, 370);
        shootLabel.getStylesheets().add("css/tabLabel.css");
        pane.getChildren().add(topRightLabel);
        pane.getChildren().add(topLeftLabel);
        pane.getChildren().add(moveLabel);
        pane.getChildren().add(shootLabel);
        tabGameProperties.setContent(pane);
    }

    public Button setButton(int width, int height, int x, int y, String text){
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setId("button");
        button.getStylesheets().add("css/keyButtons.css");
        button.setStyle("-fx-font-size:" + 30.0/Math.sqrt(text.length()) +"px;");
        return button;
    }

    public Label setLabel(String text, int x, int y){
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        return label;
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
            Stage stage1 = new Stage();
            stage1.setTitle("Snake");
            stage1.setScene(new Scene(root, GameSettings.WindowWidth, GameSettings.WindowHeight));
            stage1.show();
            stage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void pauseButtonPressed() {}
}