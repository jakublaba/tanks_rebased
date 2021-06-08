import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    private Tank testTank;
    private final double tankHeight = 115;
    @BeforeEach
    public void tankTestSetUp () {
        testTank = new Tank('L', true);
    }
    @Test
    public void upperMoveLimitTest () {
        double startPosition = tankHeight/2;
        testTank.setPosition(startPosition);
        testTank.move(GameSettings.LeftPlayerMoveUp, tankHeight);
        assertEquals(testTank.getPosition(), startPosition);
    }
    @Test
    public void lowerMoveLimitTest () {
        double startPosition = GameSettings.WindowHeight - tankHeight/2;
        testTank.setPosition(startPosition);
        testTank.move(GameSettings.LeftPlayerMoveDown, tankHeight);
        assertEquals(testTank.getPosition(), startPosition);
    }
    @Test
    public void upperBarrelLimitTest () {
        testTank.setBarrelAngle(GameSettings.BarrelAngleLimit);
        testTank.rotateBarrel(GameSettings.LeftPlayerBarrelUp);
        assertEquals(testTank.getBarrelAngle(), GameSettings.BarrelAngleLimit);
    }
    @Test
    public void lowerBarrelLimitTest () {
        testTank.setBarrelAngle(-GameSettings.BarrelAngleLimit);
        testTank.rotateBarrel(GameSettings.LeftPlayerBarrelDown);
        assertEquals(testTank.getBarrelAngle(), -GameSettings.BarrelAngleLimit);
    }
}