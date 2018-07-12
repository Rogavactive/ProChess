package Game.Model;

public class GameType {
    private int totalTimeSeconds;
    private int bonusTimeSeconds;
    public GameType(String totalTimeMinutes, String bonusTimeSecconds){
        this.bonusTimeSeconds =Integer.parseInt(bonusTimeSecconds);
        this.totalTimeSeconds = Integer.parseInt(totalTimeMinutes)*60;
    }

    public int getBonusTime(){
        return bonusTimeSeconds;
    }

    public int getTotalTimeSeconds() {
        return totalTimeSeconds;
    }
    public static String getGameType(int totalTimeMinutes){
        if(totalTimeMinutes < 3 && totalTimeMinutes > 0)
            return "Bullet";
        if(totalTimeMinutes >= 3 && totalTimeMinutes < 8)
            return "Blitz";
        if(totalTimeMinutes >=8)
            return "Classical";
        return "Invalid Game Type";
    }
}
