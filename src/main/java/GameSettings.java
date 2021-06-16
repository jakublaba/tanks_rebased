import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static javafx.scene.paint.Color.rgb;

public final class GameSettings {
    //Window
    public static double WindowWidth = 800;
    public static double WindowHeight = 800;
    public static double WidthOfTankBorder = 120;

    //Cell
    public static Color[] CellColorSequence = {rgb(7, 110, 41, 0.6), rgb(0, 153, 51, 0.6), rgb(0, 204, 102, 0.6), rgb(153, 255, 204, 0.6),
            rgb(255, 255, 102, 0.6), rgb(255, 204, 0, 0.6), rgb(255, 153, 0, 0.6), rgb(255, 51, 0, 0.6), rgb(255, 0, 0, 0.6)};
    public static double CellVelocity = 50;
    public static double CellSize = 20;
    public static int CellHealth = 10;
    public static double CellRegenerationInterval = 15;
    public static double CellVelocityIncrease = 1;
    public static double CellSizeDecrease = 2;

    //Bullet
    public static double BulletRadius = 15;
    public static double BulletVelocity = 8;
    public static Color LeftPlayerBulletColor = rgb(204, 0, 0, 0.7);
    public static Color RightPlayerBulletColor = rgb(0, 102, 255, 0.7);
    public static final double BulletFrequencyLimit = 0.1;
    public static int BulletNumberLimit = 10;
    public static double BulletVelocityIncrease = 1;
    public static double BulletRadiusDecrease = 1;

    //Tank
    public static double TankVelocity = 3;
    public static String TankBodyImg = "graphics/tankbody.png";
    public static String TankBarrelImg = "graphics/tankhead.png";
    public static double BarrelRotation = 1;
    public static double BarrelAngleLimit = 50;

    //Control
    public static KeyCode RightPlayerMoveUp = KeyCode.UP;
    public static KeyCode RightPlayerMoveDown = KeyCode.DOWN;
    public static KeyCode RightPlayerBarrelUp = KeyCode.RIGHT;
    public static KeyCode RightPlayerBarrelDown = KeyCode.LEFT;
    public static KeyCode RightPlayerFire = KeyCode.SHIFT;
    public static KeyCode LeftPlayerMoveUp = KeyCode.W;
    public static KeyCode LeftPlayerMoveDown = KeyCode.S;
    public static KeyCode LeftPlayerBarrelUp = KeyCode.A;
    public static KeyCode LeftPlayerBarrelDown = KeyCode.D;
    public static KeyCode LeftPlayerFire = KeyCode.J;
    public static KeyCode Pause = KeyCode.P;

    //Game
    public static double GameTime = 300;
    public static double Interval = 0.7;
    public static final String ImageExtension = "PNG";
    public static double TimeBetweenCellGenerating = 1;
    public static double TimeBetweenColonyGeneration = 3;
    public static double VolumeOfMusic = 0.05;
    public static double VolumeOfMusicEffects = 0.15;
    public static String BackgroundSound = "sound/background.mp3";
    public static String EndSound = "sound/end.wav";
    public static String GetScoreSound = "sound/getScore.wav";
    public static String HitSound = "sound/hit.wav";
    public static String RegenerateCellSound = "sound/regenerate.wav";

    //Settings
    private static final List<String[]> ConfigurationList = new ArrayList<>();
    public static boolean MakeScreenshot = false;

    //Configuration File
    public static String ConfigFileName;
    public static String PathConfigFile = "src/main/resources/config/game/default_settings.txt";

    public static void setConfigurationList() {
        ConfigurationList.add(new String[]{"Bullet Velocity", "V1", String.valueOf(BulletVelocity)});
        ConfigurationList.add(new String[]{"Number Of Bullets", "X1", String.valueOf(BulletNumberLimit)});
        ConfigurationList.add(new String[]{"Bullet Radius", "R1", String.valueOf(BulletRadius)});
        ConfigurationList.add(new String[]{"Cell Velocity", "V2", String.valueOf(CellVelocity)});
        ConfigurationList.add(new String[]{"Cell Size", "H1", String.valueOf(CellSize)});
        ConfigurationList.add(new String[]{"Cell Health", "P1", String.valueOf(CellHealth)});
        ConfigurationList.add(new String[]{"Interval", "T1", String.valueOf(Interval)});
        ConfigurationList.add(new String[]{"Cell Regeneration Interval", "T2", String.valueOf(CellRegenerationInterval)});
        ConfigurationList.add(new String[]{"Bullet Velocity Increase", "DV1", String.valueOf(BulletVelocityIncrease)});
        ConfigurationList.add(new String[]{"Cell Velocity Increase", "DV2", String.valueOf(CellVelocityIncrease)});
        ConfigurationList.add(new String[]{"Bullet Radius Decrease", "DR1", String.valueOf(BulletRadiusDecrease)});
        ConfigurationList.add(new String[]{"Cell Size Decrease", "DH1", String.valueOf(CellSizeDecrease)});
        ConfigurationList.add(new String[]{"Game Time", "T3", String.valueOf(GameTime)});
        ConfigurationList.add(new String[]{"Time Cells Generating", "T4", String.valueOf(TimeBetweenCellGenerating)});
        ConfigurationList.add(new String[]{"Time Colonies Generating", "T5", String.valueOf(TimeBetweenColonyGeneration)});
    }

    public static List<String[]> getConfigurationList() {
        return ConfigurationList;
    }

    public static void loadConfigFile() throws InputMismatchException {
        Scanner readingFile;
        try {
            readingFile = new Scanner(new File(PathConfigFile));
            ConfigFileName = readingFile.nextLine();
            while (readingFile.hasNextLine()) {
                String tmpLine = readingFile.nextLine();
                if (!tmpLine.matches("^\\[[A-Z]+\\d*] .+ \\[(\\d+\\.\\d*|\\d+|JPG|PNG|JPEG|BMP)]$")) {
                    PlayerInfo.addInformation("[WARNING]Incorrect Format:" + tmpLine + " - LINE SKIPPED!");
                } else {
                    if (!setGameSettings(tmpLine.substring(tmpLine.indexOf("[") + 1, tmpLine.indexOf("]")), tmpLine.substring(tmpLine.lastIndexOf("[") + 1, tmpLine.lastIndexOf("]"))))
                        PlayerInfo.addInformation("[WARNING]This line is unhandled: " + tmpLine);
                }
            }
        } catch (FileNotFoundException e) {
            PlayerInfo.addInformation("[WARNING]No Configuration File: " + PathConfigFile);
        }
    }

    public static boolean setGameSettings(String x, String y) throws NumberFormatException {
        if (Double.parseDouble(y) < 0) {
            throw new NumberFormatException();
        }
        switch (x) {
            case "V1" -> BulletVelocity = Double.parseDouble(y);
            case "X1" -> BulletNumberLimit = Integer.parseInt(y);
            case "R1" -> BulletRadius = Double.parseDouble(y);
            case "V2" -> CellVelocity = Double.parseDouble(y);
            case "H1" -> CellSize = Double.parseDouble(y);
            case "P1" -> CellHealth = Integer.parseInt(y);
            case "T2" -> CellRegenerationInterval = Double.parseDouble(y);
            case "T1" -> Interval = Double.parseDouble(y);
            case "DV1" -> BulletVelocityIncrease = Double.parseDouble(y);
            case "DV2" -> CellVelocityIncrease = Double.parseDouble(y);
            case "DR1" -> BulletRadiusDecrease = Double.parseDouble(y);
            case "DH1" -> CellSizeDecrease = Double.parseDouble(y);
            case "T3" -> GameTime = Double.parseDouble(y);
            case "T4" -> TimeBetweenCellGenerating = Double.parseDouble(y);
            case "T5" -> TimeBetweenColonyGeneration = Double.parseDouble(y);
            default -> {
                return false;
            }
        }
        return true;
    }

    public static List<TextField> saveConfigFile(List<TextField> textFieldsList, String nameOfFile) {
        List<TextField> incorrectTextFieldList = new ArrayList<>();
        String nameOfFileWithExtension = nameOfFile.replace(' ', '_') + ".txt";
        PrintWriter writingFile;
        try {
            writingFile = new PrintWriter("src/main/resources/config/" + nameOfFileWithExtension);
            writingFile.println(nameOfFile);
            for (int i = 0; i < textFieldsList.size(); i++) {
                try {
                    GameSettings.setGameSettings(GameSettings.getConfigurationList().get(i)[1], textFieldsList.get(i).getText());
                    writingFile.println("[" + GameSettings.getConfigurationList().get(i)[1] + "] " + GameSettings.getConfigurationList().get(i)[0].replaceAll(" ", "") + " [" + textFieldsList.get(i).getText() + "]");
                } catch (NumberFormatException e) {
                    incorrectTextFieldList.add(textFieldsList.get(i));
                }
            }
            writingFile.close();
            PlayerInfo.addInformation("[GameSettings]Configuration File Saved: " + nameOfFileWithExtension);
        } catch (FileNotFoundException e) {
            PlayerInfo.addInformation("[GameSettings]Saving Failed: " + nameOfFileWithExtension);
        }
        return incorrectTextFieldList;
    }
}