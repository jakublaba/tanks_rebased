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
    private final double barrelLength;
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
        barrelAngle = side == 'R' ? 180 : 0;
        bullets = new ArrayList<Bullet>();
        bodyImg = new Image(getClass().getResource(GameSettings.TANK_BODY_IMG).toExternalForm());
        bodyView = new ImageView(bodyImg);
        String imgChoice = GameSettings.TANK_BARREL_IMG;
        barrelImg = new Image(getClass().getResource(imgChoice).toExternalForm());
        barrelView = new ImageView(barrelImg);
        barrelLength = barrelImg.getWidth()/2;
        if(side == 'R') {
            barrelView.setRotate(180);
        }
    }
    public void draw (Pane pane) {
        if (side == 'L') {
            bodyView.setX(0);
            bodyView.setY(position - bodyImg.getHeight()/2);
            barrelView.setX(-90 + bodyImg.getWidth()/2);
            barrelView.setY(position - barrelImg.getHeight()/2);
        } else if (side == 'R') {
            bodyView.setX(GameSettings.WINDOW_WIDTH - bodyImg.getWidth());
            bodyView.setY(position - bodyImg.getHeight()/2);
            barrelView.setX(GameSettings.WINDOW_WIDTH -90 - bodyImg.getWidth()/2);
            barrelView.setY(position - barrelImg.getHeight()/2);
        }
        pane.getChildren().remove(bodyView);
        pane.getChildren().remove(barrelView);
        pane.getChildren().add(bodyView);
        pane.getChildren().add(barrelView);
        bullets.forEach(bullet -> bullet.draw(pane));
        bullets.forEach(Bullet::move);
    }
    public void move (KeyCode key) {
        if ((key == KeyCode.W && side == 'L' || key == KeyCode.UP && side == 'R') && position > bodyImg.getHeight()/2) {
            position -= GameSettings.TANK_VELOCITY;
            bodyView.setX(bodyView.getX()-GameSettings.TANK_VELOCITY);
        } else if ((key == KeyCode.S && side == 'L' || key == KeyCode.DOWN && side == 'R') && position < GameSettings.WINDOW_HEIGHT - bodyImg.getHeight()/2) {
            position += GameSettings.TANK_VELOCITY;
            bodyView.setX(bodyView.getX()+GameSettings.TANK_VELOCITY);
        }
    }
    public void rotateBarrel (KeyCode key) {
        if ((key == KeyCode.A && side == 'L' && barrelAngle > -GameSettings.BarrelAngleLimit) || (key == KeyCode.RIGHT && side == 'R' && barrelAngle - 180 > -GameSettings.BarrelAngleLimit )) {
            barrelAngle -= GameSettings.BARREL_ROTATION;
            barrelView.setRotate(barrelView.getRotate() + GameSettings.BARREL_ROTATION);
        } else if ((key == KeyCode.D && side == 'L' && barrelAngle < GameSettings.BarrelAngleLimit) || (key == KeyCode.LEFT && side == 'R' && barrelAngle - 180 < GameSettings.BarrelAngleLimit) ) {
            barrelAngle += GameSettings.BARREL_ROTATION;
            barrelView.setRotate(barrelView.getRotate() - GameSettings.BARREL_ROTATION);
        }
    }
    public void shoot () {
        double rsin = side == 'L' ? Math.sin(barrelAngle) : Math.sin(barrelAngle - 180);
        double rcos = side == 'L' ? Math.cos(barrelAngle) : Math.cos(barrelAngle - 180);
        double x0 = side == 'L' ? bodyImg.getWidth()/2 : GameSettings.WINDOW_WIDTH - bodyImg.getWidth()/2;
        double y0 = position;
        double x1 = rcos + x0;
        double y1 = barrelAngle >= 0 ? rsin + y0 : y0 - rsin;
        Bullet bulletFired = side == 'L' ? new Bullet(x1, y1, rcos, rsin) : new Bullet(x1, y1, -rcos, -rsin);
        bullets.add(bulletFired);
    }
    public void removeBullet (Bullet bulletToRemove) { bullets.remove(bulletToRemove); }
    public double getBarrelAngle () { return barrelAngle; }
    public ImageView getBarrelView () { return barrelView; }
}