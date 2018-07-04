package Accounting.Model;

public class Account {

    private String username;
    private String email;
    private AccountManager manager;
    private int id;
    private boolean type; //if google account: false, if native: true

    private int bulletRaiting;
    private int blitzRaiting;
    private int classicalRaiting;

    private int bulletMatches;
    private int blitzMatches;
    private int classicalMatches;

    public Account(String username, String email, AccountManager manager, int id, boolean type) {
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

    public synchronized int getBulletMatches() {
        return bulletMatches;
    }

    public synchronized int getBlitzMatches() {
        return blitzMatches;
    }

    public synchronized int getClassicalMatches() {
        return classicalMatches;
    }

    public synchronized int getBulletRaiting() {
        if (this.getBulletMatches() > 0)
            return bulletRaiting;
        return this.getDefaultRaiting();
    }

    public synchronized int getBlitzRaiting() {
        if (this.getBlitzMatches() > 0)
            return blitzRaiting;
        return this.getDefaultRaiting();
    }

    public synchronized int getClassicalRaiting() {
        if (this.getClassicalMatches() > 0)
            return classicalRaiting;
        return this.getDefaultRaiting();
    }

    public synchronized int getDefaultRaiting() {
        return 1500;
    }

    public synchronized boolean changePassword(String oldpass, String newpass) {
        return manager.setPassword(oldpass, newpass, username);
    }

    public synchronized boolean change(String username, String email) {
        if (manager.change(this.username, this.email, username, email)) {
            this.email = email;
            this.username = username;
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean remove() {
        return manager.removeAccount(username);
    }
}