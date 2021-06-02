import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static Label setLabel(String text, double x, double y){
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    public static Label setLabel(String text, double x, double y, String path){
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.getStylesheets().add(path);
        return label;
    }

    public static Label setLabel(String text, double x, double y, String id, String path){
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

    public static PieChart setPieChart(GameBoard gameBoard){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Left Player: " + gameBoard.rightPlayer.getScore(), gameBoard.rightPlayer.getScore() == 0 ? 1 : gameBoard.rightPlayer.getScore()),
                new PieChart.Data("Right Player: " + gameBoard.leftPlayer.getScore(), gameBoard.leftPlayer.getScore() == 0 ? 1 : gameBoard.leftPlayer.getScore()));
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendVisible(false);
        chart.setStyle("-fx-font-size: 17px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        chart.setMaxSize(500, 500);
        chart.setMinSize(500, 500);
        chart.setTranslateY(50);
        chart.setTranslateX(50);
        return chart;
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
    public static void makeScreenshot(Pane pane){
        WritableImage image = pane.snapshot(new SnapshotParameters(), null);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date date = new Date();
        File file = new File("src/main/resources/screenshots/screenshot[" + formatter.format(date) + "]." + GameSettings.ImageExtension.toLowerCase(Locale.ROOT));
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), GameSettings.ImageExtension.toLowerCase(Locale.ROOT), file);
        } catch (IOException e) {
            System.out.println("Screenshot issue");
        }
    }

}
