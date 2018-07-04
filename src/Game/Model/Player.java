package Game.Model;

import Accounting.Model.Account;

public class Player {
    private Account account;
    private boolean color;

    // Constructor
    public Player(Account account, boolean color){
        this.color = color;
        this.account = account;
    }

    // Returns account for player
    public Account getAccount(){
        return account;
    }

    // Returns color of player
    public boolean getColor() {
        return color;
    }
}
