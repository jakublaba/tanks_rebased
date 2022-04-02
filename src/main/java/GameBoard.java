import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private final List<Cell> cells;
    private final List <Colony> colonies;
    public PlayerInfo leftPlayer, rightPlayer;
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
    public GameBoard(List<Cell> cells, List<Colony> colonies){
        this.cells = cells;
        this.colonies = colonies;
    }

    public boolean updateGame(long time, Pane pane) {
        generateObjects(time);
        moveObjects(time, pane);
        eraseObjects();
        resizeObjects(time, pane);
        regenerateObjects(time);
        if(leftPlayerShots(pane) || rightPlayerShots(pane)){
            return true;
        }
        leftPlayer.drawScore(pane, 'L');
        rightPlayer.drawScore(pane, 'R');
        return false;
    }

    private void regenerateObjects(long time) {
        if (time - lastTimeOfIncreaseHealth >= GameSettings.CellRegenerationInterval * 1_000_000_000) {
            for(Cell cell : cells){
                cell.regenerate();
            }
            lastTimeOfIncreaseHealth = time;
        }
    }

    private boolean rightPlayerShots(Pane pane) {
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
        return false;
    }

    private boolean leftPlayerShots(Pane pane) {
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
        return false;
    }

    private void resizeObjects(long time, Pane pane) {
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
    }

    private void moveObjects(long time, Pane pane) {
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
            lastTimeOfMoveCell = time;
        }
    }
    public void eraseObjects(){
        cells.removeIf(cell -> cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        cells.removeIf(cell -> cell.getCurrentSize() <= 0);
        cells.removeIf(cell -> cell.getCurrentHp() <= 0);
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
    public void generateObjects(long time){
        if (time - lastTimeOfGeneratingCell >= GameSettings.TimeBetweenCellGenerating * 1_000_000_000) {
            Cell cell = new Cell ();
            cells.add(cell);
            lastTimeOfGeneratingCell = time;
        }
        if (time - lastTimeOfGeneratingColony >= GameSettings.TimeBetweenColonyGeneration * 1_000_000_000) {
            Colony colony = new Colony();
            colonies.add(colony);
            lastTimeOfGeneratingColony = time;
        }
    }
    public boolean bombCollision (Bullet bullet) {
        boolean yCondition = bullet.getY() >= GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - Bomb.height;
        boolean xCondition = bullet.getX() >= GameSettings.WindowWidth/2 - Bomb.width/2 && bullet.getX() <= GameSettings.WindowWidth/2 + Bomb.width/2;
        return yCondition && xCondition;
    }
    public void removeAllCells(){
        cells.clear();
        colonies.clear();
    }

    public void updateTankPosition(Pane layerPane) {
        //Left Player
        leftPlayer.getTank().draw(layerPane);
        if (Controller.leftMoveUpPressed && !Controller.leftMoveDownPressed) {
            leftPlayer.getTank().move(GameSettings.LeftPlayerMoveUp);
        }
        if (Controller.leftMoveDownPressed && !Controller.leftMoveUpPressed) {
            leftPlayer.getTank().move(GameSettings.LeftPlayerMoveDown);
        }
        if (Controller.leftBarrelDownPressed && !Controller.leftBarrelUpPressed) {
            leftPlayer.getTank().rotateBarrel(GameSettings.LeftPlayerBarrelDown);
        }
        if (Controller.leftBarrelUpPressed && !Controller.leftBarrelDownPressed) {
            leftPlayer.getTank().rotateBarrel(GameSettings.LeftPlayerBarrelUp);
        }
        if (Controller.leftPlayerShootPressed && Controller.leftPlayerAllowedToShoot) {
            leftPlayer.getTank().shoot();
            Controller.leftPlayerAllowedToShoot = false;
        }
        //Right Player
        rightPlayer.getTank().draw(layerPane);
        if (Controller.rightMoveUpPressed && !Controller.rightMoveDownPressed) {
            rightPlayer.getTank().move(GameSettings.RightPlayerMoveUp);
        }
        if (Controller.rightMoveDownPressed && !Controller.rightMoveUpPressed) {
            rightPlayer.getTank().move(GameSettings.RightPlayerMoveDown);
        }
        if (Controller.rightBarrelUpPressed && !Controller.rightBarrelDownPressed) {
            rightPlayer.getTank().rotateBarrel(GameSettings.RightPlayerBarrelUp);
        }
        if (Controller.rightBarrelDownPressed && !Controller.rightBarrelUpPressed) {
            rightPlayer.getTank().rotateBarrel(GameSettings.RightPlayerBarrelDown);
        }
        if (Controller.rightPlayerShootPressed && Controller.rightPlayerAllowedToShoot) {
            rightPlayer.getTank().shoot();
            Controller.rightPlayerAllowedToShoot = false;
        }
    }
    public List<Cell> getCellsList(){
        return cells;
    }
}