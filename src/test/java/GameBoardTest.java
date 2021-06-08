import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        gameBoard.removeCells();
        assertEquals(1, gameBoard.getCells().size());
    }

    @Test
    public void cellOnBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.removeCells();
        assertEquals(1, gameBoard.getCells().size());
    }

    @Test
    public void cellUnderBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder + 1, GameSettings.CellSize, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.removeCells();
        assertEquals(0, gameBoard.getCells().size());
    }

    @Test
    public void cellTooReduced_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder, 0, 1);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.removeCells();
        assertEquals(0, gameBoard.getCells().size());
    }

    @Test
    public void cellNotAliveBorderLine_removeCell() {
        Cell cell = new Cell(GameSettings.WindowWidth / 2, GameSettings.WindowHeight - GameSettings.WidthOfTankBorder + 1, GameSettings.CellSize, 0);
        cells.add(cell);
        gameBoard = new GameBoard(cells, colonies);
        gameBoard.removeCells();
        assertEquals(0, gameBoard.getCells().size());
    }
}
