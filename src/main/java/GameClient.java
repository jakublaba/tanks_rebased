import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class GameClient extends Application {
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
            primaryStage.setTitle("Tanks");
            primaryStage.setScene(new Scene(root, GameSettings.WindowWidth, GameSettings.WindowHeight));
            primaryStage.setResizable(false);
            primaryStage.show();
            GameSettings.loadConfigFile();
            GameSettings.setConfigurationList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}