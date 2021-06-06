import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;


public class GameBoardTest {
    private GameBoard gameBoard;
    private List<Cell> cells;
    private List<Colony> colonies;

    @BeforeEach
    public void setUp(){
        cells = new ArrayList<>();
        colonies = new ArrayList<>();
    }
    @Test
    public void CellAboveBorderLine_eraseObject(){
        Cell cell = new Cell(GameSettings.WindowWidth/2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - 1, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.eraseObjects();
        Assertions.assertEquals(1, gameBoard.getCellsList().size());
    }
    @Test
    public void CellOnBorderLine_eraseObject(){
        Cell cell = new Cell(GameSettings.WindowWidth/2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.eraseObjects();
        Assertions.assertEquals(1, gameBoard.getCellsList().size());
    }
    @Test
    public void CellUnderBorderLine_eraseObject(){
        Cell cell = new Cell(GameSettings.WindowWidth/2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder + 1, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.eraseObjects();
        Assertions.assertEquals(0, gameBoard.getCellsList().size());
    }
    @Test
    public void CellTooReduced_eraseObject(){
        Cell cell = new Cell(GameSettings.WindowWidth/2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder , 0, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.eraseObjects();
        Assertions.assertEquals(0, gameBoard.getCellsList().size());
    }
    @Test
    public void CellNotAliveBorderLine_eraseObject(){
        Cell cell = new Cell(GameSettings.WindowWidth/2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder + 1, GameSettings.CellSize, 0);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.eraseObjects();
        Assertions.assertEquals(0, gameBoard.getCellsList().size());
    }
}
