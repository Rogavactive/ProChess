package dbConnection;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DataBaseManager {

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
        } catch (SQLException e ) {
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
        return null;
    }
}
