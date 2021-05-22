import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class ControllerSetter {
    public static void addChildren(Pane pane, Node... elements){
        for(Node node: elements){
            pane.getChildren().add(node);
        }
    }

    public static Slider setSlider(double min, double max, double x, double y, double prefWidth, double primaryValue){
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setTranslateX(x);
        slider.setTranslateY(y);
        slider.setPrefWidth(prefWidth);
        slider.setValue(primaryValue);
        return slider;
    }

    public static ScrollPane setScrollPane(double x, double y, double width){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setTranslateX(x);
        scrollPane.setTranslateY(y);
        scrollPane.setPrefWidth(width);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent");
        return scrollPane;
    }

    public static GridPane setGridPane(double x, double y, double width){
        GridPane gridPane = new GridPane();
        gridPane.setTranslateX(x);
        gridPane.setTranslateY(y);
        gridPane.setPrefWidth(width);
        gridPane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
        gridPane.setVgap(40);
        return gridPane;
    }

    public static Pane setPane(double x, double y, double width, String id, String path){
        Pane pane = new Pane();
        pane.setTranslateX(x);
        pane.setTranslateY(y);
        pane.setPrefWidth(width);
        pane.setId(id);
        pane.getStylesheets().add(path);
        return pane;
    }

    public static Button setGroupOfButtons(int width, int height, int x, int y, String text){
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setId("button");
        button.getStylesheets().add("css/keyButtons.css");
        button.setStyle("-fx-font-size:" + 30.0/Math.sqrt(text.length()) +"px;");
        return button;
    }

    public static Button setButton(int x, int y, String text, String path){
        Button button = new Button(text);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.getStylesheets().add(path);
        return button;
    }

    public static Label setLabel(String text, int x, int y){
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    public static Label setLabel(String text, int x, int y, String path){
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.getStylesheets().add(path);
        return label;
    }

    public static Label setLabel(String text, int x, int y, String id, String path){
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setId(id);
        label.getStylesheets().add(path);
        return label;
    }

    public static TextField setTextField(int width, int x, int y, String text, String id){
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.setTranslateX(x);
        textField.setTranslateY(y);
        textField.setText(text);
        textField.setId(id);
        textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
        return textField;
    }

    public static Line setLine(double startX, double startY, double endX, double endY){
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        return line;
    }

    public static void setPressedKey(KeyCode keyCode){
        if (keyCode.equals(KeyCode.W)) {
            Controller.leftMoveUpPressed = true;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerMoveDown)) {
            Controller.leftMoveDownPressed = true;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerBarrelDown)) {
            Controller.leftBarrelDownPressed = true;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerBarrelUp)) {
            Controller.leftBarrelUpPressed = true;
        }
        else if (keyCode.equals(GameSettings.RightPlayerMoveUp)) {
            Controller.rightMoveUpPressed = true;
        }
        else if (keyCode.equals(GameSettings.RightPlayerMoveDown)) {
            Controller.rightMoveDownPressed = true;
        }
        else if (keyCode.equals(GameSettings.RightPlayerBarrelUp)) {
            Controller.rightBarrelUpPressed = true;
        }
        else if (keyCode.equals(GameSettings.RightPlayerBarrelDown)) {
            Controller.rightBarrelDownPressed = true;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerFire)) {
            Controller.leftPlayerShootPressed = true;
        }
        else if (keyCode.equals(GameSettings.RightPlayerFire)) {
            Controller.rightPlayerShootPressed = true;
        }
    }

    public static void setReleasedKey(KeyCode keyCode){
        if (keyCode.equals(KeyCode.W)) {
            Controller.leftMoveUpPressed = false;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerMoveDown)) {
            Controller.leftMoveDownPressed = false;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerBarrelDown)) {
            Controller.leftBarrelDownPressed = false;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerBarrelUp)) {
            Controller.leftBarrelUpPressed = false;
        }
        else if (keyCode.equals(GameSettings.RightPlayerMoveUp)) {
            Controller.rightMoveUpPressed = false;
        }
        else if (keyCode.equals(GameSettings.RightPlayerMoveDown)) {
            Controller.rightMoveDownPressed = false;
        }
        else if (keyCode.equals(GameSettings.RightPlayerBarrelUp)) {
            Controller.rightBarrelUpPressed = false;
        }
        else if (keyCode.equals(GameSettings.RightPlayerBarrelDown)) {
            Controller.rightBarrelDownPressed = false;
        }
        else if (keyCode.equals(GameSettings.LeftPlayerFire)) {
            Controller.leftPlayerShootPressed = false;
        }
        else if (keyCode.equals(GameSettings.RightPlayerFire)) {
            Controller.rightPlayerShootPressed = false;
        }
    }
}
