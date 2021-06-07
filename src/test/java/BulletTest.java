import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {
    private Bullet bullet;

    @BeforeEach
    public void bulletTestSetup() {
        bullet = new Bullet(GameSettings.WindowWidth / 2, GameSettings.WindowHeight / 2, 5, 5);
    }

    @Test
    public void moveVectorNormalisationTest() {
        Bullet bullet2 = new Bullet(GameSettings.WindowWidth / 2, GameSettings.WindowHeight / 2, 1000, 1000);
        bullet.move();
        bullet2.move();
        assertEquals(bullet.getX(), bullet2.getX());
        assertEquals(bullet.getY(), bullet2.getY());
    }
}