import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.List;

public final class Controller {
    //private List<Button> menuButtons;


    @FXML
    private void startButtonPressed() {
        System.out.println("Hej");
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
