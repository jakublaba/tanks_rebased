import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class Tank {
    private final char side;
    private double position;
    private double barrelAngle;
    private List<Bullet> bullets;
    private Image bodyImg, barrelImg;
    private ImageView bodyView, barrelView;

    public Tank (char side) {
        if (side != 'L' && side != 'R' && side != 'T') {
            System.out.println("Czołg może być tylko na lewej lub prawej ścianie, podaj prawidłowy argument");
            System.exit(1);
        }
        this.side = side;
        position = GameSettings.WINDOW_HEIGHT / 2;
        barrelAngle = 0;
        bullets = new ArrayList<Bullet>();
        bodyImg = new Image(getClass().getResource(GameSettings.TANK_BODY_IMG).toExternalForm());
        bodyView = new ImageView(bodyImg);
        String imgChoice = side == 'L' ? GameSettings.TANK_BARREL_IMG_L : GameSettings.TANK_BARREL_IMG_R;
        if(side != 'T') barrelImg = new Image(getClass().getResource(imgChoice).toExternalForm());
        barrelView = new ImageView(barrelImg);
    }
    public void draw (Pane pane) {
        if (side == 'L') {
            bodyView.setX(0);
            bodyView.setY(position - bodyImg.getHeight()/2);
            barrelView.setX(15);
            barrelView.setY(position - barrelImg.getHeight()/2);
        } else if (side == 'R') {
            bodyView.setX(GameSettings.WINDOW_WIDTH - bodyImg.getWidth());
            bodyView.setY(position - bodyImg.getHeight()/2);
            barrelView.setX(GameSettings.WINDOW_WIDTH - barrelImg.getWidth() - 15);
            barrelView.setY(position - barrelImg.getHeight()/2);
        }

        pane.getChildren().remove(bodyView);
        pane.getChildren().remove(barrelView);
        pane.getChildren().add(bodyView);
        pane.getChildren().add(barrelView);
    }
    public void move (KeyCode key) {
        if ((key == KeyCode.W && side == 'L' || key == KeyCode.UP && side == 'R') && position > bodyImg.getHeight()/2) {
            position -= GameSettings.TANK_VELOCITY;
        } else if ((key == KeyCode.S && side == 'L' || key == KeyCode.DOWN && side == 'R') && position < GameSettings.WINDOW_HEIGHT - bodyImg.getHeight()/2) {
            position += GameSettings.TANK_VELOCITY;
        }
    }
    //przy wywołaniu metody move(), wieżyczka ucieka na boki jeśli jest obrócona - im większy kąt tym szybciej, jeszcze nie rozumiem czemu tak się dzieje
    public void rotateBarrel (KeyCode key) {
        Rotate r = new Rotate();
        r.setPivotX(bodyView.getX() + bodyImg.getWidth()/2);
        r.setPivotY(bodyView.getY() + bodyImg.getHeight()/2);
        barrelView.getTransforms().add(r);
        if ((key == KeyCode.A && side == 'L' || key == KeyCode.RIGHT && side == 'R') && barrelAngle < GameSettings.BarrelAngleLimit) {
            barrelAngle += GameSettings.BARREL_ROTATION;
            r.setAngle(GameSettings.BARREL_ROTATION);
        } else if ((key == KeyCode.D && side == 'L' || key == KeyCode.LEFT && side == 'R') && barrelAngle > -GameSettings.BarrelAngleLimit) {
            barrelAngle -= GameSettings.BARREL_ROTATION;
            r.setAngle(-GameSettings.BARREL_ROTATION);
        }
    }
    public void shoot () {}
    public void removeBullet () {}
}
