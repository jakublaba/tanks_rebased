import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    private final Tank tank;
    private int score;
    private Label scoreLabel;
    private static List<String> errorList = new ArrayList<>();;
    public static Label firstErrorLine;
    public static Label secondErrorLine;
    public static Label thirdErrorLine;

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

    public static void setErrorList(Pane pane){
        firstErrorLine = ControllerSetter.setLabel("",GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder/2 - 40, "css/tabLabel.css");
        secondErrorLine = ControllerSetter.setLabel("", GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder/2 -10, "css/tabLabel.css");
        thirdErrorLine = ControllerSetter.setLabel("", GameSettings.WidthOfTankBorder, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder/2 + 20, "css/tabLabel.css");
        ControllerSetter.addChildren(pane, firstErrorLine, secondErrorLine, thirdErrorLine);
        informAboutError(pane);
    }

    private static void informAboutError(Pane pane){
        Thread errorThread = new Thread(()-> {
            AnimationTimer at = new AnimationTimer() {
                long timeBetween = 0;
                double transparentRatio = 0.9;
                @Override
                public synchronized void handle(long time) {
                    if(time - timeBetween >= 6.0 *  1_000_000_000){
                        transparentRatio = 0.9;
                        thirdErrorLine.setStyle("-fx-text-fill: rgba(255, 255, 255, 1);");
                        secondErrorLine.setStyle("-fx-text-fill: rgba(255, 255, 255, 1);");
                        firstErrorLine.setStyle("-fx-text-fill: rgba(255, 255, 255, 1);");
                        if(errorList.size() >= 3){
                            firstErrorLine.setText(errorList.get(0));
                            secondErrorLine.setText(errorList.get(1));
                            thirdErrorLine.setText(errorList.get(2));
                            errorList.remove(2);
                            errorList.remove(1);
                            errorList.remove(0);
                        }
                        else if(errorList.size() == 2){
                            thirdErrorLine.setText(firstErrorLine.getText());
                            secondErrorLine.setText(errorList.get(1));
                            firstErrorLine.setText(errorList.get(0));
                            errorList.remove(1);
                            errorList.remove(0);
                        }
                        else if(errorList.size() == 1){
                            thirdErrorLine.setText(secondErrorLine.getText());
                            secondErrorLine.setText(firstErrorLine.getText());
                            firstErrorLine.setText(errorList.get(0));
                            errorList.remove(0);
                        }
                        else {
                            thirdErrorLine.setText("< ALL RIGHT - NO INFO > ");
                            secondErrorLine.setText("");
                            firstErrorLine.setText("");
                        }
                        timeBetween = time;
                    }
                    firstErrorLine.setStyle("-fx-text-fill: rgba(255, 255, 255," + transparentRatio + ");");
                    secondErrorLine.setStyle("-fx-text-fill: rgba(255, 255, 255," + transparentRatio + ");");
                    thirdErrorLine.setStyle("-fx-text-fill: rgba(255, 255, 255," + transparentRatio + ");");
                    transparentRatio = transparentRatio - 0.003 < 0 ? 0 : transparentRatio - 0.003;
                }
            };
            at.start();
        });
        errorThread.start();
    }
    public static void addInformation(String error){
        errorList.add(error);
    }

}