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
    private GameSoundPlayer gameSoundPlayer;

    public GameBoard () {
        leftPlayer = new PlayerInfo('L');
        rightPlayer = new PlayerInfo('R');
        cells = new ArrayList<>();
        colonies = new ArrayList<>();
        gameSoundPlayer = new GameSoundPlayer();
    }
    public GameBoard(List<Cell> cells, List<Colony> colonies){
        this.cells = cells;
        this.colonies = colonies;
    }

    public void actualizeTime(long currentTime){
        lastTimeOfGeneratingColony = currentTime;
        lastTimeOfGeneratingCell = currentTime;
        lastTimeOfIncreaseHealth = currentTime;
        lastTimeOfDecrease = currentTime;
        lastTimeOfMoveCell = currentTime;
    }

    public boolean updateGame(long time, Pane pane) {
        generateObjects(time);
        resizeObjects(time, pane);
        regenerateObjects(time);
        if(playerShots(pane, leftPlayer) || playerShots(pane, rightPlayer)){
            return true;
        }
        moveObjects(time, pane);
        eraseObjectsFromPane(pane);
        eraseObjects();
        leftPlayer.drawScore(pane, 'L');
        rightPlayer.drawScore(pane, 'R');
        return false;
    }

    private void regenerateObjects(long time) {
        boolean soundPlayed = false;
        if(lastTimeOfIncreaseHealth == 0)
            lastTimeOfIncreaseHealth = time;
        if (time - lastTimeOfIncreaseHealth >= GameSettings.CellRegenerationInterval * 1_000_000_000) {
            for(Cell cell : cells){
                if(cell.regenerate() && !soundPlayed){
                    gameSoundPlayer.playRegenerateCellSound();
                    soundPlayed = true;
                }
            }
            for(Colony colony : colonies){
                if(colony.regenerate() && !soundPlayed){
                    gameSoundPlayer.playRegenerateCellSound();
                    soundPlayed = true;
                }
            }
            lastTimeOfIncreaseHealth = time;
        }
    }

    public Cell cellCollision (Bullet bullet) {
        for (Cell cell : cells) {
            double cellCenterX = cell.getX();
            double cellCenterY = cell.getY() + cell.getCurrentSize()/2;
            if (Math.abs(bullet.getX() - cellCenterX) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2 && Math.abs(bullet.getY() - cellCenterY) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2) {
                return cell;
            }
        }
        return null;
    }

    public Cell colonyCollision (Bullet bullet) {
        for (Colony colony : colonies) {
            for (Cell cell : colony.getCells()) {
                double cellCenterX = cell.getX();
                double cellCenterY = cell.getY() + cell.getCurrentSize()/2;
                if (Math.abs(bullet.getX() - cellCenterX) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2 && Math.abs(bullet.getY() - cellCenterY) < (bullet.getCurrentSize() + cell.getCurrentSize()) / 2) {
                    return cell;
                }
            }
        }
        return null;
    }

    private boolean playerShots(Pane pane, PlayerInfo player) {
        var tank = player.getTank();
        for (Bullet bullet : tank.getBullets()) {
            var cell = cellCollision(bullet);
            if (cell != null) {
                bullet.erase(pane);
                tank.removeBullet(bullet);
                if (cell.getCurrentHp() > 0) {
                    cell.getDamaged();
                    gameSoundPlayer.playHitSound();
                    if(cell.getCurrentHp() == 0) {
                        player.increaseScore(cell.getInitialHp());
                        gameSoundPlayer.playGetPointSound();
                    }
                }
                break;
            }
            cell = colonyCollision(bullet);
            if (cell != null) {
                bullet.erase(pane);
                tank.removeBullet(bullet);
                if (cell.getCurrentHp() > 0) {
                    cell.getDamaged();
                    gameSoundPlayer.playHitSound();
                    if(!cell.getColony().isColonyAlive()) {
                        player.increaseScore(cell.getColony().getInitialCellHpSum());
                        colonies.remove(cell.getColony());
                        gameSoundPlayer.playGetPointSound();
                    }
                }
                break;
            }
            if (bombCollision(bullet)) {
                bullet.erase(pane);
                tank.removeBullet(bullet);
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

    public void eraseObjectsFromPane (Pane pane) {
        for (Cell cell : cells) {
            if (cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder || cell.getCurrentSize() <= 0 || cell.getCurrentHp() <= 0) {
                cell.eraseFromPane(pane);
            }
        }
        for (Colony colony : colonies) {
            for (Cell cell : colony.getCells()) {
                if (cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder || cell.getCurrentSize() <= 0 || cell.getCurrentHp() <= 0) {
                    cell.eraseFromPane(pane);
                }
            }
        }
    }

    public void eraseObjects() {
        cells.removeIf(cell -> cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
        cells.removeIf(cell -> cell.getCurrentSize() <= 0);
        cells.removeIf(cell -> cell.getCurrentHp() <= 0);

        for (Colony colony : colonies) {
            colony.getCells().removeIf(cell -> cell.getY() > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder);
            colony.getCells().removeIf(cell -> cell.getCurrentSize() <= 0);
            colony.getCells().removeIf(cell -> cell.getCurrentHp() <= 0);
        }

    }

    public void generateObjects(long time){
        if (time - lastTimeOfGeneratingColony >= GameSettings.TimeBetweenColonyGeneration * 1_000_000_000) {
            Colony colony = new Colony();
            colonies.add(colony);
            lastTimeOfGeneratingColony = time;
        }
        if (time - lastTimeOfGeneratingCell >= GameSettings.TimeBetweenCellGenerating * 1_000_000_000) {
            Cell cell = new Cell ();
            cells.add(cell);
            lastTimeOfGeneratingCell = time;
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
}