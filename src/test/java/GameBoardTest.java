import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
>>>>>>> 4807d1a (testy i resetowanie planszy)

public class GameBoardTest {
    private GameBoard gameBoard;
    private List<Cell> cells;
    private List<Colony> colonies;

    @BeforeEach
    public void gameBoardTestSetup() {
        cells = new ArrayList<>();
        colonies = new ArrayList<>();
    }

    @Test
    public void cellAboveBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - 1, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
<<<<<<< HEAD
        gameBoard.eraseObjects();
        Assertions.assertEquals(1, gameBoard.getCells().size());
=======
        gameBoard.removeCells();
        assertEquals(1, gameBoard.getCells().size());
>>>>>>> 4807d1a (testy i resetowanie planszy)
    }

    @Test
    public void cellOnBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
<<<<<<< HEAD
        gameBoard.eraseObjects();
        Assertions.assertEquals(1, gameBoard.getCells().size());
=======
        gameBoard.removeCells();
        assertEquals(1, gameBoard.getCells().size());
>>>>>>> 4807d1a (testy i resetowanie planszy)
    }

    @Test
    public void cellUnderBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder + 1, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
<<<<<<< HEAD
        gameBoard.eraseObjects();
        Assertions.assertEquals(0, gameBoard.getCells().size());
=======
        gameBoard.removeCells();
        assertEquals(0, gameBoard.getCells().size());
>>>>>>> 4807d1a (testy i resetowanie planszy)
    }

    @Test
    public void cellTooReduced_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder, 0, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
<<<<<<< HEAD
        gameBoard.eraseObjects();
        Assertions.assertEquals(0, gameBoard.getCells().size());
=======
        gameBoard.removeCells();
        assertEquals(0, gameBoard.getCells().size());
>>>>>>> 4807d1a (testy i resetowanie planszy)
    }

    @Test
    public void cellNotAliveBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder + 1, GameSettings.CellSize, 0);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
<<<<<<< HEAD
        gameBoard.eraseObjects();
        Assertions.assertEquals(0, gameBoard.getCells().size());
=======
        gameBoard.removeCells();
        assertEquals(0, gameBoard.getCells().size());
>>>>>>> 4807d1a (testy i resetowanie planszy)
    }
}
