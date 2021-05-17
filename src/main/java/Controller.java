import javafx.animation.AnimationTimer;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
    private Tank leftTank, rightTank, testTank;


    private boolean keyDIsPressed;
    private boolean keyAIsPressed;
    private boolean keyWIsPressed;
    private boolean keySIsPressed;
    private boolean keyUpIsPressed;
    private boolean keyDownIsPressed;
    private boolean keyRightIsPressed;
    private boolean keyLeftIsPressed;

    private KeyCode getKey(KeyEvent keyPressed) {
        return keyPressed.getCode();
    }
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
        Scene scene = new Scene(root, GameSettings.WINDOW_WIDTH, GameSettings.WINDOW_HEIGHT);
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

                    timerLabel.setTranslateX(GameSettings.WINDOW_WIDTH/2 - timerLabel.getWidth()/2);
                    lastTime = currentTime;
                    timeGame--;
                }
                //TANKS
                leftTank.draw(layerPane);
                if (keyWIsPressed && !keySIsPressed) {
                    leftTank.move(KeyCode.W);
                }
                if (keySIsPressed && !keyWIsPressed) {
                    leftTank.move(KeyCode.S);
                }
                if (keyAIsPressed && !keyDIsPressed) {
                    leftTank.rotateBarrel(KeyCode.A);
                }
                if (keyDIsPressed && !keyAIsPressed) {
                    leftTank.rotateBarrel(KeyCode.D);
                }

                rightTank.draw(layerPane);
                if (keyUpIsPressed && !keyDownIsPressed) {
                    rightTank.move(KeyCode.UP);
                }
                if (keyDownIsPressed && !keyUpIsPressed) {
                    rightTank.move(KeyCode.DOWN);
                }
                if (keyLeftIsPressed && !keyRightIsPressed) {
                    rightTank.rotateBarrel(KeyCode.LEFT);
                }
                if (keyRightIsPressed && !keyLeftIsPressed) {
                    rightTank.rotateBarrel(KeyCode.RIGHT);
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
        rightLine.setEndY(GameSettings.WINDOW_HEIGHT - GameSettings.WidthOfTankBorder);
        leftLine.setStartX(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        leftLine.setStartY(0);
        leftLine.setEndX(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        leftLine.setEndY(GameSettings.WINDOW_HEIGHT - GameSettings.WidthOfTankBorder);
        horizontalLine.setStartX(GameSettings.WidthOfTankBorder);
        horizontalLine.setStartY(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        horizontalLine.setEndX(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        horizontalLine.setEndY(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.03); -fx-font-weight: bold;");
        timerLabel.setTranslateY(GameSettings.WINDOW_HEIGHT/3);
        layerPane.getChildren().add(rightLine);
        layerPane.getChildren().add(leftLine);
        layerPane.getChildren().add(horizontalLine);
        layerPane.getChildren().add(timerLabel);
        leftTank = new Tank('L');
        rightTank = new Tank('R');
        testTank = new Tank('T');
        Bomb.draw(layerPane);
    }


    @FXML
    private void settingsButtonPressed() throws IOException {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings.fxml")));
            Stage stage = new Stage();
            stage.setTitle("SnakeFX - Settings");
            stage.setScene(new Scene(root, GameSettings.WINDOW_WIDTH, GameSettings.WINDOW_HEIGHT));
            stage.setResizable(false);
            stage.show();
            Stage stage1 = (Stage) startBtn.getScene().getWindow();
            stage1.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sectionConfigurationPressed(){
        Tab tabGameMark = tabPane.getSelectionModel().getSelectedItem();
        if(tabGameMark.getText().equals("Manual Configuration")){
            List<TextField> listOfTextFields = new ArrayList<>();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setTranslateX(0);
            scrollPane.setTranslateY(0);
            scrollPane.setPrefWidth(800);
            GridPane gridPane = new GridPane();
            gridPane.setPrefWidth(800);
            gridPane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
            for(int i = 0; i < GameSettings.configuration.length/2; i++){
                Label tmpLabel = new Label(GameSettings.configuration[i * 2]);
                tmpLabel.setStyle("-fx-font-size: 20px");
                Button tmpBtn = new Button("Save " + GameSettings.configuration[i*2 + 1]);
                TextField tmpTextField = new TextField();
                tmpTextField.setPrefWidth(50);
                gridPane.add(tmpLabel,0, i);
                gridPane.add(tmpTextField,1, i);
                listOfTextFields.add(tmpTextField);
                gridPane.add(tmpBtn,2, i);
                gridPane.setVgap(40);
                tmpBtn.setOnAction(event->{
                    for(TextField tx: listOfTextFields){
                        if(tx.getLayoutY() == tmpBtn.getLayoutY()){
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
            stage1.setScene(new Scene(root, GameSettings.WINDOW_WIDTH, GameSettings.WINDOW_HEIGHT));
            stage1.show();
            stage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseButtonPressed() {}

}