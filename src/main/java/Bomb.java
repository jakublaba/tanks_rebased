import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Bomb {
    private static final double x = GameSettings.WindowWidth / 2;
    private static final double y = GameSettings.WindowHeight;
    private static double bombHeight = 50;
    private static double bombWidth = 100;

    private Bomb () {

    }
    public static void checkCollisionType () {}
    public static void draw (Pane pane) {
        Rectangle bomb = new Rectangle();
        bomb.setHeight(bombHeight);
        bomb.setWidth(bombWidth);
        bomb.setX(x - bombWidth/2);
        bomb.setY(y - GameSettings.WidthOfTankBorder - bombHeight);
        bomb.setFill(Color.BLACK);
        Line rightLine = new Line();
        rightLine.setStartX(x + bombWidth/2);
        rightLine.setStartY(y - GameSettings.WidthOfTankBorder);
        rightLine.setEndX(x + bombWidth/2);
        rightLine.setEndY(y - GameSettings.WidthOfTankBorder - bombHeight);
        rightLine.setStroke(Color.GRAY);
        rightLine.setStrokeWidth(5);
        Line leftLine = new Line();
        leftLine.setStartX(x - bombWidth/2);
        leftLine.setStartY(y - GameSettings.WidthOfTankBorder);
        leftLine.setEndX(x - bombWidth/2);
        leftLine.setEndY(y - GameSettings.WidthOfTankBorder - bombHeight);
        leftLine.setStroke(Color.GRAY);
        leftLine.setStrokeWidth(5);
        Line topLine = new Line();
        topLine.setStartX(x - bombWidth/2);
        topLine.setStartY(y - GameSettings.WidthOfTankBorder - bombHeight);
        topLine.setEndX(x + bombWidth/2);
        topLine.setEndY(y - GameSettings.WidthOfTankBorder - bombHeight);
        topLine.setStroke(Color.ORANGE);
        topLine.setStrokeWidth(5);
        pane.getChildren().add(bomb);
        pane.getChildren().add(rightLine);
        pane.getChildren().add(leftLine);
        pane.getChildren().add(topLine);
    }
}
