import javafx.scene.paint.Color;

public interface GameSettings {
    double WINDOW_WIDTH = 800;
    double WINDOW_HEIGHT = 800;

    //własności komórki
    double CELL_SIZE = 10;
    double CELL_VELOCITY = 5;
    int CELL_HEALTH = 3;
    Color [] CELL_COLOR_SEQUENCE = {Color.GREEN, Color.YELLOW, Color.RED};

    //własności pocisku
    double BULLET_SIZE = 8;
    double BULLET_VELOCITY = 15;
    Color BULLET_COLOR = Color.BLACK;

    //własności czołgu
    double BARREL_LENGTH = 5;
    String TANK_BODY_IMG = "graphics/tankbody.png";
    String TANK_BARREL_IMG = "graphics/tankhead.png";
}