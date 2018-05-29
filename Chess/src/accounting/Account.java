package accounting;

public class Account {
    private String username;
    private String email;
    private AccountManager manager;

    Account(String username, String email, AccountManager manager) {
        this.username = username;
        this.email = email;
        this.manager = manager;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() { return email;}

    public boolean changePassword(String oldpass, String newpass) {
        return manager.setPassword(oldpass, newpass, username);
    }

    public boolean change(String username, String email) {
        if (manager.change(this.username, this.email, username, email)) {
            this.email = email;
            this.username = username;
            return true;
        }
        return false;
    }

    public boolean remove() {
        return manager.removeAccount(username);
    }
}