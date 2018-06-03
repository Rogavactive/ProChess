package Accounting.Model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.security.*;
import java.util.concurrent.locks.ReentrantLock;


public class AccountManager {
    private static MysqlDataSource dataSource = new MysqlDataSource();
    private static ReentrantLock lock = new ReentrantLock();

    public AccountManager() {
        this("");
    }

    public AccountManager(String dbName) {
        connectDB(dbName);
    }

    private void connectDB(String dbName) {
        dataSource.setUser(MyDBInfo.MYSQL_USERNAME);
        dataSource.setPassword(MyDBInfo.MYSQL_PASSWORD);
        if(!dbName.equals(""))
            dataSource.setUrl(dbName);
        else
            dataSource.setUrl(MyDBInfo.MYSQL_DATABASE_SERVER);
    }

    public void dispose() {
        //we dont need anything yet.
    }

    private ResultSet executeQuerry(String statement,Connection conn){
        try {
            PreparedStatement stmt = conn.prepareStatement(statement);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean executeUpdate(String statement,Connection conn){
        try {
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account register(String username, String email, String password) {
        String pass_hash = hash(password);
        if(pass_hash!=null&&pass_hash.equals(""))
            return null;
        String sqlQueryStatement;
        if(pass_hash==null) {
            sqlQueryStatement = "insert into accounts(username,email) values\n" + "	(\'" + username
                    + "\',\'" + email + "\');";
        }
        else {
            sqlQueryStatement = "insert into accounts(username,pass_hash,email) values\n" + "	(\'" + username
                    + "\',\'" + pass_hash + "\',\'" + email + "\');";
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            if (executeUpdate(sqlQueryStatement,conn)) {
                if(password==null)
                    return new Account(username,email,this,false);
                else
                    return new Account(username, email, this, true);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            try{
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Account accountExists(String username, String password) {
        if(password==null)
            return null;
        String pass_hash = hash(password);
        String sqlQueryStatement = "select email from accounts\n" + "	where username = \'"
                + username + "\' and pass_hash = \'" + pass_hash + "\';";

        Connection conn = null;
        ResultSet rslt = null;

        try {
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if (rslt != null && rslt.next()) {
                String email = rslt.getString("email");
                return new Account(username, email, this, true);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            closeConnections(conn,rslt);
        }

    }

    public boolean existsEmail(String email) {
        String sqlQueryStatement = "select count(ID) as email_count from accounts\n" + "	where email = \'" + email
                + "\';";

        Connection conn = null;
        ResultSet rslt = null;

        try {
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if (rslt != null && rslt.next()) {
                int res = rslt.getInt("email_count");
                return res > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            closeConnections(conn,rslt);
        }
    }

    private void closeConnections(Connection conn, ResultSet rslt){
        try {
            if (rslt != null) {
                rslt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsUsername(String username) {
        String sqlQueryStatement = "select count(ID) as count_matches from accounts\n" +
                "	where username = \'" + username + "\';";

        Connection conn = null;
        ResultSet rslt = null;

        try {
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if (rslt != null && rslt.next()) {
                int res = rslt.getInt("count_matches");
                return res > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            closeConnections(conn,rslt);
        }

    }

    public boolean removeAccount(String username) {
        String sqlQueryStatement = "delete from accounts where username=\""+username+"\";";
        return simpleExecuteUpdate(sqlQueryStatement);
    }

    private boolean simpleExecuteUpdate(String statement) {
        try (Connection conn = dataSource.getConnection()) {
            return executeUpdate(statement, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean change(String old_username, String old_email, String username, String email) {
        String sqlQueryStatement = "update accounts set username=\""+username+"\", email=\""+email+"\" " +
                "where username=\""+old_username+"\" and email=\""+old_email+"\";";
        return simpleExecuteUpdate(sqlQueryStatement);
    }

    public Account googleAccountExists(String email){
        String sqlQueryStatement = "select username,pass_hash from accounts\n" +
                "  where email=\""+email+"\";";

        Connection conn = null;
        ResultSet rslt = null;

        try {
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if (rslt != null && rslt.next()) {
                String username = rslt.getString("username");
                boolean isNative = rslt.getString("pass_hash")!=null;
                return new Account(username,email,this,isNative);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            closeConnections(conn,rslt);
        }

    }

    public boolean setPassword(String oldpass, String newpass, String username) {
        String old_hash = hash(oldpass);
        String new_hash = hash(newpass);
        if(old_hash.equals("")||new_hash.equals(""))
            return false;
        String sqlQueryStatement = "update accounts set pass_hash=\""+new_hash+"\" " +
                "where username=\""+username+"\" and pass_hash=\""+old_hash+"\";";
        return simpleExecuteUpdate(sqlQueryStatement);
    }

    private String hash(String password) {
        if(password==null)
            return null;
        try {
            lock.lock();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            return hexToString(md.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return "";
        }finally {
            lock.unlock();
        }
    }

    private static String hexToString(byte[] bytes) {
        StringBuilder buff = new StringBuilder();
        for (byte aByte : bytes) {
            int val = aByte;
            val = val & 0xff;  // remove higher bits, sign
            if (val < 16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
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