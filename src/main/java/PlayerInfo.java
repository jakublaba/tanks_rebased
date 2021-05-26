import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PlayerInfo {
    private final Tank tank;
    private int score;
    private Label scoreLabel;

    public PlayerInfo (char side) {
        tank = new Tank(side);
        score = 0;
        if(side == 'L') {
            scoreLabel = ControllerSetter.setLabel(String.valueOf(score), 20, (int)GameSettings.WindowHeight - 100);
            scoreLabel.setStyle("-fx-font-size: 70px; -fx-text-fill: rgb(153, 0, 76); -fx-font-weight: bold;");
        }
        else if(side == 'R'){
            scoreLabel = ControllerSetter.setLabel(String.valueOf(score), (int)GameSettings.WindowWidth - 70, (int)GameSettings.WindowHeight - 100);
            scoreLabel.setStyle("-fx-font-size: 70px; -fx-text-fill: rgb(153, 0, 76); -fx-font-weight: bold;");
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
}