import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import java.util.Objects;

public final class GameSoundPlayer {
    private AudioClip backgroundSound;
    private AudioClip endGameSound;
    private AudioClip getPointSound;
    private AudioClip getHitSound;
    private AudioClip regenerateCellHealthSound;

    public GameSoundPlayer() {
        loadSoundFiles();
    }
    private void loadSoundFiles(){
        try {
            backgroundSound = new AudioClip(Objects.requireNonNull(getClass().getResource(GameSettings.BackgroundSound)).toExternalForm());
            backgroundSound.setVolume(GameSettings.VolumeOfMusic);
            backgroundSound.setCycleCount(MediaPlayer.INDEFINITE);
            getPointSound = new AudioClip(Objects.requireNonNull(getClass().getResource(GameSettings.GetScoreSound)).toExternalForm());
            getPointSound.setVolume(GameSettings.VolumeOfMusicEffects);
            endGameSound = new AudioClip(Objects.requireNonNull(getClass().getResource(GameSettings.EndSound)).toExternalForm());
            endGameSound.setVolume(GameSettings.VolumeOfMusicEffects);
            getHitSound = new AudioClip(Objects.requireNonNull(getClass().getResource(GameSettings.HitSound)).toExternalForm());
            getHitSound.setVolume(GameSettings.VolumeOfMusicEffects);
            regenerateCellHealthSound = new AudioClip(Objects.requireNonNull(getClass().getResource(GameSettings.RegenerateCellSound)).toExternalForm());
            regenerateCellHealthSound.setVolume(GameSettings.VolumeOfMusicEffects);
        } catch (NullPointerException e) {
            PlayerInfo.addInformation("[WARNING]There is no music file" + e);
        }
    }
    public void playBackgroundSound() {
        try {
            backgroundSound.setVolume(GameSettings.VolumeOfMusic);
            backgroundSound.play();
        }
        catch (NullPointerException e){
            PlayerInfo.addInformation("[WARNING]There is no file: " + GameSettings.BackgroundSound);
        }
    }
    public void stopBackgroundSound() {
        try{
            backgroundSound.setVolume(GameSettings.VolumeOfMusic);
            backgroundSound.stop();
        }
        catch (NullPointerException e){
            PlayerInfo.addInformation ("[WARNING]There is no file: " + GameSettings.BackgroundSound);
        }
    }
    public void playGetPointSound() {
        try{
            getPointSound.play();
            getPointSound.setVolume(GameSettings.VolumeOfMusicEffects);
        }
        catch (NullPointerException e){
            PlayerInfo.addInformation ("[WARNING]There is no file: " + GameSettings.GetScoreSound);
        }
    }
    public void playHitSound() {
        try{
            getHitSound.setVolume(GameSettings.VolumeOfMusicEffects);
            getHitSound.play();
        }
        catch (NullPointerException e){
            PlayerInfo.addInformation ("[WARNING]There is no file: " + GameSettings.HitSound);
        }
    }
    public void playEndSound() {
        try {
            endGameSound.setVolume(GameSettings.VolumeOfMusicEffects);
            endGameSound.play();
        }
        catch (NullPointerException e){
            PlayerInfo.addInformation("[WARNING]There is no file: " + GameSettings.EndSound);
        }
    }
    public void playRegenerateCellSound() {
        try {
            regenerateCellHealthSound.setVolume(GameSettings.VolumeOfMusicEffects);
            regenerateCellHealthSound.play();
        }
        catch (NullPointerException e){
            PlayerInfo.addInformation("[WARNING]There is no file: " + GameSettings.RegenerateCellSound);
        }
    }
}
