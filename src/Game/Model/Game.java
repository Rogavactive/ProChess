package Game.Model;

import com.google.gson.Gson;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Vector<Move> history;
    private Player curPlayer;

    // Constructor
    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    // This method returns all possible moves
    // for all pieces of current player
    public String pieceMoved(int srcRow, int srcCol, int dstRow, int dstCol) throws CloneNotSupportedException {
        // make move and add it in history
        history.add(new Move(srcRow, srcCol, dstRow, dstCol, (Piece)(board.getCell(srcRow, srcCol).getPiece()).clone()));

        // if piece is killed
        if(board.getCell(dstRow, dstCol).hasPiece()){
            history.add(new Move(dstRow, dstCol, Move.deadRow, Move.deadCol, (Piece)(board.getCell(dstRow, dstCol).getPiece()).clone()));
        }

        board.move(srcRow, srcCol, dstRow, dstCol);
        switchPlayer();

        // Calculate all possible moves
        return new Gson().toJson(board.getAllPossibleMoves(curPlayer.getColor()));
    }

    private void switchPlayer() {
        if(curPlayer == player1) {
            curPlayer = player2;
            return;
        }

        curPlayer = player1;
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
