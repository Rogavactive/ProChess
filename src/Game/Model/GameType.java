package Game.Model;

public class GameType {
    private long totalTimeSeconds;
    private long bonusTimeSeconds;
    public GameType(String totalTimeMinutes, String bonusTimeSecconds){
        this.bonusTimeSeconds =Long.parseLong(bonusTimeSecconds);
        this.totalTimeSeconds = Long.parseLong(totalTimeMinutes)*60;
    }

    public long getBonusTime(){
        return bonusTimeSeconds;
    }

    public long getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public int getGameTypeAsInt(){
        if(totalTimeSeconds < (3 * 60) && totalTimeSeconds > 0)
            return 0;
        else if(totalTimeSeconds >= (3 * 60) && totalTimeSeconds < (8 * 60) )
            return 1;
        else
            return 2;
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
