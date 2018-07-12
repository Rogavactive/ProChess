package Game.Model;

import Accounting.Model.Account;
import Game.Controller.GameSocket;
import GameConnection.Controller.GameSearchEndpoint;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;

public class Player {
    private Account account;
    private Constants.pieceColor color;
    private long remainingTimeSeconds;
    private long bonusTimeSeconds;
    private boolean myTurn;
    private long turnStartTimeMillis;
    private Timer timer;
    private Game myGame;
    // Constructor
    public Player(Account account, Constants.pieceColor color){
        this.color = color;
        this.account = account;
        this.myTurn = false;
        this.remainingTimeSeconds = -1;
        this.bonusTimeSeconds = -1;
        this.turnStartTimeMillis = 0;
        this.timer = new Timer();
        this.myGame = null;
    }

    public void setGame(Game game){
        this.myGame = game;
    }
    // Returns account for player
    public Account getAccount(){
        return account;
    }

    public void setType(GameType type){
        this.bonusTimeSeconds = type.getBonusTime();
        this.remainingTimeSeconds = type.getTotalTimeSeconds();
    }
    // Returns color of player
    public Constants.pieceColor getColor() {
        return color;
    }

    // Returns false if player has white pieces
    // true if player has black pieces
    public boolean getColorAsBool(){
        if(this.color == Constants.pieceColor.white)
            return false;

        return true;
    }

    public void startTurn() {
        assert (remainingTimeSeconds>0);
        myTurn=true;
        turnStartTimeMillis = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myGame.timePassedFor(account);
            }
        },remainingTimeSeconds*1000);
        System.out.println(account.getUsername() + " started turn, time remaining:" + getRemainingTimeSeconds());
    }
    public void endTurn(){
        timer.cancel();
        timer.purge();
        myTurn =false;
        remainingTimeSeconds += bonusTimeSeconds;
        remainingTimeSeconds -= (System.currentTimeMillis() - turnStartTimeMillis)/1000;
        System.out.println(account.getUsername() + " ended turn, time remaining:" + getRemainingTimeSeconds());
    }
    public long getRemainingTimeSeconds(){
        if(myTurn)
            return remainingTimeSeconds - (System.currentTimeMillis() - turnStartTimeMillis)/1000;
        return remainingTimeSeconds;
    }
}
