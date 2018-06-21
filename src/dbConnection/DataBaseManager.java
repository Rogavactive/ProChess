package dbConnection;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseManager {


    private MysqlDataSource dataSource = new MysqlDataSource();

    public DataBaseManager(){
        this("");
    }

    public DataBaseManager(String dbName){
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

    public void Dispose(){
        //nothing yet
    }

    public ResultSet executeQuerry(String statement, Connection conn){
        try {
            PreparedStatement stmt = conn.prepareStatement(statement);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean executeUpdate(String statement,Connection conn){
        try {
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnections(Connection conn, ResultSet rslt){
        try {
            if (rslt != null) {
                rslt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(conn);
    }

    public void closeConnection(Connection conn){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeResultset(ResultSet rslt){
        try {
            if (rslt != null) {
                rslt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
