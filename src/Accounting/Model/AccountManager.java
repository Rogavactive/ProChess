package Accounting.Model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

//import javax.mail.*;
//import javax.mail.internet.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;


public class AccountManager {
    private static MysqlDataSource dataSource = new MysqlDataSource();
    private static ReentrantLock lock = new ReentrantLock();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CHARS_LEN = CHARS.length();
    private static final int CODE_MIN_LEN=40;
    private static final int CODE_MAX_LEN=60;
    private GoogleServices g_services = new GoogleServices();

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
        String sqlQueryStatement = "select count(email) as email_count from (\n" +
                "  select\n" +
                "    email\n" +
                "  from validations\n" +
                "  union\n" +
                "  select\n" +
                "    email\n" +
                "  from accounts\n" +
                ") as v\n" +
                "  where email = \""+email+"\";";

        Connection conn = null;
        ResultSet rslt = null;

        try {
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if (rslt != null && rslt.next()) {
                int res = rslt.getInt("email_count");
                return res > 0;
            }
            return true;//returning true, code will think account exists and stops further searching.
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
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
        String sqlQueryStatement = "select count(username) as count_matches from (\n" +
                "  select\n" +
                "    username\n" +
                "  from validations\n" +
                "  union\n" +
                "  select\n" +
                "    username\n" +
                "  from accounts\n" +
                ") as v\n" +
                "  where username = \""+username+"\";";

        Connection conn = null;
        ResultSet rslt = null;

        try {
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if (rslt != null && rslt.next()) {
                int res = rslt.getInt("count_matches");
                return res > 0;
            }
            return true;//returning true, code will think account exists and stops further searching.
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
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

    public boolean sendValidate(String username, String email, String password){
        if(password==null||password.equals(""))
            return false;
        String code = randomCode();
        String sqlQueryStatement = "insert into validations(username, password, email, code)\n" +
                "  VALUE (\""+username+"\",\""+password+"\",\""+email+"\",\""+code+"\");";
        if(simpleExecuteUpdate(sqlQueryStatement)){
            String messageTOMail = "Hello dear " + username + ",\n\n Please follow the " +
                    "<a href=\"http://localhost:8080/validate.jsp?code="+code+"email="+email+">link</a>" +
                    "to end registration.\n" +
                    "Thank you.";
            if(g_services.sendMail(messageTOMail,email,"ProChess Email Verification"))
                return true;
        }
        return false;
    }

    private String randomCode() {
        StringBuilder bldr = new StringBuilder();
        int codeLen = (int) (Math.random() * (CODE_MAX_LEN-CODE_MIN_LEN)) + CODE_MIN_LEN;
        while(--codeLen!=0){
            char charToAppend = CHARS.charAt((int) (Math.random()*CHARS_LEN));
            bldr.append(charToAppend);
        }
        return bldr.toString();
    }

    //implement after we start google authentification.
    public Account checkValidate(String email, String code){
        if(email==null||code==null)
            return null;
        String sqlQueryStatement = "select * from validations\n" +
                "  where email=\""+email+"\" and code=\""+code+"\";";

        Connection conn=null;
        ResultSet rslt = null;
        try{
            conn = dataSource.getConnection();
            rslt = executeQuerry(sqlQueryStatement,conn);
            if(rslt!=null&&rslt.next()){
                String username = rslt.getString("username");
                String password = rslt.getString("password");
                Account acc = register(username,email,password);
                if(acc!=null){
                    sqlQueryStatement = "delete from validations\n" +
                            "  where email=\""+email+"\";";
                    if(simpleExecuteUpdate(sqlQueryStatement))
                        return acc;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally{
            closeConnections(conn,rslt);
        }
    }

    private class GoogleServices{

        private final String CLIENT_ID = "690644503931-dtn1qj0me45ovni28qbsa12g8d6c2ccf.apps.googleusercontent.com";
        private final String USERNAME = "prochess.noreply";
        private final String PASSWORD = "ProChess";


        public GoogleServices(){
            //nothing
        }

        public boolean sendMail(String text, String toMail, String subject){
//            Properties properties = System.getProperties();
//            String host = "smtp.gmail.com";
//            properties.put("mail.smtp.starttls.enable", "true");
//            properties.put("mail.smtp.host", host);
//            properties.put("mail.smtp.user", USERNAME);
//            properties.put("mail.smtp.password", PASSWORD);
//            properties.put("mail.smtp.port", "587");
//            properties.put("mail.smtp.auth", "true");
//
//            Session session = Session.getDefaultInstance(properties);
//            MimeMessage message = new MimeMessage(session);
//
//            try {
//                message.setFrom(new InternetAddress(USERNAME));
//                InternetAddress toAddress = new InternetAddress(toMail);
//
//                message.addRecipient(Message.RecipientType.TO, toAddress);
//
//                message.setSubject(subject);
//                message.setText(text,"utf-8","html");
//                Transport transport = session.getTransport("smtp");
//                transport.connect(host, USERNAME, PASSWORD);
//                transport.sendMessage(message, message.getAllRecipients());
//                transport.close();
//                return true;
//            } catch (MessagingException me) {
//                me.printStackTrace();
//                return false;
//            }
            return true;
        }

    }

}