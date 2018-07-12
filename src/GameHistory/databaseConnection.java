package GameHistory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import Game.Model.Constants;
import Game.Model.Move;
import Game.Model.Player;
import dbConnection.DataBaseMainManager;
import dbConnection.DataBaseManager;
import javafx.util.Pair;

public class databaseConnection {
    private static databaseConnection instance = new databaseConnection();
    private static DataBaseManager manager;

    private databaseConnection(){
        manager = DataBaseMainManager.getInstance();
    }

    public static databaseConnection getInstance(){ return instance; }

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

    public Vector<Pair<Integer, Integer>> getLastTenGames(int playerID) throws SQLException {
        Connection con = manager.getConnection();

        String stm = "Select ID, player1ID, player2ID from games where " +
                "player1ID = " + playerID + " or Player2ID = " + playerID +
                " order by time limit 10";
        ResultSet res = manager.executeQuerry(stm, con);

        Vector<Pair<Integer, Integer>> result = new Vector<>();

        while(res.next()){
            if(res.getInt(2) == playerID)
                result.add(new Pair<>(res.getInt(1), res.getInt(3)));
            else
                result.add(new Pair<>(res.getInt(1), res.getInt(2)));
        }

        con.close();

        return result;
    }

    public Vector<Move> findMoves(int gameID) throws SQLException {
        Connection con = manager.getConnection();

        String stm = "Select * from moves where gameID = " + gameID + " order by numberOfMove";
        ResultSet res = manager.executeQuerry(stm, con);

        Vector<Move> result = new Vector<>();

        while(res.next()){
            Constants.pieceColor color;
            Constants.pieceType type;

            if(res.getBoolean(8))
                color = Constants.pieceColor.black;
            else
                color = Constants.pieceColor.white;

            if(res.getInt(7) == 0)
                type = Constants.pieceType.King;
            else if(res.getInt(7) == 1)
                type = Constants.pieceType.Pawn;
            else if(res.getInt(7) == 2)
                type = Constants.pieceType.Bishop;
            else if(res.getInt(7) == 3)
                type = Constants.pieceType.Knight;
            else if(res.getInt(7) == 4)
                type = Constants.pieceType.Rook;
            else
                type = Constants.pieceType.Queen;

            Move curMove = new Move(res.getInt(3), res.getInt(4), res.getInt(5), res.getInt(6),
                    type, color);

            result.add(curMove);
        }

        con.close();

        return result;
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
