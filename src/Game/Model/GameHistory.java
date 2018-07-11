package Game.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import dbConnection.DataBaseMainManager;
import dbConnection.DataBaseManager;
import dbConnection.DataBaseTestManager;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;

public class GameHistory {
    private static GameHistory instance = new GameHistory();
    private static DataBaseManager manager;

    private GameHistory(){
        manager = DataBaseMainManager.getInstance();
    }

    public static GameHistory getInstance(){ return instance; }

    // This method saves game in database
    public void saveGame(Vector<Move> history, int winner,
                         Player player1, Player player2) throws SQLException {
        Connection con = manager.getConnection();

        // Creates new game in database
        String updStm = insertGameStatement(winner, player1, player2);
        manager.executeUpdate(updStm, con);

        // find current game's ID
        int gameID = -1;

        String findStm = findGameStatement(winner, player1, player2);
        ResultSet result = manager.executeQuerry(findStm, con);
        result.first();
        gameID = result.getInt(1);

        // Saves every move
        for(int i = 0; i < history.size(); i++){
            String stm = addMovesStatement(gameID, history.get(i), i);

            manager.executeUpdate(stm, con);
        }

        manager.closeConnection(con);
    }

    // Statement for adding moves in database
    private String addMovesStatement(int gameID, Move curMove, int order){
        return "insert into moves (gameID, srcRow, srcCol, dstRow, dstCol, pieceType, pieceColor, numberOfMove) " +
                "values (" + gameID + ", " + curMove.getFrom().getKey() + ", " + curMove.getFrom().getValue() + ", "
                + curMove.getTo().getKey() + ", " + curMove.getTo().getValue() + ", " + curMove.getTypeAsInt()
                + ", " + curMove.getColorAsBool() + ", " + order + ")";
    }

    // Returns current date and time
    private String getCurrentTime(){
        java.util.Date date = new java.util.Date();

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateFormat.format(date);

        return currentTime;
    }

    // Statement for finding game in database
    private String findGameStatement(int winner, Player player1, Player player2){
        return "select ID from games where player1ID = " + player1.getAccount().getID() + " and " +
                "player2ID = " + player2.getAccount().getID() + " and " + "ColorOfPlayer1 = " +
                player1.getColorAsBool() + " and " + "ColorOfPlayer2 = " + player2.getColorAsBool() +
                " and winner = " + winner + " order by time DESC";
    }

    // Statement for creating new game in database
    private String insertGameStatement(int winner, Player player1, Player player2){
        return "insert into games (player1ID, player2ID, colorOfPlayer1, colorOfPlayer2, winner, time) " +
                "values (" + player1.getAccount().getID() + ", " + player2.getAccount().getID() +
                ", " + player1.getColorAsBool() + ", " + player2.getColorAsBool() + ", " + winner +
                ", '" + getCurrentTime() + "')";
    }
}
