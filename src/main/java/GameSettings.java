import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameSettings {
    public static double WINDOW_WIDTH = 800;
    public static double WINDOW_HEIGHT = 800;
    Color TANKS_FIELD_COLOR_ONE = Color.web("#3798D8"); //Zdefiniowane na później
    Color TANKS_FIELD_COLOR_TWO = Color.web("E4E8EB");
    Color WAR_FIELD_COLOR_ONE = Color.web("0D9068");
    Color WAR_FIELD_COLOR_TWO = Color.web("0D9068");

    //własności komórki
    public static double CELL_SIZE = 10;
    public static double CELL_VELOCITY = 5;
    public static int CELL_HEALTH = 3;
    public static Color [] CELL_COLOR_SEQUENCE = {Color.GREEN, Color.YELLOW, Color.RED};
    public static double CellVelocity = 10;
    public static double CellSize = 20;
    public static int CellHealth = 3;
    public static double CellRegenerationInterval = 10;
    public static double CellVelocityIncrease = 5;
    public static double CellSizeDecrease = 69;

    //własności pocisku
    public static double BULLET_SIZE = 8;
    public static double BULLET_VELOCITY = 15;
    public static Color BULLET_COLOR = Color.BLACK;
    public static double BulletVelocity = 20;
    public static int NumberOfBullets = 15;
    public static double BulletRadius = 5;
    public static double BulletVelocityIncrease = 15;
    public static double BulletRadiusDecrease = 69;

    //własności czołgu
    double BARREL_LENGTH = 5;
    public static String TANK_BODY_IMG = "graphics/tankbody.png";
    public static String TANK_BARREL_IMG = "graphics/tankhead.png";
    public static double BarrelAngleLimit = 69;
    public static KeyCode LeftPlayerMoveUp = KeyCode.UP;
    public static KeyCode LeftPlayerMoveDown = KeyCode.DOWN;
    public static KeyCode LeftPlayerBarrelUp = KeyCode.RIGHT;
    public static KeyCode LeftPlayerBarrelDown = KeyCode.LEFT;
    public static KeyCode LeftPlayerFire = KeyCode.SPACE;
    public static KeyCode RightPlayerMoveUp = KeyCode.W;
    public static KeyCode RightPlayerMoveDown = KeyCode.S;
    public static KeyCode RightPlayerBarrelUp = KeyCode.D;
    public static KeyCode RightPlayerBarrelDown = KeyCode.A;
    public static KeyCode RightPlayerFire = KeyCode.SHIFT;

    //rozgrywka
    public static double GameTime = 200;
    public static double Interval = 3;
    public static String ImageExtension = "PNG";
    public static KeyCode Pause = KeyCode.P;
    public static String configFileName;

    public static void loadConfigFile() throws InputMismatchException {
        Scanner readingFile;
        try{
            readingFile = new Scanner(new File("src/main/resources/config/configFile.txt"));
            configFileName = readingFile.nextLine();
            while(readingFile.hasNextLine()){
                String tmpLine = readingFile.nextLine();
                if(!tmpLine.matches("^\\[[A-Z]+\\d*] .+ \\[(\\d+\\.\\d*|\\d+|JPG|PNG|JPEG|BMP)\\]$")) {
                    System.out.println("INCORRECT FORMAT: " + tmpLine + " - LINE SKIPPED!");
                }
                else {
                    String numberSubstring = tmpLine.substring(tmpLine.lastIndexOf("[") + 1, tmpLine.lastIndexOf("]"));
                    switch (tmpLine.substring(tmpLine.indexOf("[") + 1, tmpLine.indexOf("]"))) {
                        case "V1" -> {
                            BulletVelocity = Double.parseDouble(numberSubstring);
                            System.out.println("Set V1: " + BulletVelocity);
                        }
                        case "X1" -> {
                            NumberOfBullets = Integer.parseInt(numberSubstring);
                            System.out.println("Set X1: " + NumberOfBullets);
                        }
                        case "R1" -> {
                            BulletRadius = Double.parseDouble(numberSubstring);
                            System.out.println("Set R1: " + BulletRadius);
                        }
                        case "V2" -> {
                            CellVelocity = Double.parseDouble(numberSubstring);
                            System.out.println("Set V2: " + CellVelocity);
                        }
                        case "H1" -> {
                            CellSize = Double.parseDouble(numberSubstring);
                            System.out.println("Set H1: " + CellSize);
                        }
                        case "P1" -> {
                            CellHealth = Integer.parseInt(numberSubstring);
                            System.out.println("Set P1: " + CellHealth);
                        }
                        case "T2" -> {
                            CellRegenerationInterval = Double.parseDouble(numberSubstring);
                            System.out.println("Set T2: " + CellRegenerationInterval);
                        }
                        case "T1" -> {
                            Interval = Double.parseDouble(numberSubstring);
                            System.out.println("Set T1: " + Interval);
                        }
                        case "DV1" -> {
                            BulletVelocityIncrease = Double.parseDouble(numberSubstring);
                            System.out.println("Set DV1: " + BulletVelocityIncrease);
                        }
                        case "DV2" -> {
                            CellVelocityIncrease = Double.parseDouble(numberSubstring);
                            System.out.println("Set DV2: " + CellVelocityIncrease);
                        }
                        case "DR1" -> {
                            BulletRadiusDecrease = Double.parseDouble(numberSubstring);
                            System.out.println("Set DR1: " + BulletRadiusDecrease);
                        }
                        case "DH1" -> {
                            CellSizeDecrease = Double.parseDouble(numberSubstring);
                            System.out.println("Set DH1: " + CellSizeDecrease);
                        }
                        case "T3" -> {
                            GameTime = Double.parseDouble(numberSubstring);
                            System.out.println("Set T3: " + GameTime);
                        }
                        case "IMG" -> {
                            ImageExtension = numberSubstring;
                            System.out.println("Set IMG: " + numberSubstring);
                        }
                        default -> System.out.println("Warnings! This line is unhandled: " + tmpLine);
                    }
                }

            }
        }
        catch(FileNotFoundException e){
            System.out.println("There is no config file!");
        }
    }

}