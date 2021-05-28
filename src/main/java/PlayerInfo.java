import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    private final Tank tank;
    private int score;
    private Label scoreLabel;
    private static List<String> errorList;

    public PlayerInfo (char side) {
        tank = new Tank(side);
        score = 0;
        if(side == 'L') {
            scoreLabel = ControllerSetter.setLabel(String.valueOf(score), 20, (int)GameSettings.WindowHeight - 100);
            scoreLabel.setStyle("-fx-font-size: 70px; -fx-text-fill: rgba(204, 0, 0, 0.7); -fx-font-weight: bold;");
        }
        else if(side == 'R'){
            scoreLabel = ControllerSetter.setLabel(String.valueOf(score), (int)GameSettings.WindowWidth - 70, (int)GameSettings.WindowHeight - 100);
            scoreLabel.setStyle("-fx-font-size: 70px; -fx-text-fill: rgba(0, 102, 255, 0.7); -fx-font-weight: bold;");
        }
    }
    public Tank getTank () { return tank; }
    public void increaseScore (int points) { score += points; }
    public void drawScore(Pane pane, char side){
        pane.getChildren().remove(scoreLabel);
        scoreLabel.setText(String.valueOf(score));
        pane.getChildren().add(scoreLabel);
    }
    public int getScore(){
        return score;
    }

    public static void setErrorList(){

        errorList = new ArrayList<>();
    }

    public static void informAboutError(Pane pane){
        Thread errorThread = new Thread(()-> {

        });
    }

    public static void addError(String error){
        errorList.add(error);
    }

}