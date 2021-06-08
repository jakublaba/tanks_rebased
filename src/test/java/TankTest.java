import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    private Tank testTank;
    private final double tankHeight = 115;
    @BeforeEach
    public void tankTestSetUp () {
        testTank = new Tank('L');
    }
    @Test
    public void upperMoveLimitTest () {
        double startPosition = testTank.getHeight()/2;
        testTank.setPosition(startPosition);
        testTank.move(GameSettings.LeftPlayerMoveUp);
        assertEquals(testTank.getPosition(), startPosition);
    }
    @Test
    public void lowerMoveLimitTest () {
        double startPosition = GameSettings.WindowHeight - testTank.getHeight()/2;
        testTank.setPosition(startPosition);
        testTank.move(GameSettings.LeftPlayerMoveDown);
        assertEquals(testTank.getPosition(), startPosition);
    }
}