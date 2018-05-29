package accounting;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class AccountManager {
    private Connection conn;

    AccountManager() {
        connectDB();
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Called to just initialize JDBC driver
            conn = DriverManager.getConnection(MyDBInfo.MYSQL_DATABASE_SERVER, // Connect to database
                    MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);

            Statement stmt = conn.createStatement();
            stmt.execute("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account register(String username, String email, String password) {
        String pass_hash = hash(password);
        String sqlQuerryStatement = "insert into accounts(username,pass_hash,email) values\n" + "	(\'" + username
                + "\',\'" + pass_hash + "\',\'" + email + "\');";
        try {
            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
            stmt.executeUpdate();
            return new Account(username,email,this);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Account accountExists(String username, String password) {
        String pass_hash = hash(password);
        String sqlQuerryStatement = "select count(ID) as count_matches from accounts\n" + "	where username = \'"
                + username + "\' and pass_hash = \'" + pass_hash + "\';";
        try {
            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
            ResultSet rslt = stmt.executeQuery();
            rslt.next();
            if( 0 != Integer.parseInt(rslt.getString(1))) {
                sqlQuerryStatement = "select email from accounts where username=\""+username+"\";";
                rslt = stmt.executeQuery();
                rslt.next();
                String email = rslt.getString(1);
                System.out.println(email);
                return new Account(username,email,this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsEmail(String email) {
        String sqlQuerryStatement = "select count(ID) as count_matches from accounts\n" + "	where email = \'" + email
                + "\';";
        try {
            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
            ResultSet rslt = stmt.executeQuery();
            rslt.next();
            return 0 != Integer.parseInt(rslt.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean existsUsername(String username) {
        String sqlQuerryStatement = "select count(ID) as count_matches from accounts\n" +
                "	where username = \'"+username+"\';";
        try {
            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
            ResultSet rslt = stmt.executeQuery();
            rslt.next();
            return 0 != Integer.parseInt(rslt.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeAccount(String username) {
        //TODO remove acc with that username from database
        return false;
    }

    public Account googleAccountExists(String email, String password){
        String pass_hash = hash(password);
        //TODO look for this combination and if acc exists return it, otherwise null.
        return null;
    }

    public boolean change(String old_username, String old_email, String username, String email) {
        //TODO if email and username exists and old!=new replace it in database
        return false;
    }

    public boolean setPassword(String oldpass, String newpass, String username) {
        String old_hash = hash(oldpass);
        String new_hash = hash(newpass);
        //TODO checks if old is valid and sets the new one in database
        return false;
    }

    private String hash(String password) {
        // TODO hashes given password
        return password;
    }

}