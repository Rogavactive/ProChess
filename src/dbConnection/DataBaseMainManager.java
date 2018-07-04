package dbConnection;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseMainManager extends DataBaseManager {

    private static DataBaseMainManager managerInstance = new DataBaseMainManager();
    private MysqlDataSource dataSource = new MysqlDataSource();

    public static DataBaseMainManager getInstance() {return managerInstance;}

    private DataBaseMainManager(){
        connectDB();
    }

    private void connectDB() {
        dataSource.setUser(MyDBInfo.MYSQL_USERNAME);
        dataSource.setPassword(MyDBInfo.MYSQL_PASSWORD);
        dataSource.setUrl(MyDBInfo.MYSQL_DATABASE_SERVER);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
