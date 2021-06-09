import javafx.scene.layout.Pane;

public abstract class GameSegment {
    protected double x, y, currentSize, currentVelocity;

    public GameSegment(double x, double y, double initialSize, double initialVelocity) {
        this.x = x;
        this.y = y;
        currentSize = initialSize;
        currentVelocity = initialVelocity;
    }

    protected double getX() {
        return x;
    }

    protected double getY() {
        return y;
    }

    public double getCurrentSize() {
        return currentSize;
    }

    public void draw(Pane pane) {
    }

    protected void move() {
    }

    protected void resize() {
    }
}