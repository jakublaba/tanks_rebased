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
    //własności okna
    public static double WindowWidth = 800;
    public static double WindowHeight = 800;
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
    public static double BulletFrequencyLimit = 0.5;
    public static int BulletNumberLimit = 15;
    public static double BulletRadius = 3;
    public static double BulletVelocityIncrease = 15;
    public static double BulletRadiusDecrease = 2;

    //własności czołgu
    public static double TankVelocity = 3;
    public static String TankBodyImg = "graphics/tankbody.png";
    public static String TankBarrelImg = "graphics/tankhead.png";
    public static double BarrelRotation = 1;
    public static double BarrelAngleLimit = 50;

    //to chyba w ogóle niepotrzebne jest
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
    public static double TimeBetweenCellGenerating = 2;
    public static double TimeBetweenColonyGeneration = 100;
    public static double VolumeOfMusic = 0.25;
    public static double VolumeOfMusicEffects = 0.25;


    //okno ustawień
    private static final ArrayList<String[]> ConfigurationList = new ArrayList<>();
    public static boolean MakeScreenshot = false;

    //plik konfiguracyjny
    public static String PathConfigFile = "src/main/resources/config/configFile.txt";

    public static void setConfigurationList(){
        ConfigurationList.add(new String[]{"Bullet Velocity", "V1", String.valueOf(BulletVelocity)});
        ConfigurationList.add(new String[]{"Number Of Bullets", "X1", String.valueOf(BulletNumberLimit)});
        ConfigurationList.add(new String[]{"Bullet Radius", "R1", String.valueOf(BulletRadius)});
        ConfigurationList.add(new String[]{"Cell Velocity", "V2", String.valueOf(CellVelocity)});
        ConfigurationList.add(new String[]{"Cell Size", "H1", String.valueOf(CellSize)});
        ConfigurationList.add(new String[]{"Cell Health", "P1", String.valueOf(CellHealth)});
        ConfigurationList.add(new String[]{"Interval", "T1", String.valueOf(CellRegenerationInterval)});
        ConfigurationList.add(new String[]{"Cell Regeneration Interval", "T2", String.valueOf(CellRegenerationInterval)});
        ConfigurationList.add(new String[]{"Bullet Velocity Increase", "DV1", String.valueOf(BulletVelocityIncrease)});
        ConfigurationList.add(new String[]{"Cell Velocity Increase", "DV2", String.valueOf(CellVelocityIncrease)});
        ConfigurationList.add(new String[]{"Bullet Radius Decrease", "DR1", String.valueOf(BulletRadiusDecrease)});
        ConfigurationList.add(new String[]{"Cell Size Decrease", "DH1", String.valueOf(CellSizeDecrease)});
        ConfigurationList.add(new String[]{"Game Time", "T3", String.valueOf(GameTime)});
    }
    public static ArrayList<String[]> getConfigurationList(){
        return ConfigurationList;
    }

    public static void loadConfigFile() throws InputMismatchException {
        Scanner readingFile;
        try {
            readingFile = new Scanner(new File(PathConfigFile));
            ConfigFileName = readingFile.nextLine();
            while(readingFile.hasNextLine()) {
                String tmpLine = readingFile.nextLine();
                if(!tmpLine.matches("^\\[[A-Z]+\\d*] .+ \\[(\\d+\\.\\d*|\\d+|JPG|PNG|JPEG|BMP)]$")) {
                    PlayerInfo.addInformation("[WARNING]Incorrect Format:" + tmpLine + " - LINE SKIPPED!");
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
            PlayerInfo.addInformation("[WARNING]No Configuration File: " + PathConfigFile);
        }
    }

    public static boolean setGameSettings(String x, String y) throws NumberFormatException{
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
            case "IMG" -> ImageExtension = y;
            default -> {
                return false;
            }
        }
        return true;
    }

    public static List<TextField> saveConfigFile(List<TextField> textFieldsList, String nameOfFile){
        List <TextField> incorrectTextFieldList = new ArrayList<>();
        String nameOfFileWithExtension = nameOfFile.replace(' ', '_') + ".txt";
        PrintWriter writingFile;
        try {
            writingFile = new PrintWriter("src/main/resources/config/"+ nameOfFileWithExtension);
            writingFile.println(nameOfFile);
            for (int i = 0; i < textFieldsList.size(); i++){
                try{
                    GameSettings.setGameSettings(GameSettings.getConfigurationList().get(i)[1], textFieldsList.get(i).getText());
                    writingFile.println("[" + GameSettings.getConfigurationList().get(i)[1] + "] " + GameSettings.getConfigurationList().get(i)[0].replaceAll(" ", "") + " [" + textFieldsList.get(i).getText() + "]");
                } catch (NumberFormatException e){
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