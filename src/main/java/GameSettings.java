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
    public static double BulletSize = 15;
    public static double BulletVelocity = 0.5;
    public static Color BulletColor = Color.BLACK;
    public static double BulletFrequencyLimit = 0.01;
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
    public static double TimeBetweenCellGenerating = 0.02  ;

    //okno ustawień
    private static final ArrayList<String[]> configurationList = new ArrayList<>();

    public static void setConfigurationList(){
        configurationList.add(new String[]{"Bullet Velocity", "V1", String.valueOf(BulletVelocity)});
        configurationList.add(new String[]{"NumberOfBullets", "X1", String.valueOf(BulletNumberLimit)});
        configurationList.add(new String[]{"BulletRadius", "R1", String.valueOf(BulletRadius)});
        configurationList.add(new String[]{"CellVelocity", "V2", String.valueOf(CellVelocity)});
        configurationList.add(new String[]{"CellSize", "H1", String.valueOf(CellSize)});
        configurationList.add(new String[]{"CellHealth", "P1", String.valueOf(CellHealth)});
        configurationList.add(new String[]{"Interval", "T1", String.valueOf(CellRegenerationInterval)});
        configurationList.add(new String[]{"CellRegenerationInterval", "T2", String.valueOf(CellRegenerationInterval)});
        configurationList.add(new String[]{"BulletVelocityIncrease", "DV1", String.valueOf(BulletVelocityIncrease)});
        configurationList.add(new String[]{"CellVelocityIncrease", "DV2", String.valueOf(CellVelocityIncrease)});
        configurationList.add(new String[]{"BulletRadiusDecrease", "DR1", String.valueOf(BulletRadiusDecrease)});
        configurationList.add(new String[]{"CellSizeDecrease", "DH1", String.valueOf(CellSizeDecrease)});
        configurationList.add(new String[]{"GameTime", "T3", String.valueOf(GameTime)});
    }
    public static ArrayList<String[]> getConfigurationList(){
        return configurationList;
    }



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
                    String numberSubstring = tmpLine.substring(tmpLine.lastIndexOf("[") + 1, tmpLine.lastIndexOf("]"));
                    switch (tmpLine.substring(tmpLine.indexOf("[") + 1, tmpLine.indexOf("]"))) {
                        case "V1" -> {
                            BulletVelocity = Double.parseDouble(numberSubstring);
                            System.out.println("Set V1: " + BulletVelocity);
                        }
                        case "X1" -> {
                            BulletNumberLimit = Integer.parseInt(numberSubstring);
                            System.out.println("Set X1: " + BulletNumberLimit);
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