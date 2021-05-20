import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Tank {
    private final char side;
    private double position;
    private double barrelAngle;
    private final List<Bullet> bullets;
    private final Image bodyImg, barrelImg;
    private final ImageView bodyView, barrelView;

    public Tank (char side) {
        if (side != 'L' && side != 'R' && side != 'T') {
            System.out.println("Czołg może być tylko na lewej lub prawej ścianie, podaj prawidłowy argument");
            System.exit(1);
        }
        this.side = side;
        position = GameSettings.WindowHeight / 2;
        barrelAngle = side == 'R' ? 180 : 0;
        bullets = new ArrayList<>();
        bodyImg = new Image(getClass().getResource(GameSettings.TankBodyImg).toExternalForm());
        bodyView = new ImageView(bodyImg);
        barrelImg = new Image(getClass().getResource(GameSettings.TankBarrelImg).toExternalForm());
        barrelView = new ImageView(barrelImg);
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
            bodyView.setX(GameSettings.WindowWidth - bodyImg.getWidth());
            bodyView.setY(position - bodyImg.getHeight()/2);
            barrelView.setX(GameSettings.WindowWidth -90 - bodyImg.getWidth()/2);
            barrelView.setY(position - barrelImg.getHeight()/2);
        }
        pane.getChildren().remove(bodyView);
        pane.getChildren().remove(barrelView);
        pane.getChildren().add(bodyView);
        pane.getChildren().add(barrelView);
        bullets.forEach(bullet -> bullet.draw(pane));
        for (Bullet bullet : bullets) {
            bullet.move();
            if (side == 'L' && bullet.getX() > GameSettings.WindowWidth - GameSettings.WidthOfTankBorder || side == 'R' && bullet.getX() < GameSettings.WidthOfTankBorder || (bullet.getY() < 0 || bullet.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder)) {
                bullet.erase(pane);
                removeBullet(bullet);
                //System.out.println("Current amount of bullets: " + bullets.size());
                break; //bez tego występuje ConcurrentModificationException - usuwamy elementy z listy, po której nadal iterujemy
            }
        }
    }
    public void move (KeyCode key) {
        if ((key == GameSettings.LeftPlayerMoveUp && side == 'L' || key == GameSettings.RightPlayerMoveUp && side == 'R') && position > bodyImg.getHeight()/2) {
            position -= GameSettings.TankVelocity;
        } else if ((key == GameSettings.LeftPlayerMoveDown && side == 'L' || key == GameSettings.RightPlayerMoveDown && side == 'R') && position < GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - bodyImg.getHeight()/2) {
            position += GameSettings.TankVelocity;
        }
    }
    public void rotateBarrel (KeyCode key) {
        if ((key == GameSettings.LeftPlayerBarrelDown && side == 'L' && barrelAngle > -GameSettings.BarrelAngleLimit) || (key == GameSettings.RightPlayerBarrelDown && side == 'R' && barrelAngle - 180 > -GameSettings.BarrelAngleLimit )) {
            barrelAngle -= GameSettings.BarrelRotation;
            barrelView.setRotate(barrelView.getRotate() + GameSettings.BarrelRotation);
        } else if ((key == GameSettings.LeftPlayerBarrelUp && side == 'L' && barrelAngle < GameSettings.BarrelAngleLimit) || (key == GameSettings.RightPlayerBarrelUp && side == 'R' && barrelAngle - 180 < GameSettings.BarrelAngleLimit) ) {
            barrelAngle += GameSettings.BarrelRotation;
            barrelView.setRotate(barrelView.getRotate() - GameSettings.BarrelRotation);
        }
    }
    public void shoot () {
        double r = barrelImg.getWidth()/2;
        double alfa = Math.toRadians(barrelAngle);
        double x0 = side == 'L' ? bodyImg.getWidth()/2 : GameSettings.WindowWidth - bodyImg.getWidth()/2;
        double y0 = position;
        double x1 = r*Math.cos(alfa) + x0;
        double y1 = y0 - r*Math.sin(alfa);
        double vectorX = x1 - x0;
        double vectorY = y1 - y0;
        Bullet bulletFired = new Bullet(x1, y1, vectorX, vectorY);
        bullets.add(bulletFired);
    }
    public void removeBullet (Bullet bulletToRemove) { bullets.remove(bulletToRemove); }
    public double getBarrelAngle () { return barrelAngle; }
    public List<Bullet> getBullets () { return bullets; }
}