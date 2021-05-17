import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public final class GameSettings {
    //własności okna
    public static double WindowWidth = 800;
    public static double WindowHeight = 800;
    Color TANKS_FIELD_COLOR_ONE = Color.web("#3798D8"); //Zdefiniowane na później
    Color TANKS_FIELD_COLOR_TWO = Color.web("E4E8EB");
    Color WAR_FIELD_COLOR_ONE = Color.web("0D9068");
    Color WAR_FIELD_COLOR_TWO = Color.web("0D9068");
    public static double WidthOfTankBorder = 120;

    //własności komórki
    public static Color [] CellColorSequence = {Color.OLIVEDRAB, Color.GOLD, Color.CRIMSON};
    public static double CellVelocity = 10;
    public static double CellSize = 20;
    public static int CellHealth = 3;
    public static double CellRegenerationInterval = 10;
    public static double CellVelocityIncrease = 5;
    public static double CellSizeDecrease = 69;

    //własności pocisku
    public static double BulletSize = 15;
    public static double BulletVelocity = 0.5;
    public static Color BulletColor = Color.BLACK;
    public static double BulletFrequencyLimit = 2;
    public static int BulletNumberLimit = 15;
    public static double BulletRadius = 3;
    public static double BulletVelocityIncrease = 15;
    public static double BulletRadiusDecrease = 2;

    //własności czołgu
    public static double TankVelocity = 5;
    public static String TankBodyImg = "graphics/tankbody.png";
    public static String TankBarrelImg = "graphics/tankhead.png";
    public static double BarrelRotation = 1;
    public static double BarrelAngleLimit = 50;

    //dałeś bindingi na odwrót
    public static final KeyCode RightPlayerMoveUp = KeyCode.UP;
    public static final KeyCode RightPlayerMoveDown = KeyCode.DOWN;
    public static final KeyCode RightPlayerBarrelUp = KeyCode.RIGHT;
    public static final KeyCode RightPlayerBarrelDown = KeyCode.LEFT;
    public static final KeyCode RightPlayerFire = KeyCode.SPACE;
    public static final KeyCode LeftPlayerMoveUp = KeyCode.W;
    public static final KeyCode LeftPlayerMoveDown = KeyCode.S;
    public static final KeyCode LeftPlayerBarrelUp = KeyCode.D;
    public static final KeyCode LeftPlayerBarrelDown = KeyCode.A;
    public static final KeyCode LeftPlayerFire = KeyCode.SHIFT;

    //rozgrywka
    public static double GameTime = 200;
    public static double Interval = 3;
    public static String ImageExtension = "PNG";
    public static KeyCode Pause = KeyCode.P;
    public static String ConfigFileName;
    public static double TimeBetweenCellGenerating = 1;

    //okno ustawień
    public static String[] configuration = new String[] {"Bullet Velocity","V1","NumberOfBullets","X1","BulletRadius","R1","CellVelocity","V2","CellSize","H1","CellHealth","P1","CellRegenerationInterval","T2","Interval","T1","BulletVelocityIncrease","DV1","CellVelocityIncrease","DV2","BulletRadiusDecrease","DR1","CellSizeDecrease","DH1","GameTime","T3"};

    public static void loadConfigFile() throws InputMismatchException {
        Scanner readingFile;
        try {
            readingFile = new Scanner(new File("src/main/resources/config/configFile.txt"));
            ConfigFileName = readingFile.nextLine();
            while(readingFile.hasNextLine()){
                String tmpLine = readingFile.nextLine();
                if(!tmpLine.matches("^\\[[A-Z]+\\d*] .+ \\[(\\d+\\.\\d*|\\d+|JPG|PNG|JPEG|BMP)\\]$")) {
                    System.out.println("INCORRECT FORMAT: " + tmpLine + " - LINE SKIPPED!");
                }
                else {
                    if(setGameSettings(tmpLine.substring(tmpLine.indexOf("[") + 1, tmpLine.indexOf("]")), tmpLine.substring(tmpLine.lastIndexOf("[") + 1, tmpLine.lastIndexOf("]"))) == false)
                        System.out.println("Warnings! This line is unhandled: " + tmpLine);
                }

            }
        }
        catch(FileNotFoundException e){
            System.out.println("There is no config file!");
        }
    }

    public static boolean setGameSettings(String x, String y) throws NumberFormatException{
        switch (x) {
            case "V1" -> {
                BulletVelocity = Double.parseDouble(y);
                System.out.println("Set V1: " + BulletVelocity);
            }
            case "X1" -> {
                BulletNumberLimit = Integer.parseInt(y);
                System.out.println("Set X1: " + BulletNumberLimit);
            }
            case "R1" -> {
                BulletRadius = Double.parseDouble(y);
                System.out.println("Set R1: " + BulletRadius);
            }
            case "V2" -> {
                CellVelocity = Double.parseDouble(y);
                System.out.println("Set V2: " + CellVelocity);
            }
            case "H1" -> {
                CellSize = Double.parseDouble(y);
                System.out.println("Set H1: " + CellSize);
            }
            case "P1" -> {
                CellHealth = Integer.parseInt(y);
                System.out.println("Set P1: " + CellHealth);
            }
            case "T2" -> {
                CellRegenerationInterval = Double.parseDouble(y);
                System.out.println("Set T2: " + CellRegenerationInterval);
            }
            case "T1" -> {
                Interval = Double.parseDouble(y);
                System.out.println("Set T1: " + Interval);
            }
            case "DV1" -> {
                BulletVelocityIncrease = Double.parseDouble(y);
                System.out.println("Set DV1: " + BulletVelocityIncrease);
            }
            case "DV2" -> {
                CellVelocityIncrease = Double.parseDouble(y);
                System.out.println("Set DV2: " + CellVelocityIncrease);
            }
            case "DR1" -> {
                BulletRadiusDecrease = Double.parseDouble(y);
                System.out.println("Set DR1: " + BulletRadiusDecrease);
            }
            case "DH1" -> {
                CellSizeDecrease = Double.parseDouble(y);
                System.out.println("Set DH1: " + CellSizeDecrease);
            }
            case "T3" -> {
                GameTime = Double.parseDouble(y);
                System.out.println("Set T3: " + GameTime);
            }
            case "IMG" -> {
                ImageExtension = y;
                System.out.println("Set IMG: " + y);
            }
            default -> {
                return false;
            }
        }
        return true;
    }

}