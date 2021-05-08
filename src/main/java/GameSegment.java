import javafx.scene.paint.Color;

public abstract class GameSegment {
    private double x, y, currentSize, currentVelocity;
    private Color currentColor;

    public GameSegment (double x, double y, double initialSize, double initialVelocity, Color initialColor) {
        this.x = x;
        this.y = y;
        currentSize = initialSize;
        currentVelocity = initialVelocity;
        currentColor = initialColor;
    }
    public double getX () { return x; }
    public double getY () { return y; }
    public double getCurrentSize () { return currentSize; }
    public void draw () {}
    protected void move () {}
    protected void resize () {}
}
