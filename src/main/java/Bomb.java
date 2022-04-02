import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Bomb {
    private static final double x = GameSettings.WindowWidth / 2;
    private static final double y = GameSettings.WindowHeight;
    public static final double height = 50;
    public static final double width = 100;

    private Bomb () {}
    public static boolean fatalCollision (Bullet bullet) {
        return Math.abs(GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - Bomb.height - bullet.getY()) <= 1;
    }
    public static void draw (Pane pane) {
        Rectangle bombImg = new Rectangle();
        bombImg.setHeight(height);
        bombImg.setWidth(width);
        bombImg.setX(x - width /2);
        bombImg.setY(y - GameSettings.WidthOfTankBorder - height);
        bombImg.setFill(Color.BLACK);
        bombImg.toFront();
        Line rightLine = new Line();
        rightLine.setStartX(x + width /2);
        rightLine.setStartY(y - GameSettings.WidthOfTankBorder);
        rightLine.setEndX(x + width /2);
        rightLine.setEndY(y - GameSettings.WidthOfTankBorder - height);
        rightLine.setStroke(Color.GRAY);
        rightLine.setStrokeWidth(5);
        Line leftLine = new Line();
        leftLine.setStartX(x - width /2);
        leftLine.setStartY(y - GameSettings.WidthOfTankBorder);
        leftLine.setEndX(x - width /2);
        leftLine.setEndY(y - GameSettings.WidthOfTankBorder - height);
        leftLine.setStroke(Color.GRAY);
        leftLine.setStrokeWidth(5);
        Line topLine = new Line();
        topLine.setStartX(x - width /2);
        topLine.setStartY(y - GameSettings.WidthOfTankBorder - height);
        topLine.setEndX(x + width /2);
        topLine.setEndY(y - GameSettings.WidthOfTankBorder - height);
        topLine.setStroke(Color.ORANGE);
        topLine.setStrokeWidth(5);
        pane.getChildren().add(bombImg);
        pane.getChildren().add(rightLine);
        pane.getChildren().add(leftLine);
        pane.getChildren().add(topLine);
    }
}
