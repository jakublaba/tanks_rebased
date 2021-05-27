import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Colony {
    private final List<Cell> cells;
    private int initialCellHpSum;

    public Colony () {
        cells = new ArrayList<>();
        double colonyCenter = ThreadLocalRandom.current().nextDouble(GameSettings.WidthOfTankBorder + 1.5 * GameSettings.CellSize, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder - 1.5 * GameSettings.CellSize);
        int colonySize = ThreadLocalRandom.current().nextInt(2, 5);
        List<Integer> usedPosition = new ArrayList<>();
        Cell centerCell = new Cell(colonyCenter - GameSettings.CellSize/2, 0, GameSettings.CellSize, GameSettings.CellVelocity, 5);
        cells.add(centerCell);
        for (int i = 0; i < colonySize; i++){
            int colonyArrangement = ThreadLocalRandom.current().nextInt(1,8);
            while(usedPosition.contains(colonyArrangement))
                colonyArrangement = ThreadLocalRandom.current().nextInt(1,8);
            Cell cell = null;
            switch (colonyArrangement) {
                case 1 -> cell = new Cell(colonyCenter - 1.5 * GameSettings.CellSize, -GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity, 1);
                case 2 -> cell = new Cell(colonyCenter - 0.5 * GameSettings.CellSize, -GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity, 2);
                case 3 -> cell = new Cell(colonyCenter + 0.5 * GameSettings.CellSize, -GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity, 3);
                case 4 -> cell = new Cell(colonyCenter - 1.5 * GameSettings.CellSize, 0, GameSettings.CellSize, GameSettings.CellVelocity,4);
                case 5 -> cell = new Cell(colonyCenter + 0.5 * GameSettings.CellSize, 0, GameSettings.CellSize, GameSettings.CellVelocity,6);
                case 6 -> cell = new Cell(colonyCenter - 1.5 * GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity,7);
                case 7 -> cell = new Cell(colonyCenter - 0.5 * GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity,8);
                case 8 -> cell = new Cell(colonyCenter + 0.5 * GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity,9);
                default -> System.out.println("Warnings: Out of Range");
            }
            cells.add(cell);
            usedPosition.add(colonyArrangement);
            assert cell != null;
            initialCellHpSum += cell.getInitialHp();
        }
    }
    public int getInitialCellHpSum () { return initialCellHpSum; }
    public boolean isColonyAlive () { return cells.size() != 0; }
    public void draw (Pane pane) {
        for (Cell x : cells){
            x.draw(pane);
        }
    }
    public void move (double timeBetween) {
        for (Cell x : cells){
            x.move(timeBetween);
        }
    }

    public void resize () {
        for(Cell x : cells){
            x.resize();
            if(x.getPosition() != 0){
                switch(x.getPosition()){
                    case 1:
                        x.getSegmentShape().setX(x.getSegmentShape().getX() + GameSettings.CellSizeDecrease);
                        x.getSegmentShape().setY(x.getSegmentShape().getY() + GameSettings.CellSizeDecrease);
                        x.setX(x.getX() + GameSettings.CellSizeDecrease);
                        x.setY(x.getY() + GameSettings.CellSizeDecrease);
                        break;
                    case 2:
                        x.getSegmentShape().setY(x.getSegmentShape().getY() + GameSettings.CellSizeDecrease);
                        x.setY(x.getY() + GameSettings.CellSizeDecrease);
                        break;
                    case 3:
                        x.getSegmentShape().setX(x.getSegmentShape().getX() - GameSettings.CellSizeDecrease );
                        x.getSegmentShape().setY(x.getSegmentShape().getY() + GameSettings.CellSizeDecrease );
                        x.setX(x.getX() - GameSettings.CellSizeDecrease);
                        x.setY(x.getY() + GameSettings.CellSizeDecrease);
                        break;
                    case 4:
                        x.getSegmentShape().setX(x.getSegmentShape().getX() + GameSettings.CellSizeDecrease );
                        x.setX(x.getX() + GameSettings.CellSizeDecrease);
                        break;
                    case 5:
                        break;
                    case 6:
                        x.getSegmentShape().setX(x.getSegmentShape().getX() - GameSettings.CellSizeDecrease);
                        x.setX(x.getX() - GameSettings.CellSizeDecrease);
                        break;
                    case 7:
                        x.getSegmentShape().setX(x.getSegmentShape().getX() + GameSettings.CellSizeDecrease);
                        x.getSegmentShape().setY(x.getSegmentShape().getY() - GameSettings.CellSizeDecrease);
                        x.setX(x.getX() + GameSettings.CellSizeDecrease);
                        x.setY(x.getY() - GameSettings.CellSizeDecrease);
                        break;
                    case 8:
                        x.getSegmentShape().setY(x.getSegmentShape().getY() - GameSettings.CellSizeDecrease);
                        x.setY(x.getY() - GameSettings.CellSizeDecrease);
                        break;
                    case 9:
                        x.getSegmentShape().setX(x.getSegmentShape().getX() - GameSettings.CellSizeDecrease);
                        x.getSegmentShape().setY(x.getSegmentShape().getY() - GameSettings.CellSizeDecrease);
                        x.setX(x.getX() - GameSettings.CellSizeDecrease);
                        x.setY(x.getY() - GameSettings.CellSizeDecrease);
                        break;
                }
            }
            x.getLabel().setTranslateX(x.getX() - x.getCurrentSize()/2);
        }
    }
}