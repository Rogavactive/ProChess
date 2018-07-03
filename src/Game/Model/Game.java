package Game.Model;

import com.google.gson.Gson;
import dbConnection.DataBaseManager;
import javafx.util.Pair;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static Game.Model.Move.deadCol;
import static Game.Model.Move.deadRow;

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
        manager = DataBaseManager.getInstance();
    }

    //for mocking and testing
    public Game(Player player1, Player player2, DataBaseManager manager){
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
        this.manager = manager;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    // This method returns all possible moves
    // for all pieces of current player
    public String pieceMoved(int srcRow, int srcCol, int dstRow, int dstCol) throws CloneNotSupportedException, SQLException {
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
        return new Gson().toJson(result);
    }

    // This method checks wheter cur player has any move
    private boolean noMoveIsPossible(ConcurrentHashMap<Pair<Integer,Integer>,Vector<Pair<Integer,Integer>>> result) {
        for (Pair<Integer, Integer> p :  result.keySet()) {
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
        String updStm = "insert into games (player1ID, player2ID, ColorOfPlayer1, ColorOfPlayer2, winner) " +
                "values (" + player1.getAccount().getID() + ", " + player2.getAccount().getID() + ", " + player1.getColor() + ", "
                + player2.getColor() + ", " + winner + ")";
        manager.executeUpdate(updStm, con);

        // find current game's ID
        int gameID = 0;
        String findStm = "select ID from games where player1ID = " + player1.getAccount().getID() + " and " +
                "player2ID = " + player2.getAccount().getID() + " and " + "ColorOfPlayer1 = " + player1.getColor() + " and " +
                "ColorOfPlayer2 = " + player2.getColor() + " and " + "winner = " + winner + ")";
        ResultSet result = manager.executeQuerry(findStm, con);
        gameID = result.getInt(0);

        // Saves every move
        for(int i = 0; i < history.size(); i++){
            Move curMove = history.get(i);
            String stm = "insert into moves (gameID, srcRow, srcCol, dstRow, dstCol, pieceType, pieceColor) " +
                    "values (" + gameID + ", " + curMove.getTo().getKey() + ", " + curMove.getTo().getValue() + ", "
                    + curMove.getFrom().getKey() + ", " + curMove.getFrom().getValue() + ", " + curMove.getType()
                    + ", " + curMove.getColor() + ")";

            manager.executeUpdate(stm, con);
        }

        manager.closeConnection(con);
    }
    public Vector<Vector<Piece.pieceType>> getBoardState(){
        Vector<Vector<Piece.pieceType>> result = new Vector<Vector<Piece.pieceType>>();

        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            result.add(new Vector<Piece.pieceType>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                result.get(row).add(board.getCell(row,col).getPieceType());
            }
        }
        return result;
    }

 /*   public void click(int row, int col, Player player){
        if(curPlayer!=player)
            return;
        if(clickNum == 0){
            if(!board.getCell(row,col).hasPiece() || board.getCell(row,col).getPieceColor()!=curPlayer.getColor())
                return;
            markedCell = new Pair<Integer, Integer>(row,col);
            possibleMoves = board.getMoves(row,col,curPlayer.getColor());
            // return board.getMoves(row,col,curPlayer.getColor());
        }
        if(markedCell.equals(new Pair<>(row, col))){
            clickNum = 0;
            markedCell = new Pair<>(-1,-1);
            possibleMoves = null;
            return;
        }
        if(!possibleMoves.contains(new Pair<>(row,col)))
            return;
        switchPlayer();
        clickNum=0;
        board.move(markedCell.getKey(), markedCell.getValue(), row, col);
        markedCell = new Pair<>(-1,-1);
        possibleMoves = null;
    }
*/
}
