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
    private Rectangle segmentShape;

    public Cell (boolean memberOfColony) {
        super(ThreadLocalRandom.current().nextDouble(GameSettings.WidthOfTankBorder + GameSettings.CellSize/2, GameSettings.WindowWidth - GameSettings.WidthOfTankBorder - GameSettings.CellSize/2), 0, GameSettings.CellSize, GameSettings.CellVelocity);
        segmentShape = new Rectangle();
        segmentShape.setWidth(GameSettings.CellSize);
        segmentShape.setHeight(GameSettings.CellSize);
        segmentShape.setX(x - GameSettings.CellSize/2);
        segmentShape.setY(y - GameSettings.CellSize/2);
        segmentShape.setFill(GameSettings.CellColorSequence[0]);
        this.memberOfColony = memberOfColony;
        this.initialHp = GameSettings.CellHealth;
        this.currentHp = GameSettings.CellHealth;
        this.label = new Label(String.valueOf(initialHp));
        label.setMinSize(GameSettings.CellSize, GameSettings.CellSize);
        label.setMaxSize(GameSettings.CellSize, GameSettings.CellSize);
        label.setAlignment(Pos.CENTER);
        label.setTranslateX(x - GameSettings.CellSize/2);
        label.setStyle("-fx-font-weight: bold; -fx-text-alignment: center; -fx-font-size:" + 0.75 * GameSettings.CellSize +"px;");
    }
    public void draw (Pane pane){
        segmentShape.setY(y);
        pane.getChildren().remove(segmentShape);
        if(y < GameSettings.WindowHeight) {
            pane.getChildren().add(segmentShape);
        }
        label.setTranslateY(y);
        pane.getChildren().remove(label);
        pane.getChildren().add(label);
    }
    public void move(double time){
        y += GameSettings.CellVelocity * time;
    }

    public int getCurrentHp () { return currentHp; }

    private void regenerate () {
        currentHp = currentHp >= initialHp ? initialHp : currentHp + 1;
    }

    public void getDamaged () {
        double tmp = 0;
        for(int i = 0; i < GameSettings.CellColorSequence.length; i++){
            if((double)currentHp/(double)initialHp >= tmp && (double)currentHp/(double)initialHp < tmp + 1.0/GameSettings.CellColorSequence.length)
                segmentShape.setFill(GameSettings.CellColorSequence[GameSettings.CellColorSequence.length -i -1]);
            tmp+= 1.0/GameSettings.CellColorSequence.length;
        }
        currentHp = currentHp <= 0 ? 0 : currentHp - 1;
        label.setText(String.valueOf(currentHp));
    }
    public void resize(){
        currentSize -= GameSettings.CellSizeDecrease;
        segmentShape.setWidth(currentSize);
        segmentShape.setHeight(currentSize);
        segmentShape.setX(segmentShape.getX()+GameSettings.CellSizeDecrease/2);
        segmentShape.setY(segmentShape.getY()+GameSettings.CellSizeDecrease/2);
        label.setMinSize(currentSize, currentSize);
        label.setMaxSize(currentSize, currentSize);
        label.setAlignment(Pos.CENTER);
        label.setTranslateX(x - currentSize/2);
        label.setStyle("-fx-text-alignment: center; -fx-font-weight: bold; -fx-font-size:" + 0.75 * currentSize + "px;");
    }
}
