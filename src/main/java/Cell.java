import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.ThreadLocalRandom;

public class Cell extends GameSegment {
    private final boolean memberOfColony;
    private final int initialHp;
    private int currentHp;
    private final Label label;

    public Cell (boolean memberOfColony) {
        super(ThreadLocalRandom.current().nextDouble(GameSettings.WidthOfTankBorder + GameSettings.CellSize/2, GameSettings.WINDOW_WIDTH - GameSettings.WidthOfTankBorder - GameSettings.CellSize/2), 0, GameSettings.CellSize, GameSettings.CellVelocity);
        segmentShape.setFill(GameSettings.CELL_COLOR_SEQUENCE[0]);
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
        super.draw(pane);
        label.setTranslateY(y);
        pane.getChildren().remove(label);
        if(y < GameSettings.WINDOW_HEIGHT - GameSettings.WidthOfTankBorder )
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
        for(int i = 0; i < GameSettings.CELL_COLOR_SEQUENCE.length; i++){
            if((double)currentHp/(double)initialHp >= tmp && (double)currentHp/(double)initialHp < tmp + 1.0/GameSettings.CELL_COLOR_SEQUENCE.length)
                segmentShape.setFill(GameSettings.CELL_COLOR_SEQUENCE[GameSettings.CELL_COLOR_SEQUENCE.length -i -1]);
            tmp+= 1.0/GameSettings.CELL_COLOR_SEQUENCE.length;
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
