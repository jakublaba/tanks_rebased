import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


public final class Controller {
    @FXML
    private javafx.scene.control.Button startBtn;
    public static Pane layerPane;
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int timeGame;
    private GameBoard gameBoard = new GameBoard();
    private KeyCode keyCode;
    private Tank leftTank, rightTank, testTank;
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
        scene.setOnKeyPressed(keyEvent -> keyCode = getKey(keyEvent));
        scene.setOnKeyReleased(keyEvent -> keyCode = null);
        primaryStage.setTitle("Tanks");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        setGameBoard();
        GameSettings.loadConfigFile();
        timeGame = (int)(GameSettings.GameTime);

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
                if (keyCode == KeyCode.W || keyCode == KeyCode.S) {
                    leftTank.move(keyCode);
                }
                if (keyCode == KeyCode.A || keyCode == KeyCode.D) {
                    leftTank.rotateBarrel(keyCode);
                }

                rightTank.draw(layerPane);
                if (keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
                    rightTank.move(keyCode);
                }
                if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT) {
                    rightTank.rotateBarrel(keyCode);
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
        rightLine.setStartX(120);
        rightLine.setStartY(0);
        rightLine.setEndX(120);
        rightLine.setEndY(GameSettings.WINDOW_HEIGHT);
        leftLine.setStartX(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        leftLine.setStartY(0);
        leftLine.setEndX(GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder);
        leftLine.setEndY(GameSettings.WINDOW_HEIGHT);
        timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.03); -fx-font-weight: bold;");
        timerLabel.setTranslateY(GameSettings.WINDOW_HEIGHT/3);
        layerPane.getChildren().add(rightLine);
        layerPane.getChildren().add(leftLine);
        layerPane.getChildren().add(timerLabel);
        leftTank = new Tank('L');
        rightTank = new Tank('R');
        testTank = new Tank('T');
    }


    @FXML
    private void settingsButtonPressed() {
        System.out.println("Tu będą ustawienia");
    }

    @FXML
    private void exitButtonPressed() {
        System.exit(1);
    }

    private void pauseButtonPressed() {}

}