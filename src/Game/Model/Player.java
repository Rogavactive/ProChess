package Game.Model;

import Accounting.Model.Account;

public class Player {
    private Account account;
    private Constants.pieceColor color;

    // Constructor
    public Player(Account account, Constants.pieceColor color){
        this.color = color;
        this.account = account;
    }

    // Returns account for player
    public Account getAccount(){
        return account;
    }

    // Returns color of player
    public Constants.pieceColor getColor() {
        return color;
    }
}
