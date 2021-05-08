public class Bullet extends GameSegment {
    private final double dirX, dirY;
    public Bullet (int x, int y, double dirX, double dirY) {
        super(x, y, GameSettings.BULLET_SIZE, GameSettings.BULLET_VELOCITY, GameSettings.BULLET_COLOR);
        this.dirX = dirX;
        this.dirY = dirY;
    }
}
