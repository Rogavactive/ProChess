package dbConnection;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseTestManager extends DataBaseManager {

    private static DataBaseTestManager managerInstance = new DataBaseTestManager();
    private MysqlDataSource dataSource = new MysqlDataSource();

    public static DataBaseTestManager getInstance() {
        return managerInstance;
    }

    private DataBaseTestManager() {
        connectDB();
    }

    private void connectDB() {
        dataSource.setUser(MyDBInfo.MYSQL_USERNAME);
        dataSource.setPassword(MyDBInfo.MYSQL_PASSWORD);
        dataSource.setUrl(MyDBInfo.MYSQL_DATABASE_TEST_SERVER);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
