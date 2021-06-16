import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphicElementsSetter {
    public static void addChildren(Pane pane, Node... elements) {
        for (Node node : elements) {
            pane.getChildren().add(node);
        }
    }

    public static Slider setSlider(double min, double max, double x, double y, double prefWidth, double primaryValue) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setTranslateX(x);
        slider.setTranslateY(y);
        slider.setPrefWidth(prefWidth);
        slider.setValue(primaryValue);
        return slider;
    }

    public static ScrollPane setScrollPane(double x, double y, double width) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setTranslateX(x);
        scrollPane.setTranslateY(y);
        scrollPane.setPrefWidth(width);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent");
        return scrollPane;
    }

    public static GridPane setGridPane(double x, double y, double width) {
        GridPane gridPane = new GridPane();
        gridPane.setTranslateX(x);
        gridPane.setTranslateY(y);
        gridPane.setPrefWidth(width);
        gridPane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2)");
        gridPane.setVgap(40);
        return gridPane;
    }

    public static Pane setPane(double x, double y, double width, String id, String path) {
        Pane pane = new Pane();
        pane.setTranslateX(x);
        pane.setTranslateY(y);
        pane.setPrefWidth(width);
        pane.setId(id);
        pane.getStylesheets().add(path);
        return pane;
    }

    public static Button setButtonOfGroup(int width, int height, int x, int y, String text) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setId("button");
        button.getStylesheets().add("css/keyButtons.css");
        button.setStyle("-fx-font-size:" + 30.0 / Math.sqrt(text.length()) + "px;");
        return button;
    }

    public static Button setButton(int x, int y, String text, String path) {
        Button button = new Button(text);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.getStylesheets().add(path);
        return button;
    }

    public static Label setLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    public static Label setLabel(String text, double x, double y, String path) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.getStylesheets().add(path);
        return label;
    }

    public static Label setLabel(String text, double x, double y, String id, String path) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setId(id);
        label.getStylesheets().add(path);
        return label;
    }

    public static TextField setTextField(int width, int x, int y, String text, String id) {
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.setTranslateX(x);
        textField.setTranslateY(y);
        textField.setText(text);
        textField.setId(id);
        textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
        return textField;
    }

    public static Line setLine(double startX, double startY, double endX, double endY) {
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        return line;
    }

    public static PieChart setPieChart(GameBoard gameBoard) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Right: " + gameBoard.rightPlayer.getScore(), gameBoard.rightPlayer.getScore() == 0 ? 1 : gameBoard.rightPlayer.getScore()),
                new PieChart.Data("Left: " + gameBoard.leftPlayer.getScore(), gameBoard.leftPlayer.getScore() == 0 ? 1 : gameBoard.leftPlayer.getScore()));
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendVisible(false);
        chart.setStyle("-fx-font-size: 17px; -fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
        chart.setMaxSize(500, 500);
        chart.setMinSize(500, 500);
        chart.setTranslateY(50);
        chart.setTranslateX(50);
        return chart;
    }

    public static CheckBox setCheckBox(double x, double y, String path) {
        CheckBox checkBox = new CheckBox();
        checkBox.setLayoutX(x);
        checkBox.setLayoutY(y);
        checkBox.getStylesheets().add(path);
        if (GameSettings.MakeScreenshot) {
            checkBox.fire();
        }
        return checkBox;
    }

    public static void setPressedKey(KeyCode keyCode) {
        if (keyCode.equals(KeyCode.W)) {
            Controller.leftMoveUpPressed = true;
        } else if (keyCode.equals(GameSettings.LeftPlayerMoveDown)) {
            Controller.leftMoveDownPressed = true;
        } else if (keyCode.equals(GameSettings.LeftPlayerBarrelDown)) {
            Controller.leftBarrelDownPressed = true;
        } else if (keyCode.equals(GameSettings.LeftPlayerBarrelUp)) {
            Controller.leftBarrelUpPressed = true;
        } else if (keyCode.equals(GameSettings.RightPlayerMoveUp)) {
            Controller.rightMoveUpPressed = true;
        } else if (keyCode.equals(GameSettings.RightPlayerMoveDown)) {
            Controller.rightMoveDownPressed = true;
        } else if (keyCode.equals(GameSettings.RightPlayerBarrelUp)) {
            Controller.rightBarrelUpPressed = true;
        } else if (keyCode.equals(GameSettings.RightPlayerBarrelDown)) {
            Controller.rightBarrelDownPressed = true;
        } else if (keyCode.equals(GameSettings.LeftPlayerFire)) {
            Controller.leftPlayerShootPressed = true;
        } else if (keyCode.equals(GameSettings.RightPlayerFire)) {
            Controller.rightPlayerShootPressed = true;
        } else if (!keyCode.equals(GameSettings.Pause)) {
            PlayerInfo.addInformation("[INFO]" + keyCode + "<-This key is not assigned!");
        }
    }

    public static void setReleasedKey(KeyCode keyCode) {
        if (keyCode.equals(KeyCode.W)) {
            Controller.leftMoveUpPressed = false;
        } else if (keyCode.equals(GameSettings.LeftPlayerMoveDown)) {
            Controller.leftMoveDownPressed = false;
        } else if (keyCode.equals(GameSettings.LeftPlayerBarrelDown)) {
            Controller.leftBarrelDownPressed = false;
        } else if (keyCode.equals(GameSettings.LeftPlayerBarrelUp)) {
            Controller.leftBarrelUpPressed = false;
        } else if (keyCode.equals(GameSettings.RightPlayerMoveUp)) {
            Controller.rightMoveUpPressed = false;
        } else if (keyCode.equals(GameSettings.RightPlayerMoveDown)) {
            Controller.rightMoveDownPressed = false;
        } else if (keyCode.equals(GameSettings.RightPlayerBarrelUp)) {
            Controller.rightBarrelUpPressed = false;
        } else if (keyCode.equals(GameSettings.RightPlayerBarrelDown)) {
            Controller.rightBarrelDownPressed = false;
        } else if (keyCode.equals(GameSettings.LeftPlayerFire)) {
            Controller.leftPlayerShootPressed = false;
        } else if (keyCode.equals(GameSettings.RightPlayerFire)) {
            Controller.rightPlayerShootPressed = false;
        }
    }

    public static void makeScreenshot(Pane pane) {
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

    public static void setControlsTabLabels(Pane pane) {
        Label topRightLabel = GraphicElementsSetter.setLabel("Left Player:", 100, 30, "css/tabLabel.css");
        Label topLeftLabel = GraphicElementsSetter.setLabel("Right Player:", 500, 30, "css/tabLabel.css");
        Label moveLabel = GraphicElementsSetter.setLabel("Move:", 50, 100, "css/tabLabel.css");
        Label shootLabel = GraphicElementsSetter.setLabel("Shoot:", 50, 370, "css/tabLabel.css");
        addChildren(pane, topRightLabel, topLeftLabel, moveLabel, shootLabel);
    }

    public static void setControlsTabButtons(Pane pane) {
        List<Button> controlButtons = new ArrayList<>();
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 100, 200, GameSettings.LeftPlayerBarrelDown.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 170, 200, GameSettings.LeftPlayerMoveDown.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 240, 200, GameSettings.LeftPlayerBarrelUp.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 170, 130, GameSettings.LeftPlayerMoveUp.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 500, 200, GameSettings.RightPlayerBarrelDown.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 570, 200, GameSettings.RightPlayerMoveDown.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 640, 200, GameSettings.RightPlayerBarrelUp.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 570, 130, GameSettings.RightPlayerMoveUp.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 170, 400, GameSettings.LeftPlayerFire.toString()));
        controlButtons.add(GraphicElementsSetter.setButtonOfGroup(60, 60, 570, 400, GameSettings.RightPlayerFire.toString()));
        for (Button x : controlButtons) {
            x.setOnMouseClicked(keyEvent -> {
                for (Button y : controlButtons) {
                    y.setId("button");
                }
                x.setId("chosenButton");
            });
            x.setOnKeyPressed(keyEvent -> {
                boolean keyIsUsed = false;
                for (Button y : controlButtons) {
                    if (y.getText().equals(keyEvent.getCode().toString())) {
                        keyIsUsed = true;
                        if (y != x) {
                            y.setId("usedButton");
                        }
                    }
                }
                if (!keyIsUsed) {
                    changeKey(x, keyEvent);
                }
            });
            pane.getChildren().add(x);
        }
    }

    private static void changeKey(Button button, KeyEvent keyEvent) {
        if (GameSettings.LeftPlayerBarrelDown.toString().equals(button.getText()))
            GameSettings.LeftPlayerBarrelDown = keyEvent.getCode();
        else if (GameSettings.LeftPlayerBarrelUp.toString().equals(button.getText()))
            GameSettings.LeftPlayerBarrelUp = keyEvent.getCode();
        else if (GameSettings.LeftPlayerMoveDown.toString().equals(button.getText()))
            GameSettings.LeftPlayerMoveDown = keyEvent.getCode();
        else if (GameSettings.LeftPlayerMoveUp.toString().equals(button.getText()))
            GameSettings.LeftPlayerMoveUp = keyEvent.getCode();
        else if (GameSettings.RightPlayerBarrelDown.toString().equals(button.getText()))
            GameSettings.RightPlayerBarrelDown = keyEvent.getCode();
        else if (GameSettings.RightPlayerBarrelUp.toString().equals(button.getText()))
            GameSettings.RightPlayerBarrelUp = keyEvent.getCode();
        else if (GameSettings.RightPlayerMoveDown.toString().equals(button.getText()))
            GameSettings.RightPlayerMoveDown = keyEvent.getCode();
        else if (GameSettings.RightPlayerMoveUp.toString().equals(button.getText()))
            GameSettings.RightPlayerMoveUp = keyEvent.getCode();
        else if (GameSettings.RightPlayerFire.toString().equals(button.getText()))
            GameSettings.RightPlayerFire = keyEvent.getCode();
        else if (GameSettings.LeftPlayerFire.toString().equals(button.getText()))
            GameSettings.LeftPlayerFire = keyEvent.getCode();
        else {
            PlayerInfo.addInformation("[ERROR]Unhandled Case!");
        }
        button.setText(keyEvent.getCode().toString());
        button.setStyle("-fx-font-size:" + 30.0 / Math.sqrt(keyEvent.getCode().toString().length()) + "px;");
    }

    public static List<TextField> setLineOfElements(GridPane gridConfigurationPane) {
        List<TextField> listOfTextFields = new ArrayList<>();
        for (int i = 0; i < GameSettings.getConfigurationList().size(); i++) {
            Label tmpLabel = GraphicElementsSetter.setLabel(GameSettings.getConfigurationList().get(i)[0], 20, 0);
            tmpLabel.setStyle("-fx-font-size: 20px;-fx-font-family:\"Courier New\", Helvetica, Courier New, sans-serif;");
            Button tmpBtn = GraphicElementsSetter.setButton(200, 0, "Save " + GameSettings.getConfigurationList().get(i)[1], "css/tabPaneButtons.css");
            TextField tmpTextField = GraphicElementsSetter.setTextField(100, 50, 0, GameSettings.getConfigurationList().get(i)[2], GameSettings.getConfigurationList().get(i)[1]);
            gridConfigurationPane.add(tmpLabel, 0, i + 1);
            gridConfigurationPane.add(tmpTextField, 1, i + 1);
            gridConfigurationPane.add(tmpBtn, 2, i + 1);
            listOfTextFields.add(tmpTextField);
            tmpBtn.setOnAction(event -> {
                for (TextField tx : listOfTextFields) {
                    if (tx.getId().equals(tmpBtn.getText().substring(5))) {
                        try {
                            if (!GameSettings.setGameSettings(tmpBtn.getText().substring(5), tx.getText()))
                                PlayerInfo.addInformation("[ERROR]This value is unhandled: " + tx.getText());
                        } catch (NumberFormatException e) {
                            tx.clear();
                        }
                    }
                }
            });
        }
        return listOfTextFields;
    }

    public static void setManualConfigurationTabButtons(GridPane gridPane, TextField configNameTextField, Label configNameLabel, Label successLabel, List<TextField> listOfTextFields) {
        Button saveConfigurationBtn = new Button("Save Configuration File");
        final int position = GameSettings.getConfigurationList().size() + 2;
        saveConfigurationBtn.setOnMouseClicked(mouseEvent -> {
            if (saveConfigurationBtn.getText().equals("Accept Name")) {
                configNameTextField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
                if (configNameTextField.getText().length() == 0) {
                    configNameTextField.setStyle("-fx-background-color: rgba(200, 2, 2, 0.3)");
                } else {
                    for (TextField textField : GameSettings.saveConfigFile(listOfTextFields, configNameTextField.getText())) {
                        textField.setStyle("-fx-background-color: rgba(200, 2, 2, 0.3)");
                    }
                    gridPane.getChildren().remove(saveConfigurationBtn);
                    saveConfigurationBtn.setText("Save Configuration File");
                    gridPane.add(saveConfigurationBtn, 0, position);
                    configNameLabel.setVisible(false);
                    configNameTextField.setVisible(false);
                    gridPane.add(successLabel, 0, position + 1);
                }
            } else if (saveConfigurationBtn.getText().equals("Save Configuration File")) {
                for (TextField textField : listOfTextFields) {
                    textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
                }
                gridPane.getChildren().remove(saveConfigurationBtn);
                saveConfigurationBtn.setText("Accept Name");
                saveConfigurationBtn.setPrefWidth(saveConfigurationBtn.getWidth());
                gridPane.getChildren().remove(successLabel);
                configNameLabel.setVisible(true);
                configNameTextField.setVisible(true);
                gridPane.add(saveConfigurationBtn, 2, position + 1);
            }
        });
        gridPane.add(saveConfigurationBtn, 0, GameSettings.getConfigurationList().size() + 1);
    }

    public static void setGamePropertiesTabLabels(Pane pane) {
        Label musicLabel = GraphicElementsSetter.setLabel("Set Volume Of Music:", 50, 50, "css/tabLabel.css");
        Label soundLabel = GraphicElementsSetter.setLabel("Set Volume Of Sounds:", 50, 150, "css/tabLabel.css");
        Label configLabel = GraphicElementsSetter.setLabel("Load Your Configuration File:", 50, 250, "css/tabLabel.css");
        Label screenshotLabel = GraphicElementsSetter.setLabel("Make Screenshot After Game:", 50, 350, "css/tabLabel.css");
        addChildren(pane, musicLabel, soundLabel, configLabel, screenshotLabel);
    }

    public static void setGamePropertiesTabControlElements(Pane mainPane, TabPane tabPane) {
        Label currentConfigLabel = GraphicElementsSetter.setLabel("Current Configuration: " + GameSettings.ConfigFileName, 50, 280, "minLabel", "css/tabLabel.css");
        Label currentScreenshotExt = GraphicElementsSetter.setLabel("Current Extension File: " + GameSettings.ImageExtension, 50, 380, "minLabel", "css/tabLabel.css");
        Slider musicSlider = GraphicElementsSetter.setSlider(0, 1, 50, 100, 700, GameSettings.VolumeOfMusic);
        musicSlider.setOnMouseReleased(mouseEvent -> GameSettings.VolumeOfMusic = musicSlider.getValue());
        Slider soundSlider = GraphicElementsSetter.setSlider(0, 1, 50, 200, 700, GameSettings.VolumeOfMusicEffects);
        soundSlider.setOnMouseReleased(mouseEvent -> GameSettings.VolumeOfMusicEffects = soundSlider.getValue());
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Button configFileBtn = GraphicElementsSetter.setButton(500, 250, "Load File", "css/tabPaneButtons.css");
        configFileBtn.setOnMouseClicked(mouseEvent -> {
            File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
            if (file != null) {
                GameSettings.PathConfigFile = file.toString();
                GameSettings.loadConfigFile();
                currentConfigLabel.setText("Current Configuration File: " + GameSettings.ConfigFileName);
                currentScreenshotExt.setText("Current Extension: " + GameSettings.ImageExtension);
                tabPane.getTabs().get(1).setDisable(true);
            }
        });
        addChildren(mainPane, musicSlider, soundSlider, currentConfigLabel, configFileBtn, currentScreenshotExt);
    }

    public static void updateTimer(Label timerLabel, int gameTime) {
        timerLabel.setVisible(true);
        if (gameTime == GameSettings.GameTime) {
            timerLabel.setVisible(false);
        }
        if (gameTime >= 600 && gameTime % 60 < 10) {
            timerLabel.setText((gameTime - gameTime % 60) / 60 + ":" + "0" + gameTime % 60);
        } else if (gameTime >= 60 && gameTime < 600 && gameTime % 60 >= 10) {
            timerLabel.setText("0" + (gameTime - gameTime % 60) / 60 + ":" + gameTime % 60);
        } else if (gameTime >= 60 && gameTime < 600 && gameTime % 60 < 10) {
            timerLabel.setText("0" + (gameTime - gameTime % 60) / 60 + ":0" + gameTime % 60);
        } else if (gameTime < 60 && gameTime % 60 >= 10) {
            timerLabel.setText("00" + ":" + (gameTime % 60));
        } else if (gameTime > 0 && gameTime < 60) {
            timerLabel.setText("00" + ":0" + (gameTime % 60));
        } else {
            timerLabel.setText((gameTime - gameTime % 60) / 60 + ":" + gameTime % 60);
        }
        timerLabel.setTranslateX(GameSettings.WindowWidth / 2 - timerLabel.getWidth() / 2);
    }
}