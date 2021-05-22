import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public final class Cell extends GameSegment {
    private final boolean memberOfColony;
    private final int initialHp;
    private int currentHp;
    private final Label label;
    private final Rectangle segmentShape;

    public Cell (boolean memberOfColony) {
        super(ThreadLocalRandom.current().nextDouble(GameSettings.WidthOfTankBorder + GameSettings.CellSize/2, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder - GameSettings.CellSize/2), 0, GameSettings.CellSize, GameSettings.CellVelocity);
        segmentShape = new Rectangle();
        segmentShape.setWidth(GameSettings.CellSize);
        segmentShape.setHeight(GameSettings.CellSize);
        segmentShape.setX(x - GameSettings.CellSize/2);
        segmentShape.setY(y - GameSettings.CellSize/2);
        segmentShape.setFill(GameSettings.CellColorSequence[0]);
        this.memberOfColony = memberOfColony;
        initialHp = ThreadLocalRandom.current().nextInt(1,9);
        currentHp = initialHp;
        label = new Label(String.valueOf(initialHp));
        label.setMinSize(GameSettings.CellSize, GameSettings.CellSize);
        label.setMaxSize(GameSettings.CellSize, GameSettings.CellSize);
        label.setAlignment(Pos.CENTER);
        label.setTranslateX(x - GameSettings.CellSize/2);
        label.setStyle("-fx-font-weight: bold; -fx-text-alignment: center; -fx-font-size:" + 0.75 * GameSettings.CellSize +"px;");
    }
    public void draw (Pane pane){
        segmentShape.setY(y);
        pane.getChildren().remove(segmentShape);
        pane.getChildren().remove(label);
        if(y < GameSettings.WindowHeight - GameSettings.WidthOfTankBorder && currentHp!=0) {
            pane.getChildren().add(segmentShape);
            label.setTranslateY(y);
            pane.getChildren().add(label);
        }
        label.setTranslateY(y);
        pane.getChildren().remove(label);
        if(y < GameSettings.WINDOW_HEIGHT - GameSettings.WidthOfTankBorder )
        pane.getChildren().add(label);
    }
    public void move (double time){
        y += GameSettings.CellVelocity * time;
    }
    public int getInitialHp () { return initialHp; }
    public int getCurrentHp () {
        return currentHp;
    }
    public void regenerate () {
        label.setText(String.valueOf(currentHp));
        if(currentHp < initialHp && currentHp > 0)
            currentHp++;
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
        label.setText(String.valueOf(currentHp));
    }
    public void resize(){
        this.currentSize -= GameSettings.CellSizeDecrease;
        this.segmentShape.setWidth(currentSize);
        this.segmentShape.setHeight(currentSize);
        this.segmentShape.setX(segmentShape.getX()+GameSettings.CellSizeDecrease/2);
        this.segmentShape.setY(segmentShape.getY()+GameSettings.CellSizeDecrease/2);
        this.label.setMinSize(currentSize, currentSize);
        this.label.setMaxSize(currentSize, currentSize);
        this.label.setAlignment(Pos.CENTER);
        this.label.setTranslateX(x - currentSize/2);
        this.label.setStyle("-fx-text-alignment: center; -fx-font-weight: bold; -fx-font-size:" + 0.75 * currentSize + "px;");
    }
}