import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class GameSegment {
    protected double x, y, currentSize, currentVelocity;
    private Color currentColor;
    protected Rectangle segmentShape;

    public GameSegment (double x, double y, double initialSize, double initialVelocity) {
        this.x = x;
        this.y = y;
        currentSize = initialSize;
        currentVelocity = initialVelocity;
        //Zostawiłem to w tym konstruktorze, bo to chyba bedzie współna część dla Cell i Bullet
        segmentShape = new Rectangle();
        segmentShape.setWidth(initialSize);
        segmentShape.setHeight(initialSize);
        segmentShape.setX(x - initialSize/2);
        segmentShape.setY(y - initialSize/2);
    }
    protected double getX () { return x; }
    protected double getY () { return y; }
    public double getCurrentSize () { return currentSize; }
    public void draw (Pane pane) {
        segmentShape.setY(y);
        pane.getChildren().remove(segmentShape);
        if(y < GameSettings.WINDOW_HEIGHT - GameSettings.WidthOfTankBorder)
            pane.getChildren().add(segmentShape);
    }
    protected void move () {}
    protected void resize () {

    }
}
