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
        int[] ranks = manager.getRanks(id);
        if(ranks==null)
            throw new IllegalArgumentException();
        bulletRaiting = ranks[0];
        blitzRaiting = ranks[1];
        classicalRaiting = ranks[2];
        int[] matches = manager.getMatches(id);
        if(matches==null)
            throw new IllegalArgumentException();
        bulletMatches = matches[0];
        blitzMatches = matches[1];
        classicalMatches = matches[2];
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
        return bulletRaiting;
    }

    public synchronized int getBlitzRaiting() {
        return blitzRaiting;
    }

    public synchronized int getClassicalRaiting() {
        return classicalRaiting;
    }

    public synchronized boolean changePassword(String oldpass, String newpass) {
        if(manager.setPassword(oldpass, newpass, id)){
            type = true;
            return true;
        }
        return false;
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

    public synchronized boolean changeRating(int change,int gameType){
        switch (gameType) {
            case 0:
                bulletMatches++;
                if(!manager.setMatches(id,gameType,bulletMatches))
                    return false;
                bulletRaiting+=change;
                if(manager.setRating(id,gameType,bulletRaiting))
                    return false;
                break;
            case 1:
                blitzMatches++;
                if(manager.setMatches(id,gameType,blitzMatches))
                    return false;
                blitzRaiting+=change;
                if(manager.setRating(id,gameType,blitzRaiting))
                    return false;
                break;
            case 2:
                classicalMatches++;
                if(!manager.setMatches(id,gameType,classicalMatches))
                    return false;
                classicalRaiting+=change;
                if(!manager.setRating(id,gameType,classicalRaiting))
                    return false;
                break;
        }
        return true;
    }

    public synchronized boolean remove() {
        return manager.removeAccount(id);
    }
}