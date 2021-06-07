import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public final class Cell extends GameSegment {
    private final int initialHp;
    private int currentHp;
    private Label currentHpLabel;
    private Rectangle segmentShape;
    private final int position;
    private final Colony colony;

    public Cell () {
        super(ThreadLocalRandom.current().nextDouble(GameSettings.WidthOfTankBorder + GameSettings.CellSize/2, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder - GameSettings.CellSize/2), 0, GameSettings.CellSize, GameSettings.CellVelocity);
        setShape();
        initialHp = ThreadLocalRandom.current().nextInt(1,9);
        currentHp = initialHp;
        position = 0;
        colony = null;
        setHpLabel();
    }

    public Cell (double x, double y, double initialSize, double initialVelocity, int position, int initialHp, Colony colony){
        super(x, y, initialSize, initialVelocity);
        setShape();
        this.initialHp = initialHp;
        currentHp = initialHp;
        this.position = position;
        this.colony = colony;
        setHpLabel();
    }

    public Cell (double x, double y, double cellSize, int Hp){
        super(x, y, cellSize, GameSettings.CellVelocity);
        setShape();
        this.initialHp = GameSettings.CellHealth;
        currentHp = Hp;
        this.position = 0;
        colony = null;
    }

    private void setShape(){
        segmentShape = new Rectangle();
        segmentShape.setWidth(GameSettings.CellSize);
        segmentShape.setHeight(GameSettings.CellSize);
        segmentShape.setX(x - GameSettings.CellSize/2);
        segmentShape.setY(y - GameSettings.CellSize/2);
        segmentShape.setFill(GameSettings.CellColorSequence[0]);
    }

    private void setHpLabel(){
        currentHpLabel = new Label(String.valueOf(initialHp));
        currentHpLabel.setMinSize(GameSettings.CellSize, GameSettings.CellSize);
        currentHpLabel.setMaxSize(GameSettings.CellSize, GameSettings.CellSize);
        currentHpLabel.setAlignment(Pos.CENTER);
        currentHpLabel.setTranslateX(x - GameSettings.CellSize/2);
        currentHpLabel.setStyle("-fx-font-weight: bold; -fx-text-alignment: center; -fx-font-size:" + 0.75 * GameSettings.CellSize +"px;");
    }

    public void draw (Pane pane) {
        segmentShape.setY(y);
        pane.getChildren().remove(segmentShape);
        pane.getChildren().remove(currentHpLabel);
        if(y > GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - currentSize){
            segmentShape.setHeight(GameSettings.WindowWidth - GameSettings.WidthOfTankBorder - y);
        }
        if(y < GameSettings.WindowHeight - GameSettings.WidthOfTankBorder && currentHp > 0) {
            pane.getChildren().add(segmentShape);
            currentHpLabel.setTranslateY(y);
            if(y < GameSettings.WindowHeight - GameSettings.WidthOfTankBorder - 0.75 * currentSize){
                pane.getChildren().add(currentHpLabel);
            }
        }

    }

    public void move (double time){
        y += GameSettings.CellVelocity * time;
    }

    public int getInitialHp () { return initialHp; }

    public int getCurrentHp () {
        return currentHp;
    }

    public Rectangle getSegmentShape() { return segmentShape; }

    public int getPosition() {return position;}

    public Label getLabel() { return currentHpLabel; }

    public double getCurrentSize() { return currentSize; }

    public Colony getColony () { return colony; }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void eraseFromPane (Pane pane) {
        pane.getChildren().remove(segmentShape);
        pane.getChildren().remove(currentHpLabel);
    }

    public boolean regenerate () {
        if(currentHp < initialHp && currentHp > 0) {
            double tmp = 0;
            currentHp++;
            for(int i = 0; i < GameSettings.CellColorSequence.length; i++){
                if((double)currentHp/(double)initialHp >= tmp && (double)currentHp/(double)initialHp < tmp + 1.0/GameSettings.CellColorSequence.length) {
                    segmentShape.setFill(GameSettings.CellColorSequence[GameSettings.CellColorSequence.length - i - 1]);
                }
                tmp += 1.0/GameSettings.CellColorSequence.length;
            }
            currentHpLabel.setText(String.valueOf(currentHp));
            return true;
        }
        return false;
    }

    public void getDamaged () {
        double tmp = 0;
        currentHp = currentHp < 1 ? currentHp : currentHp - 1;
        for(int i = 0; i < GameSettings.CellColorSequence.length; i++){
            if((double)currentHp/(double)initialHp >= tmp && (double)currentHp/(double)initialHp < tmp + 1.0/GameSettings.CellColorSequence.length) {
                segmentShape.setFill(GameSettings.CellColorSequence[GameSettings.CellColorSequence.length - i - 1]);
            }
            tmp += 1.0/GameSettings.CellColorSequence.length;
        }
        currentHpLabel.setText(String.valueOf(currentHp));
    }
    public void resize(){
        this.currentSize -= GameSettings.CellSizeDecrease;
        this.segmentShape.setWidth(currentSize);
        this.segmentShape.setHeight(currentSize);
        this.segmentShape.setX(segmentShape.getX()+GameSettings.CellSizeDecrease/2);
        this.segmentShape.setY(segmentShape.getY()-GameSettings.CellSizeDecrease/2);
        this.currentHpLabel.setMinSize(currentSize, currentSize);
        this.currentHpLabel.setMaxSize(currentSize, currentSize);
        this.currentHpLabel.setAlignment(Pos.CENTER);
        this.currentHpLabel.setTranslateX(x - currentSize/2);
        this.currentHpLabel.setStyle("-fx-text-alignment: center; -fx-font-weight: bold; -fx-font-size:" + 0.75 * currentSize + "px;");
    }
}