package Game.Model;

import dbConnection.DataBaseMainManager;
import dbConnection.DataBaseManager;
import dbConnection.DataBaseTestManager;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static Game.Model.Constants.deadCol;
import static Game.Model.Constants.deadRow;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Vector<Move> history;
    private Player curPlayer;
    private DataBaseManager manager;

    // Constructor
    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
        manager = DataBaseMainManager.getInstance();
    }

    // for mocking and testing
    public Game(Player player1, Player player2, DataBaseTestManager manager){
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
        this.manager = manager;
    }

    // Returns first player
    public Player getPlayer1(){
        return player1;
    }

    // Returns second player
    public Player getPlayer2(){
        return player2;
    }

    // This method returns all possible moves
    // for all pieces of current player
    public String pieceMoved(int srcRow, int srcCol, int dstRow, int dstCol)
            throws CloneNotSupportedException, SQLException {
        // make move and add it in history
        history.add(new Move(srcRow, srcCol, dstRow, dstCol,
                board.getCell(srcRow, srcCol).getPieceType(),board.getCell(srcRow, srcCol).getPieceColor()));

        // if piece is killed
        if(board.getCell(dstRow, dstCol).hasPiece()){
            history.add(new Move(dstRow, dstCol, deadRow, deadCol,
                    board.getCell(srcRow, srcCol).getPieceType(),board.getCell(srcRow, srcCol).getPieceColor()));
        }

        board.move(srcRow, srcCol, dstRow, dstCol);
        switchPlayer();

        // Calculate all possible moves
        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = board.getAllPossibleMoves(curPlayer.getColor());
        if(noMoveIsPossible(result)){
            gameOver(true);
        }

        return Stringify(result);
    }

    public String Castling(String len){
        if(len == "ooo"){

        }else{

        }
        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = board.getAllPossibleMoves(curPlayer.getColor());
        return Stringify(result);
    }
    public String getStartingMoves(){
        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = board.getAllPossibleMoves(player1.getColor());
        return Stringify(result);
    }

    public String Stringify( ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result){
        String res;
        if(curPlayer.getColor()== Constants.pieceColor.black)
            res = "B";
        else
            res = "W";
        for(Pair<Integer,Integer> key : result.keySet()){
            Vector<Pair<Integer,Integer>> val = result.get(key);
            for(int i  = 0; i < val.size(); i++){
                res += key.getKey() + "" + key.getValue() + "" + val.get(i).getKey() + "" + val.get(i).getValue();
            }
        }
        return res;
    }
    // This method checks whether current player has any move
    private boolean noMoveIsPossible(ConcurrentHashMap<Pair<Integer,Integer>,Vector<Pair<Integer,Integer>>> result) {
        // Check for every piece of current player
        for (Pair<Integer, Integer> p :  result.keySet()) {
            // If any piece has some moves, there is at least  one possible move
            if( !(result.get(p).isEmpty()) )
                return false;
        }

        return true;
    }

    // This method switches players
    private void switchPlayer() {
        if(curPlayer == player1) {
            curPlayer = player2;
            return;
        }

        curPlayer = player1;
    }

    // This method undoes last move
    public void undo(){
        if(!history.isEmpty()){
            // if last move killed any of pieces
            // return killed piece to it's place
            Move lastMove = history.get(history.size() - 1);
            if(lastMove.getTo().equals(new Pair<>(deadRow, deadCol))){
                Pair<Integer, Integer> cor = lastMove.getFrom();
                board.addPiece(cor.getKey(), cor.getValue(), Piece.createPiece(lastMove.getType(), lastMove.getColor()));
                history.remove(history.size() - 1);
                lastMove = history.get(history.size() - 1);
            }else{
                // else just clear this cell
                Pair<Integer, Integer> cor = lastMove.getFrom();
                board.addPiece(cor.getKey(), cor.getValue(), null);
            }

            // undo last move
            Pair<Integer, Integer> cor = lastMove.getFrom();
            board.addPiece(cor.getKey(), cor.getValue(), Piece.createPiece(lastMove.getType(), lastMove.getColor()));
            history.remove(history.size() - 1);
        }
    }

    // This method is called when game is over
    public void gameOver(boolean winnerExists) throws SQLException {
        // if game ended as draw
        if(!winnerExists){
            saveGame(0);
        }else{
            if(curPlayer.equals(player1)){
                saveGame(2);
            }else{
                saveGame(1);
            }
        }
    }

    // This method saves game in database
    private void saveGame(int winner) throws SQLException {
        Connection con = manager.getConnection();

        // Creates new game in database
        String updStm = insertGameStatement(winner);
        manager.executeUpdate(updStm, con);

        // find current game's ID
        int gameID = -1;

        String findStm = findGameStatement(winner);
        ResultSet result = manager.executeQuerry(findStm, con);
        gameID = result.getInt(1);

        // Saves every move
        for(int i = 0; i < history.size(); i++){
            String stm = addMovesStatement(gameID, history.get(i));

            manager.executeUpdate(stm, con);
        }

        manager.closeConnection(con);
    }

    // Statement for adding moves in database
    private String addMovesStatement(int gameID, Move curMove){
        return "insert into moves (gameID, srcRow, srcCol, dstRow, dstCol, pieceType, pieceColor) " +
                "values (" + gameID + ", " + curMove.getFrom().getKey() + ", " + curMove.getFrom().getValue() + ", "
                + curMove.getTo().getKey() + ", " + curMove.getTo().getValue() + ", " + curMove.getType()
                + ", " + curMove.getColor() + ")";
    }

    // Statement for finding game in database
    private String findGameStatement(int winner){
        return "select ID from games where player1ID = " + player1.getAccount().getID() + " and " +
                "player2ID = " + player2.getAccount().getID() + " and " + "ColorOfPlayer1 = " +
                player1.getColor() + " and " + "ColorOfPlayer2 = " + player2.getColor() +
                " and winner = " + winner + ")";
    }

    // Statement for creating new game in database
    private String insertGameStatement(int winner){
        return "insert into games (player1ID, player2ID, colorOfPlayer1, colorOfPlayer2, winner) " +
                "values (" + player1.getAccount().getID() + ", " + player2.getAccount().getID() +
                ", " + player1.getColor() + ", " + player2.getColor() + ", " + winner + ")";
    }

    // Returns copy of board
    public Vector<Vector<Constants.pieceType>> getBoardState(){
        Vector<Vector<Constants.pieceType>> result = new Vector<Vector<Constants.pieceType>>();

        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            result.add(new Vector<Constants.pieceType>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                result.get(row).add(board.getCell(row,col).getPieceType());
            }
        }

        return result;
    }
}
