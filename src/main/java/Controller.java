import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int gameTime;
    private GameBoard gameBoard;
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
        scene.setOnKeyPressed(key-> ControllerSetter.setPressedKey(key.getCode()));
        scene.setOnKeyReleased(key-> ControllerSetter.setReleasedKey(key.getCode()));

        gameLoop = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long currentTime) {
                if (gameTime == 0) {
                    System.out.println("Lewy: " + gameBoard.leftPlayer.getScore() + "Prawy: " + gameBoard.rightPlayer.getScore());
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
                gameBoard.updateGame(currentTime, layerPane);
            }
        };
        gameLoop.start();
    }

    private void setGameBoard() {
        Line rightLine = ControllerSetter.setLine(GameSettings.WidthOfTankBorder, 0, GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line leftLine = ControllerSetter.setLine(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, 0, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        Line horizontalLine = ControllerSetter.setLine(GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder);
        timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.03); -fx-font-weight: bold;");
        timerLabel.setTranslateY(GameSettings.WindowHeight /3);
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

    @FXML
    private void initialize(){
        if(tabPane!=null){
            System.out.println("Ustawienia");
            Tab tab = tabPane.getTabs().get(0);
            GridPane gridPane = new GridPane();
            gridPane.setTranslateX(0);
            gridPane.setTranslateY(0);
            gridPane.setPrefWidth(800);
            gridPane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
            tab.setContent(gridPane);
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
        int i;
        for(i = 0; i < GameSettings.getConfigurationList().size(); i++){
            Label tmpLabel = new Label(GameSettings.getConfigurationList().get(i)[0]);
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
                                System.out.println("Warnings! This value is unhandled: " + tx.getText());
                        }
                        catch (NumberFormatException e){
                            System.out.println("Wrong format!");
                            tx.clear();
                        }
                    }
                });
            }
            scrollPane.setContent(gridPane);
            scrollPane.setStyle("-fx-background: transparent");
            tabGameMark.setContent(scrollPane);
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
    private void pauseButtonPressed() {}

}