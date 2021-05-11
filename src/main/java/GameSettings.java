import javafx.scene.paint.Color;

public interface GameSettings {
    double WINDOW_WIDTH = 800;
    double WINDOW_HEIGHT = 800;
    Color TANKS_FIELD_COLOR_ONE = Color.web("#3798D8"); //Zdefiniowane na później
    Color TANKS_FIELD_COLOR_TWO = Color.web("E4E8EB");
    Color WAR_FIELD_COLOR_ONE = Color.web("0D9068");
    Color WAR_FIELD_COLOR_TWO = Color.web("0D9068");

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

    //rozgrywka
    int GameTime = 66;
}