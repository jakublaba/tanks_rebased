import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public final class Controller {
    //private List<Button> menuButtons;
    @FXML
    private javafx.scene.control.Button startBtn;
    public static Pane layerPane;
    public static AnimationTimer gameLoop;
    public static Label timerLabel;
    private int timeGame = GameSettings.GameTime;



    @FXML
    private void startButtonPressed() throws java.io.IOException {
            Stage Menu = (Stage) startBtn.getScene().getWindow();
            Menu.hide();
            Pane gameFiled = new Pane();
            Stage primaryStage = new Stage();
            BorderPane root = new BorderPane();
            layerPane = new Pane();
            layerPane.getChildren().add(gameFiled);
            root.setCenter(layerPane);
            Scene scene = new Scene(root, GameSettings.WINDOW_WIDTH, GameSettings.WINDOW_HEIGHT);
            primaryStage.setTitle("Tanks - GAME");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            setGameBoard();

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
        leftLine.setStartX(GameSettings.WINDOW_WIDTH-120);
        leftLine.setStartY(0);
        leftLine.setEndX(GameSettings.WINDOW_WIDTH-120);
        leftLine.setEndY(GameSettings.WINDOW_HEIGHT);
        timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.03); -fx-font-weight: bold;");
        timerLabel.setTranslateY(GameSettings.WINDOW_HEIGHT/3);
        layerPane.getChildren().add(rightLine);
        layerPane.getChildren().add(leftLine);
        layerPane.getChildren().add(timerLabel);

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
    private void loadConfigFile() {}
}
