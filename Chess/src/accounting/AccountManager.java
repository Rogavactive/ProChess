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
        connectDB(null);
    }

    AccountManager(String dbName) {
        connectDB(dbName);
    }

    private void connectDB(String dbName) {
        if (dbName == null)
            dbName = MyDBInfo.MYSQL_DATABASE_NAME;
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Called to just initialize JDBC driver
            conn = DriverManager.getConnection(MyDBInfo.MYSQL_DATABASE_SERVER, // Connect to database
                    MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);

            Statement stmt = conn.createStatement();
            stmt.execute("USE " + dbName);
        } catch (ClassNotFoundException | SQLException e) {
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
            return new Account(username, email, this,true);
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
            if (0 != Integer.parseInt(rslt.getString(1))) {
                String emailStatement = "select email from accounts where username=\'" + username + "\';";
                PreparedStatement emailStmt = (PreparedStatement) conn.prepareStatement(emailStatement);
                ResultSet email_rslt = emailStmt.executeQuery();
                email_rslt.next();
                String email = email_rslt.getString(1);
                return new Account(username, email, this,true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account existsEmail(String email) {
        String sqlQuerryStatement = "select count(ID) as count_matches from accounts\n" + "	where email = \'" + email
                + "\';";
        if(executeExists(sqlQuerryStatement)){
            String username="";
            //TODO: get username from SQL base
            return new Account(username,email,this,false);
        }
        return null;
    }

    private boolean executeExists(String querryStatement) {
        try {
            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(querryStatement);
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
                "	where username = \'" + username + "\';";
        return executeExists(sqlQuerryStatement);
    }

    public Account registerGoogle(String email, String username){
        String sqlQuerryStatement = "insert into accounts(username,email) values\n" + "	(\'" + username
                + "\',\'" + email + "\');";
        try {
            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
            stmt.executeUpdate();
            return new Account(username, email, this,false);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeAccount(String username) {
        //TODO remove acc with that username from database
        return false;
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

    //implement after we start google authentification.
    public boolean sendValidate(String username, String email, String password){
        //TODO: check if account exists, if not send mesasge to email and redirect to validate.jsp
        //TODO: generate code , send it to email and store info with code in Base. (hash password)
        return false;
    }

    //implement after we start google authentification.
    public Account checkValidate(String email, String code){
        //TODO: check if code is valid. if it is, register and send account.
        //if(codeValid) register(getInfoFromBase);
        return null;
    }

}