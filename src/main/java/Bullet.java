import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class Bullet extends GameSegment {
    private final Circle segmentShape;
    private final double dirX, dirY;

    public Bullet(double x, double y, double dirX, double dirY, Color color) {
        super(x, y, GameSettings.BulletSize, GameSettings.BulletVelocity);
        this.dirX = dirX;
        this.dirY = dirY;
        segmentShape = new Circle(x, y, currentSize);
        segmentShape.setCenterX(x);
        segmentShape.setCenterY(y);
        segmentShape.setFill(color);
    }

    @Override
    public void move() {
        double vectorLength = Math.sqrt(dirX * dirX + dirY * dirY);
        double normalisedX = dirX / vectorLength;
        double normalisedY = dirY / vectorLength;
        x += normalisedX * GameSettings.BulletVelocity;
        y += normalisedY * GameSettings.BulletVelocity;
    }

    @Override
    public void draw(Pane pane) {
        pane.getChildren().remove(segmentShape);
        segmentShape.setCenterX(x);
        segmentShape.setCenterY(y);
        pane.getChildren().add(segmentShape);
    }

    public void erase(Pane pane) {
        pane.getChildren().remove(segmentShape);
    }

    @Override
    protected void resize() {
        this.currentSize -= GameSettings.BulletRadiusDecrease;
        this.segmentShape.setRadius(currentSize);
    }
}
