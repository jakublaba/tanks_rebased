import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private final List<Cell> cells;
    private  final List <Colony> colonies;
    public final PlayerInfo leftPlayer, rightPlayer;
    private long lastTimeOfGeneratingCell;
    private long lastTimeOfIncreaseHealth;
    private long lastTimeOfMoveCell;
    private long lastTimeOfDecrease;
    private long lastTimeOfGeneratingColony;

    public GameBoard () {
        leftPlayer = new PlayerInfo('L');
        rightPlayer = new PlayerInfo('R');
        cells = new ArrayList<>();
        colonies = new ArrayList<>();
    }
    public boolean updateGame(long time, Pane pane) {
        if (time - lastTimeOfGeneratingCell >= GameSettings.TimeBetweenCellGenerating * 1_000_000_000) {
            Cell cell = new Cell ();
            cells.add(cell);
            lastTimeOfGeneratingCell = time;
            leftPlayer.drawScore(pane, 'L');
            rightPlayer.drawScore(pane, 'R');
        }
        if (time - lastTimeOfGeneratingColony >= GameSettings.TimeBetweenColonyGeneration * 1_000_000_000) {
            Colony colony = new Colony();
            colonies.add(colony);
            lastTimeOfGeneratingColony = time;
        }
        if (time - lastTimeOfMoveCell >= 15_000_000) {
            double timeBetween = ((double)time - (double)lastTimeOfMoveCell)/1_000_000_000;
            for (Cell cell : cells) {
                cell.move(timeBetween);
                cell.draw(pane);
            }
            for (Colony colony : colonies){
                colony.move(timeBetween);
                colony.draw(pane);
            }
            cells.removeIf(cell -> cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
            cells.removeIf(cell -> cell.getCurrentSize() < 0);
            lastTimeOfMoveCell = time;
        }

        if (time - lastTimeOfDecrease >= 1_000_000_000 * GameSettings.Interval) {
            for (Cell cell : cells) {
                cell.resize();
            }
            for (Colony colony : colonies){
                colony.resize();
            }
            for (Bullet bullet : leftPlayer.getTank().getBullets()) {
                bullet.resize();
                if (bullet.getCurrentSize() <= 0) {
                    bullet.erase(pane);
                    leftPlayer.getTank().removeBullet(bullet);
                    break;
                }
            }
            for (Bullet bullet : rightPlayer.getTank().getBullets()) {
                bullet.resize();
                if (bullet.getCurrentSize() <= 0) {
                    bullet.erase(pane);
                    rightPlayer.getTank().removeBullet(bullet);
                    break;
                }
            }
            lastTimeOfDecrease = time;
            GameSettings.CellVelocity += GameSettings.CellVelocityIncrease;
            GameSettings.BulletVelocity += GameSettings.BulletVelocityIncrease;
        }
        var leftTank = leftPlayer.getTank();
        for (Bullet bullet : leftTank.getBullets()) {
            var cell = cellCollision(bullet);
            if (cell != null) {
                bullet.erase(pane);
                leftTank.removeBullet(bullet);
                if (cell.getCurrentHp() > 0) {
                    cell.getDamaged();
                    if(cell.getCurrentHp() == 0)
                        leftPlayer.increaseScore(cell.getInitialHp());
                }
                break;
            }
            if (bombCollision(bullet)) {
                bullet.erase(pane);
                leftTank.removeBullet(bullet);
                if (Bomb.fatalCollision(bullet)) {
                    return true;
                }
                break;
            }
        }
        var rightTank = rightPlayer.getTank();
        for (Bullet bullet : rightTank.getBullets()) {
            var cell = cellCollision(bullet);
            if (cell != null) {
                bullet.erase(pane);
                rightTank.removeBullet(bullet);
                if (cell.getCurrentHp() > 0) {
                    cell.getDamaged();
                    if(cell.getCurrentHp() == 0)
                        rightPlayer.increaseScore(cell.getInitialHp());
                }
                break;
            }
            if (bombCollision(bullet)) {
                bullet.erase(pane);
                rightTank.removeBullet(bullet);
                if (Bomb.fatalCollision(bullet)) {
                    return true;
                }
                break;
            }
        }
        if (time - lastTimeOfIncreaseHealth >= GameSettings.CellRegenerationInterval * 1_000_000_000) {

            for(Cell cell : cells){
                cell.regenerate();
            }
            lastTimeOfIncreaseHealth = time;
        }
        return false;
    }
    public Cell cellCollision (Bullet bullet) {
        for (Cell cell : cells) {
            double cellCenterX = cell.getX();
            double cellCenterY = cell.getY() + cell.getCurrentSize()/2;
            if (Math.abs(bullet.getX() - cellCenterX) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2 &&
                Math.abs(bullet.getY() - cellCenterY) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2) {
                return cell;
            }
        }
        for (Colony colony : colonies) {
            for (Cell cell : colony.getCells()) {
                double cellCenterX = cell.getX();
                double cellCenterY = cell.getY() + cell.getCurrentSize()/2;
                if (Math.abs(bullet.getX() - cellCenterX) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2 &&
                        Math.abs(bullet.getY() - cellCenterY) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2) {
                    return cell;
                }
            }
        }
        return null;
    }
    public boolean bombCollision (Bullet bullet) {
        boolean yCondition = bullet.getY() >= GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - Bomb.height;
        boolean xCondition = bullet.getX() >= GameSettings.WindowWidth/2 - Bomb.width/2 && bullet.getX() <= GameSettings.WindowWidth/2 + Bomb.width/2;
        return yCondition && xCondition;
    }
    private void removeCell (Cell cellToRemove) {
        if (cellToRemove == null) return;
        cells.remove(cellToRemove);
    }
    public void removeColony () {}

}