package Game.Model;

import dbConnection.DataBaseMainManager;
import dbConnection.DataBaseManager;
import dbConnection.DataBaseTestManager;

import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CHARS_LEN = CHARS.length();
    private static final int CODE_LEN=60;

    private DataBaseManager manager;
    private ConcurrentHashMap<String,Game> games;
    private static GameManager testInstance = new GameManager(DataBaseTestManager.getInstance());
    private static GameManager mainInstance = new GameManager(DataBaseMainManager.getInstance());

    private GameManager(DataBaseMainManager manager){
        this.manager = manager;
        games = new ConcurrentHashMap<>();
    }

    public static GameManager getInstance(){
        return mainInstance;
    }

    public static GameManager getTestInstance(){
        return testInstance;
    }

    private GameManager(DataBaseTestManager manager){
        this.manager = manager;
        games = new ConcurrentHashMap<>();
    }

    public String registerGame(Player player1, Player player2, GameType type) {
        //id damtxvevis shansebi imdenad mcirea, rom ugulvebelvyoft;
        String id = generateRandomCode();
        Game game = new Game(player1,player2, type, id);
        games.put(id,game);
        return id;
        //register a game, store it and return an id
    }

    private String generateRandomCode() {
        StringBuilder bldr = new StringBuilder();
        int codeLen = CODE_LEN;
        while(--codeLen!=0){
            char charToAppend = CHARS.charAt((int) (Math.random()*CHARS_LEN));
            bldr.append(charToAppend);
        }
        return bldr.toString();
    }
    public Game getGameByID(String id){
        return games.get(id);
    }
    public void endGame(String gameID){
        games.remove(gameID);
    }

}
