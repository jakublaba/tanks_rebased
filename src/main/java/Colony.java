import java.util.ArrayList;
import java.util.List;

public class Colony {
    private List<Cell> cells;
    private final int initialCellHpSum;

    public Colony (ArrayList<Cell> cells) {
        this.cells = cells;
        initialCellHpSum = this.cells.stream().mapToInt(Cell::getCurrentHp).sum();
    }
    public int getInitialCellHpSum () { return initialCellHpSum; }
    public boolean isColonyAlive () { return cells.size() != 0; }
    public void draw () { cells.forEach(GameSegment::draw); }
    public void move () { cells.forEach(GameSegment::move); }
    //w poniższej metodzie trzeba będzie napisać jakiś algorytm, żeby odpowiednio "zsunąć" ze sobą komórki, aby nie rozbić kolonii
    public void resize () { cells.forEach(GameSegment::resize); }
}
