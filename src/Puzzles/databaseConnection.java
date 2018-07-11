package Puzzles;

import dbConnection.DataBaseMainManager;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class databaseConnection {
    private DataBaseMainManager manager;

    public databaseConnection(){
        manager = DataBaseMainManager.getInstance();
    }

    public int puzzlesCount() throws SQLException {
        Connection con = manager.getConnection();

        String stm = "select count(*) as puzzle_num from puzzles";
        ResultSet result = manager.executeQuerry(stm, con);
        result.first();

        int c = result.getInt("puzzle_num");
        con.close();

        return c;
    }

    public Puzzle getPuzzle(int puzzleNumber) throws SQLException {
        Connection con = manager.getConnection();

        if(puzzleNumber > puzzlesCount() || puzzleNumber <= 0)
            return null;

        String stm = "select * from puzzles where ID = " + puzzleNumber;
        ResultSet result = manager.executeQuerry(stm, con);

        result.first();
        Puzzle p = new Puzzle(result.getString(2), result.getString(3), result.getString(4));

        con.close();

        return p;
    }
}
