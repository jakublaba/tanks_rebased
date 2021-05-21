public class PlayerInfo {
    private Tank tank;
    private int score;

    public PlayerInfo (char side) {
        tank = new Tank(side);
        score = 0;
    }
    public int getScore () { return score; }
    public Tank getTank () { return tank; }
    public void increaseScore (int points) { score += points; }
}