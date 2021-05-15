import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameBoard {
    private List<Cell> cells;
    private Colony colony; //we wstępnych pracach nad projektem jedna - w późniejszej fazie to pole zostanie przekształcone w listę koloni
    private final PlayerInfo leftPlayer, rightPlayer;
    private long lastTimeOfGeneratingCell;
    private long lastTimeOfMoveCell;
    private long lastTimeOfDecrease;

    public GameBoard () {
        leftPlayer = new PlayerInfo('L');
        rightPlayer = new PlayerInfo('R');
        cells = new ArrayList<Cell>();
        //do inicjalizowania kolonii trzeba będzie zaimplementować jakiś algorytm wykrywający sąsiadujące komórki
        //colony = new Colony();
    }
    public void updateGame(long time, Pane pane) {
        if(time - lastTimeOfGeneratingCell >= GameSettings.TimeBetweenCellGenerating * 1_000_000_000){
            Cell cell = new Cell (false);
            cells.add(cell);
            lastTimeOfGeneratingCell = time;
        }
        //Co 15 000 000 nanosekund aktualizowana jest pozycja komórek => częstoliwość około 60 Hz
        if(time - lastTimeOfMoveCell >= 15_000_000) {
            double timeBetween = ((double)time - (double)lastTimeOfMoveCell)/1_000_000_000;
            //System.out.println("Czas między zmianami: " + timeBetween);
            for (Cell x : cells) {
                    x.move(timeBetween);
                    x.draw(pane);
            }
            cells.removeIf(x -> x.getY() > GameSettings.WINDOW_HEIGHT - GameSettings.WidthOfTankBorder);
            cells.removeIf(x -> x.getCurrentSize() < 0);

            lastTimeOfMoveCell = time;
        }

        if(time - lastTimeOfDecrease >= 1_000_000_000 * GameSettings.Interval){
            for (Cell x : cells) {
                x.resize();
            }
            lastTimeOfDecrease = time;
        }
    }
    public void cellCollision () {}
    public void bombCollision () {}
    public void removeCell () {

    }
    public void removeColony () {}
    private void addPoints(@NotNull PlayerInfo player, int points) { player.increaseScore(points); }
}
