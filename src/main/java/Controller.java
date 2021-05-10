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
        timerLabel = new Label((GameSettings.GameTime - GameSettings.GameTime%60)/60 + ":" + GameSettings.GameTime%60);
        timerLabel.setStyle("-fx-font-size: 12em; -fx-text-fill: rgba(153, 0, 76, 0.1); -fx-font-weight: bold");
        timerLabel.setPrefWidth(300);
        timerLabel.setTranslateX(GameSettings.WINDOW_WIDTH/2 - 150);
        timerLabel.setTranslateY(GameSettings.WINDOW_HEIGHT/4);
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
