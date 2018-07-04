package Accounting.Model;

public class Account {
    private String username;
    private String email;
    private AccountManager manager;
    private int id;
    private boolean type; //if google account: false, if native: true

    public Account(String username, String email, AccountManager manager, int id ,boolean type) {
        this.username = username;
        this.email = email;
        this.manager = manager;
        this.type = type;
        this.id = id;
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized String getEmail() {
        return email;
    }

    public synchronized boolean type(){
        return type;
    }

    public synchronized int getID(){
        return id;
    }

    public synchronized boolean changePassword(String oldpass, String newpass) {
        return manager.setPassword(oldpass, newpass, username);
    }

    public synchronized boolean change(String username, String email) {
        if (manager.change(this.username, this.email, username, email)) {
            this.email = email;
            this.username = username;
            return true;
        }else{
            return false;
        }
    }

    public synchronized boolean remove() {
        return manager.removeAccount(username);
    }
}