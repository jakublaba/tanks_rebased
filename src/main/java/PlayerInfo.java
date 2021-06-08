import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    private final Tank tank;
    private int score;
    private Label scoreLabel;
    private static final List<String> errorList = new ArrayList<>();
    public static Label firstErrorLine;
    private static double firstTransparentRatio = 1;
    public static Label secondErrorLine;
    private static double secondTransparentRatio = 1;
    public static Label thirdErrorLine;
    private static double thirdTransparentRatio = 1;

    public PlayerInfo (char side) {
        tank = new Tank(side);
        score = 0;
        if(side == 'L') {
            scoreLabel = ControllerSetter.setLabel(String.valueOf(score), 10, (int)GameSettings.WindowHeight - 100);
            scoreLabel.setAlignment(Pos.TOP_LEFT);
            scoreLabel.setStyle("-fx-font-size: 70px; -fx-text-fill: rgba(204, 0, 0, 0.7); -fx-font-weight: bold;");
        }
        else if(side == 'R'){
            scoreLabel = ControllerSetter.setLabel(String.valueOf(score), (int)GameSettings.WindowWidth - 210, (int)GameSettings.WindowHeight - 100);
            scoreLabel.setAlignment(Pos.TOP_RIGHT);
            scoreLabel.setStyle("-fx-font-size: 70px; -fx-text-fill: rgba(0, 102, 255, 0.7); -fx-font-weight: bold;");
        }
        scoreLabel.setPrefWidth(200);
    }
    public Tank getTank () { return tank; }
    public void increaseScore (int points) { score += points; }
    public void drawScore(Pane pane){
        pane.getChildren().remove(scoreLabel);
        scoreLabel.setText(String.valueOf(score));
        pane.getChildren().add(scoreLabel);
    }
    public int getScore(){
        return score;
    }

    public static void updateErrorInformation() {
        double ratio = 0.005;
        if (errorList.size() != 0) {
            if(firstTransparentRatio == 1){
                firstErrorLine.setText(errorList.get(0));
                errorList.remove(0);
            }
            else if(secondTransparentRatio == 1){
                secondErrorLine.setText(errorList.get(0));
                errorList.remove(0);
            }
            else if(thirdTransparentRatio == 1){
                thirdErrorLine.setText(errorList.get(0));
                errorList.remove(0);
            }
        }
        if (!firstErrorLine.getText().equals("")) {
            firstTransparentRatio = firstTransparentRatio - ratio < 0 ? 0 : firstTransparentRatio - ratio;
            firstErrorLine.setStyle("-fx-text-fill: rgba(255,255,255," + firstTransparentRatio + ");");
            if(firstTransparentRatio == 0){
                firstErrorLine.setText("");
                firstTransparentRatio = 1;
            }
        }
        if (!secondErrorLine.getText().equals("")) {
            secondTransparentRatio = secondTransparentRatio - ratio < 0 ? 0 : secondTransparentRatio - ratio;
            secondErrorLine.setStyle("-fx-text-fill: rgba(255,255,255," + secondTransparentRatio + ");");
            if(secondTransparentRatio == 0){
                secondErrorLine.setText("");
                secondTransparentRatio = 1;
            }
        }
        if (!thirdErrorLine.getText().equals("")) {
            thirdTransparentRatio = thirdTransparentRatio - ratio < 0 ? 0 : thirdTransparentRatio - ratio;
            thirdErrorLine.setStyle("-fx-text-fill: rgba(255,255,255," + thirdTransparentRatio + ");");
            if(thirdTransparentRatio == 0){
                thirdErrorLine.setText("");
                thirdTransparentRatio = 1;
            }
        }
    }

    public static void setErrorList(Pane pane){
        firstErrorLine = ControllerSetter.setLabel("", GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder/2 - 40, "minLabel", "css/tabLabel.css");
        secondErrorLine = ControllerSetter.setLabel("", GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder/2 -10, "minLabel", "css/tabLabel.css");
        thirdErrorLine = ControllerSetter.setLabel("", GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder/2 + 20, "minLabel", "css/tabLabel.css");
        ControllerSetter.addChildren(pane, firstErrorLine, secondErrorLine, thirdErrorLine);
    }

    public static void addInformation(String error) {
        errorList.add(error);
    }

}