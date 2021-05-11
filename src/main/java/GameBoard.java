import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<Cell> cells;
    private Colony colony; //we wstępnych pracach nad projektem jedna - w późniejszej fazie to pole zostanie przekształcone w listę koloni
    private final PlayerInfo leftPlayer, rightPlayer;

    public GameBoard () {
        leftPlayer = new PlayerInfo('L');
        rightPlayer = new PlayerInfo('R');
        cells = new ArrayList<Cell>();
        //do inicjalizowania kolonii trzeba będzie zaimplementować jakiś algorytm wykrywający sąsiadujące komórki
        //colony = new Colony();
    }
    public void updateGame() {}
    public void cellCollision () {}
    public void bombCollision () {}
    public void removeCell () {}
    public void removeColony () {}
    private void addPoints(@NotNull PlayerInfo player, int points) { player.increaseScore(points); }
}
