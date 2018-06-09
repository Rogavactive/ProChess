package Game.Model;

import Accounting.Model.Account;

public class Player {
    private Account account;
    private boolean color;

    public Player(Account account, boolean color){
        this.color = color;
        this.account = account;
    }

    public Account getAccount(){
        return account;
    }

    public boolean getColor() {
        return color;
    }
}
