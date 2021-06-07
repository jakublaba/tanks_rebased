import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Colony {
    private final List<Cell> cells;
    private int initialCellHpSum;

    public Colony () {
        cells = new ArrayList<>();
        double colonyCenter = ThreadLocalRandom.current().nextDouble(GameSettings.WidthOfTankBorder + 2 * GameSettings.CellSize, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder - 1.5 * GameSettings.CellSize);
        int colonySize = ThreadLocalRandom.current().nextInt(2, 5);
        List<Integer> usedPosition = new ArrayList<>();
        Cell centerCell = new Cell(colonyCenter - GameSettings.CellSize/2, 0, GameSettings.CellSize, GameSettings.CellVelocity, 5, ThreadLocalRandom.current().nextInt(1,9));
        cells.add(centerCell);
        for (int i = 0; i < colonySize; i++){
            int colonyArrangement = ThreadLocalRandom.current().nextInt(1,8);
            while(usedPosition.contains(colonyArrangement))
                colonyArrangement = ThreadLocalRandom.current().nextInt(1,8);
            Cell cell = null;
            switch (colonyArrangement) {
                case 1 -> cell = new Cell(colonyCenter - 1.5 * GameSettings.CellSize, -GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity, 1, ThreadLocalRandom.current().nextInt(1,9));
                case 2 -> cell = new Cell(colonyCenter - 0.5 * GameSettings.CellSize, -GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity, 2, ThreadLocalRandom.current().nextInt(1,9));
                case 3 -> cell = new Cell(colonyCenter + 0.5 * GameSettings.CellSize, -GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity, 3, ThreadLocalRandom.current().nextInt(1,9));
                case 4 -> cell = new Cell(colonyCenter - 1.5 * GameSettings.CellSize, 0, GameSettings.CellSize, GameSettings.CellVelocity,4, ThreadLocalRandom.current().nextInt(1,9));
                case 5 -> cell = new Cell(colonyCenter + 0.5 * GameSettings.CellSize, 0, GameSettings.CellSize, GameSettings.CellVelocity,6, ThreadLocalRandom.current().nextInt(1,9));
                case 6 -> cell = new Cell(colonyCenter - 1.5 * GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity,7, ThreadLocalRandom.current().nextInt(1,9));
                case 7 -> cell = new Cell(colonyCenter - 0.5 * GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity,8, ThreadLocalRandom.current().nextInt(1,9));
                case 8 -> cell = new Cell(colonyCenter + 0.5 * GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellSize, GameSettings.CellVelocity,9, ThreadLocalRandom.current().nextInt(1,9));
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
    public boolean regenerate(){
        boolean playSound = false;
        for(Cell cell : cells){
            if(cell.regenerate()){
                playSound = true;
            }
        }
        return playSound;
    }

    public void resize () {
        for(Cell cell : cells){
            cell.resize();
            if(cell.getPosition() != 0) {
                switch(cell.getPosition()) {
                    case 1:
                        cell.getSegmentShape().setX(cell.getSegmentShape().getX() + GameSettings.CellSizeDecrease);
                        cell.getSegmentShape().setY(cell.getSegmentShape().getY() + GameSettings.CellSizeDecrease);
                        cell.setX(cell.getX() + GameSettings.CellSizeDecrease);
                        cell.setY(cell.getY() + GameSettings.CellSizeDecrease);
                        break;
                    case 2:
                        cell.getSegmentShape().setY(cell.getSegmentShape().getY() + GameSettings.CellSizeDecrease);
                        cell.setY(cell.getY() + GameSettings.CellSizeDecrease);
                        break;
                    case 3:
                        cell.getSegmentShape().setX(cell.getSegmentShape().getX() - GameSettings.CellSizeDecrease );
                        cell.getSegmentShape().setY(cell.getSegmentShape().getY() + GameSettings.CellSizeDecrease );
                        cell.setX(cell.getX() - GameSettings.CellSizeDecrease);
                        cell.setY(cell.getY() + GameSettings.CellSizeDecrease);
                        break;
                    case 4:
                        cell.getSegmentShape().setX(cell.getSegmentShape().getX() + GameSettings.CellSizeDecrease );
                        cell.setX(cell.getX() + GameSettings.CellSizeDecrease);
                        break;
                    case 5:
                        break;
                    case 6:
                        cell.getSegmentShape().setX(cell.getSegmentShape().getX() - GameSettings.CellSizeDecrease);
                        cell.setX(cell.getX() - GameSettings.CellSizeDecrease);
                        break;
                    case 7:
                        cell.getSegmentShape().setX(cell.getSegmentShape().getX() + GameSettings.CellSizeDecrease);
                        cell.getSegmentShape().setY(cell.getSegmentShape().getY() - GameSettings.CellSizeDecrease);
                        cell.setX(cell.getX() + GameSettings.CellSizeDecrease);
                        cell.setY(cell.getY() - GameSettings.CellSizeDecrease);
                        break;
                    case 8:
                        cell.getSegmentShape().setY(cell.getSegmentShape().getY() - GameSettings.CellSizeDecrease);
                        cell.setY(cell.getY() - GameSettings.CellSizeDecrease);
                        break;
                    case 9:
                        cell.getSegmentShape().setX(cell.getSegmentShape().getX() - GameSettings.CellSizeDecrease);
                        cell.getSegmentShape().setY(cell.getSegmentShape().getY() - GameSettings.CellSizeDecrease);
                        cell.setX(cell.getX() - GameSettings.CellSizeDecrease);
                        cell.setY(cell.getY() - GameSettings.CellSizeDecrease);
                        break;
                }
            }
            cell.getLabel().setTranslateX(cell.getX() - cell.getCurrentSize()/2);
        }
    }

    public List<Cell> getCells() { return cells; }
}