import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public final class Bullet extends GameSegment {
    private Circle segmentShape;
    private final double dirX, dirY;
    public Bullet (double x, double y, double dirX, double dirY) {
        super(x, y, GameSettings.BULLET_SIZE, GameSettings.BULLET_VELOCITY);
        this.dirX = dirX;
        this.dirY = dirY;
        segmentShape = new Circle(x, y, currentSize);
        segmentShape.setCenterX(x);
        segmentShape.setCenterY(y);
        segmentShape.setFill(GameSettings.BULLET_COLOR);
    }
    @Override
    public void move() {
        double vectorLength = Math.sqrt(dirX*dirX + dirY*dirY);
        double normalisedX = dirX / vectorLength;
        double normalisedY = dirY / vectorLength;
        x += normalisedX*currentVelocity;
        y += normalisedY*currentVelocity;
    }
    @Override
    public void draw(Pane pane) {
        pane.getChildren().remove(segmentShape);
        segmentShape.setCenterX(x);
        segmentShape.setCenterY(y);
        pane.getChildren().add(segmentShape);
    }
}
