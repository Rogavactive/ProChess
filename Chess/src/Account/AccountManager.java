package Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class AccountManager {
	private Connection conn;

	public AccountManager() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void register(String username, String email, String password) {
		String sqlQuerryStatement = "insert into accounts(username,pass_hash,email) values\n" + "	(\'" + username
				+ "\',\'" + password + "\',\'" + email + "\');";
		try {
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public boolean accountExists(String username, String password) {
		String sqlQuerryStatement = "select count(ID) as count_matches from accounts\n" + "	where username = \'"
				+ username + "\' and pass_hash = \'" + password + "\';";
		try {
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlQuerryStatement);
			ResultSet rslt = stmt.executeQuery();
			rslt.next();
			return 0 != Integer.parseInt(rslt.getString(1));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return false;
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
