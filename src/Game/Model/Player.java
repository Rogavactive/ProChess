package Game.Model;

import Accounting.Model.Account;

public class Player {
    private int ID;
    private Account account;
    private boolean color;

    public Player(Account account, int ID, boolean color){
        this.ID = ID;
        this.color = color;
        this.account = account;
    }

    public int getID(){ return this.ID; };

    public Account getAccount(){
        return account;
    }

    public boolean getColor() {
        return color;
    }
}
