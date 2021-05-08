import java.util.concurrent.ThreadLocalRandom;

public class Cell extends GameSegment {
    private final boolean memberOfColony;
    private final int initialHp;
    private int currentHp;

    public Cell (boolean memberOfColony) {
        super(ThreadLocalRandom.current().nextDouble(0, GameSettings.WINDOW_WIDTH), 0, GameSettings.CELL_SIZE, GameSettings.CELL_VELOCITY, GameSettings.CELL_COLOR_SEQUENCE[0]);
        this.memberOfColony = memberOfColony;
        this.initialHp = GameSettings.CELL_HEALTH;
    }

    public int getCurrentHp () { return currentHp; }

    private void regenerate () { currentHp++; }

    public int getDamaged () { return --currentHp; }
}
