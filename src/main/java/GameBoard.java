import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private final List<Cell> cells;
    private Colony colony; //we wstępnych pracach nad projektem jedna - w późniejszej fazie to pole zostanie przekształcone w listę koloni
    public final PlayerInfo leftPlayer, rightPlayer;
    private long lastTimeOfGeneratingCell;
    private long lastTimeOfMoveCell;
    private long lastTimeOfDecrease;

    public GameBoard () {
        leftPlayer = new PlayerInfo('L');
        rightPlayer = new PlayerInfo('R');
        cells = new ArrayList<>();
        //do inicjalizowania kolonii trzeba będzie zaimplementować jakiś algorytm wykrywający sąsiadujące komórki
        //colony = new Colony();
    }
    public void updateGame(long time, Pane pane) {
        if (time - lastTimeOfGeneratingCell >= GameSettings.TimeBetweenCellGenerating * 1_000_000_000) {
            Cell cell = new Cell (false);
            cells.add(cell);
            lastTimeOfGeneratingCell = time;
        }
        //Co 15 000 000 nanosekund aktualizowana jest pozycja komórek => częstoliwość około 60 Hz
        if (time - lastTimeOfMoveCell >= 15_000_000) {
            double timeBetween = ((double)time - (double)lastTimeOfMoveCell)/1_000_000_000;
            //System.out.println("Czas między zmianami: " + timeBetween);
            for (Cell cell : cells) {
                cell.move(timeBetween);
                cell.draw(pane);
            }
            cells.removeIf(cell -> cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
            cells.removeIf(cell -> cell.getCurrentSize() < 0);
            lastTimeOfMoveCell = time;
        }

        if (time - lastTimeOfDecrease >= 1_000_000_000 * GameSettings.Interval) {
            for (Cell cell : cells) {
                cell.resize();
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
        }
        var leftTank = leftPlayer.getTank();
        for (Bullet bullet : leftTank.getBullets()) {
            var cell = cellCollision(bullet);
            if (cell != null) {
                bullet.erase(pane);
                leftTank.removeBullet(bullet);
                if (cell.getCurrentHp() > 0) {
                    cell.getDamaged();
                }
                break;
            }
            if (bombCollision(bullet)) {
                //System.out.printf("Bomb collision (L)");
                bullet.erase(pane);
                leftTank.removeBullet(bullet);
                if (Bomb.fatalCollision(bullet)) {
                    System.exit(0);
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
                }
                break;
            }
            if (bombCollision(bullet)) {
                //System.out.println("Bomb collision (R)");
                bullet.erase(pane);
                rightTank.removeBullet(bullet);
                if (Bomb.fatalCollision(bullet)) {
                    System.exit(0);
                }
                break;
            }
        }
    }
    public Cell cellCollision (Bullet bullet) {
        for (Cell cell : cells) {
            if (Math.abs(bullet.getX() - cell.getX()) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2 &&
                Math.abs(bullet.getY() - cell.getY()) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2) {
                return cell;
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
    private void addPoints(@NotNull PlayerInfo player, int points) { player.increaseScore(points); }
}