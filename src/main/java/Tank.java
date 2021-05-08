import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class Tank {
    private final char side;
    private double position;
    private double barrelAngle;
    private List<Bullet> bullets;
    private ImageView bodyImg, barrelImg;

    public Tank (char side) {
        if (side != 'L' && side != 'R') {
            System.out.println("Czołg może być tylko na lewej lub prawej ścianie, podaj prawidłowy argument");
            System.exit(1);
        }
        this.side = side;
        position = GameSettings.WINDOW_HEIGHT / 2;
        barrelAngle = 0;
        bullets = new ArrayList<Bullet>();
        bodyImg = new ImageView(getClass().getResource(GameSettings.TANK_BODY_IMG).toExternalForm());
        barrelImg = new ImageView(getClass().getResource(GameSettings.TANK_BARREL_IMG).toExternalForm());
    }
    public void draw () {}
    public void move (KeyCode key) {}
    public void rotateBarrel (KeyCode key) {}
    public void shoot () {}
    public void removeBullet () {}
}
