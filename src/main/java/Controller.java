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
import javafx.stage.Stage;

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
    @FXML
    private AnchorPane rootPane;
    public static Pane layerPane;
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int timeGame;
    private final GameBoard gameBoard = new GameBoard();

    //movement
    private boolean keyDIsPressed;
    private boolean keyAIsPressed;
    private boolean keyWIsPressed;
    private boolean keySIsPressed;
    private boolean keyUpIsPressed;
    private boolean keyDownIsPressed;
    private boolean keyRightIsPressed;
    private boolean keyLeftIsPressed;
    //shooting
    //V E R B O S E
    private long lastTimeOfLeftPlayerShot, lastTimeOfRightPlayerShot;
    private boolean leftPlayerAllowedToShoot, rightPlayerAllowedToShoot;
    private boolean keySpaceIsPressed;
    private boolean keyShiftIsPressed;


    @FXML
    private void startButtonPressed() throws java.io.IOException {
        Stage Menu = (Stage) startBtn.getScene().getWindow();
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
        timeGame = (int)(GameSettings.GameTime);
        scene.setOnKeyPressed(key->{
            KeyCode keyCode = key.getCode();
            if(keyCode.equals(KeyCode.W))
                keyWIsPressed = true;
            if(keyCode.equals(KeyCode.S))
                keySIsPressed = true;
            if(keyCode.equals(KeyCode.A))
                keyAIsPressed = true;
            if(keyCode.equals(KeyCode.D))
                keyDIsPressed = true;
            if(keyCode.equals(KeyCode.UP))
                keyUpIsPressed = true;
            if(keyCode.equals(KeyCode.DOWN))
                keyDownIsPressed = true;
            if(keyCode.equals(KeyCode.LEFT))
                keyLeftIsPressed = true;
            if(keyCode.equals(KeyCode.RIGHT))
                keyRightIsPressed = true;
            if(keyCode.equals(KeyCode.SPACE))
                keySpaceIsPressed = true;
            if(keyCode.equals(KeyCode.SHIFT))
                keyShiftIsPressed = true;
        });
        scene.setOnKeyReleased(key->{
            KeyCode keyCode = key.getCode();
            if(keyCode.equals(KeyCode.W))
                keyWIsPressed = false;
            if(keyCode.equals(KeyCode.S))
                keySIsPressed = false;
            if(keyCode.equals(KeyCode.A))
                keyAIsPressed = false;
            if(keyCode.equals(KeyCode.D))
                keyDIsPressed = false;
            if(keyCode.equals(KeyCode.UP))
                keyUpIsPressed = false;
            if(keyCode.equals(KeyCode.DOWN))
                keyDownIsPressed = false;
            if(keyCode.equals(KeyCode.LEFT))
                keyLeftIsPressed = false;
            if(keyCode.equals(KeyCode.RIGHT))
                keyRightIsPressed = false;
            if(keyCode.equals(KeyCode.SPACE))
                keySpaceIsPressed = false;
            if(keyCode.equals(KeyCode.SHIFT))
                keyShiftIsPressed = false;
        });

        gameLoop = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long currentTime) {
                if(currentTime - lastTime >= 1000000000) {
                    timerLabel.setVisible(true);
                    if(timeGame == GameSettings.GameTime)
                        timerLabel.setVisible(false);
                    if (timeGame == 0)
                        gameLoop.stop();
                    else if(timeGame >= 600 && timeGame%60 <10)
                        timerLabel.setText((timeGame-timeGame%60)/60 + ":" + "0" + timeGame%60);
                    else if(timeGame >= 60 && timeGame < 600 && timeGame%60 >= 10)
                        timerLabel.setText("0" + (timeGame-timeGame%60)/60 + ":" + timeGame%60);
                    else if(timeGame >= 60 && timeGame < 600 && timeGame%60 < 10)
                        timerLabel.setText("0" + (timeGame-timeGame%60)/60 + ":0" + timeGame%60);
                    else if(timeGame > 0 && timeGame < 60 && timeGame%60 >= 10)
                        timerLabel.setText("00" + ":" + (timeGame%60));
                    else if(timeGame > 0 && timeGame < 60)
                        timerLabel.setText("00" + ":0" + (timeGame%60));
                    else
                        timerLabel.setText((timeGame-timeGame%60)/60 + ":" + timeGame%60);

                    timerLabel.setTranslateX(GameSettings.WindowWidth /2 - timerLabel.getWidth()/2);
                    lastTime = currentTime;
                    timeGame--;
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
                if (keyWIsPressed && !keySIsPressed) {
                    gameBoard.leftPlayer.getTank().move(KeyCode.W);
                }
                if (keySIsPressed && !keyWIsPressed) {
                    gameBoard.leftPlayer.getTank().move(KeyCode.S);
                }
                if (keyAIsPressed && !keyDIsPressed) {
                    gameBoard.leftPlayer.getTank().rotateBarrel(KeyCode.A);
                    //System.out.println("Barrel Angle (L): " + gameBoard.leftPlayer.getTank().getBarrelAngle());
                }
                if (keyDIsPressed && !keyAIsPressed) {
                    gameBoard.leftPlayer.getTank().rotateBarrel(KeyCode.D);
                    //System.out.println("Barrel Angle (L): " + gameBoard.leftPlayer.getTank().getBarrelAngle());
                }
                if (keySpaceIsPressed && leftPlayerAllowedToShoot) {
                    gameBoard.leftPlayer.getTank().shoot();
                    leftPlayerAllowedToShoot = false;
                }

                gameBoard.rightPlayer.getTank().draw(layerPane);
                if (keyUpIsPressed && !keyDownIsPressed) {
                    gameBoard.rightPlayer.getTank().move(KeyCode.UP);
                }
                if (keyDownIsPressed && !keyUpIsPressed) {
                    gameBoard.rightPlayer.getTank().move(KeyCode.DOWN);
                }
                if (keyLeftIsPressed && !keyRightIsPressed) {
                    gameBoard.rightPlayer.getTank().rotateBarrel(KeyCode.LEFT);
                    System.out.println("Barrel Angle (L): " + gameBoard.rightPlayer.getTank().getBarrelAngle());
                }
                if (keyRightIsPressed && !keyLeftIsPressed) {
                    gameBoard.rightPlayer.getTank().rotateBarrel(KeyCode.RIGHT);
                    System.out.println("Barrel Angle (L): " + gameBoard.rightPlayer.getTank().getBarrelAngle());
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

    @FXML
    private void sectionConfigurationPressed(){
        Tab tabGameMark = tabPane.getSelectionModel().getSelectedItem();
        if(tabGameMark.getText().equals(tabPane.getTabs().get(1).getText())){
            List<TextField> listOfTextFields = new ArrayList<>();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setTranslateX(0);
            scrollPane.setTranslateY(0);
            scrollPane.setPrefWidth(800);
            GridPane gridPane = new GridPane();
            gridPane.setPrefWidth(785);
            gridPane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
            for(int i = 0; i < GameSettings.getConfigurationList().size(); i++){
                Label tmpLabel = new Label(GameSettings.getConfigurationList().get(i)[0]);
                tmpLabel.setStyle("-fx-font-size: 20px");
                Button tmpBtn = new Button("Save " + GameSettings.getConfigurationList().get(i)[1]);
                TextField tmpTextField = new TextField();
                tmpTextField.setPrefWidth(50);
                tmpTextField.setText(GameSettings.getConfigurationList().get(i)[2]);
                tmpTextField.setId(GameSettings.getConfigurationList().get(i)[1]);
                gridPane.add(tmpLabel,0, i);
                gridPane.add(tmpTextField,1, i);
                listOfTextFields.add(tmpTextField);
                gridPane.add(tmpBtn,2, i);
                gridPane.setVgap(40);
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
