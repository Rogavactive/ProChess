package Game.Model;

import Accounting.Model.Account;
import GameHistory.databaseConnection;
import javafx.util.Pair;
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
    private databaseConnection dbconnection;
    private boolean playerLeftGame;
    private int winnerByGameLeft;
    private String game_ID;

    // Constructor
    public Game(Player player1, Player player2,String id){
        game_ID=id;
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
        playerLeftGame = false;
        dbconnection = databaseConnection.getInstance();
    }

    // Returns first player
    public Player getPlayer1(){
        return player1;
    }

    // Returns second player
    public Player getPlayer2(){
        return player2;
    }

    // Makes move on board
    public void pieceMoved(int srcRow, int srcCol, int dstRow, int dstCol)
            throws CloneNotSupportedException {
        if(srcRow == board.getKingPos(curPlayer.getColor()).getKey() &&
                srcCol == board.getKingPos(curPlayer.getColor()).getValue()){
            if(Math.abs(dstCol-srcCol) > 1)
                castling(srcRow,srcCol,dstRow,dstCol);
        }

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
    }

    // Special move when pawn reaches end of board
    // it can change into another piece
    public void promotion(int row, int col, Constants.pieceType type){
        if(board.getCell(row,col).getPieceType() != Constants.pieceType.Pawn)
            return;
        Constants.pieceColor color = board.getCell(row, col).getPieceColor();
        board.getCell(row,col).removePiece();
        board.getCell(row, col).putPiece(Piece.createPiece(type, color));
    }

    // Special move where two pieces change their position
    private void castling(int srcRow, int srcCol, int dstRow, int dstCol){
        assert (srcCol - dstCol == 3 || dstCol - srcCol == 2);
        if(srcCol - dstCol == 3){
            history.add(new Move(srcRow, srcCol - 4, dstRow, dstCol + 1,
                    board.getCell(srcRow, srcCol - 4).getPieceType(),
                    board.getCell(srcRow, srcCol - 4).getPieceColor()));

            board.move(srcRow,srcCol - 4, dstRow,dstCol + 1);
        }else{
            history.add(new Move(srcRow, srcCol + 3, dstRow, dstCol - 1,
                    board.getCell(srcRow, srcCol + 3).getPieceType(),
                    board.getCell(srcRow, srcCol + 3).getPieceColor()));

            board.move(srcRow, srcCol + 3,dstRow,dstCol - 1);
        }
    }

    // Possible moves for given player in current state of the board
    public String getCurrentPossibleMoves(Account acc) throws SQLException {
        // when player demands moves, it means another player
        // has already done a move or leave a game

        // if player left game, it's over
        if(playerLeftGame){
            return gameOver(winnerByGameLeft);
        }

        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = board.getAllPossibleMoves(curPlayer.getColor());

        if(noMoveIsPossible(result)){
            if(curPlayer == player1)
                return gameOver(2);
            else
                return gameOver(1);
        }

        if(curPlayer.getAccount() != acc){
            if(player2.getAccount()==acc && player2.getColor()==Constants.pieceColor.black
                    || player1.getAccount()==acc && player1.getColor()==Constants.pieceColor.black)
                return "";
            return "";
        }

        return Stringify(result);
    }

    public String getPlayerColor(Account acc){
        String color;
        if(acc==player1.getAccount()) {
            if (player1.getColor() == Constants.pieceColor.white) {
                color = "W";
            } else {
                color = "B";
            }
        }
        else{
            if (player2.getColor() == Constants.pieceColor.white) {
                color = "W";
            } else {
                color = "B";
            }
        }
        return color;
    }

    // Making string of possible moves
    private String Stringify( ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result){
        String res="";

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

            boolean castling = false;
            // if last move was castling, return king to it's place
            if(lastMove.getType() == Constants.pieceType.King &&
                    Math.abs( lastMove.getTo().getValue() - lastMove.getFrom().getValue() ) > 1 ){
                castling = true;

                board.move(lastMove.getTo().getKey(), lastMove.getTo().getValue(),
                        lastMove.getFrom().getKey(), lastMove.getFrom().getValue());
                // king is at the starting position again
                board.getCell(lastMove.getFrom().getKey(), lastMove.getFrom().getValue()).getPiece().setHasMoved(false);

                history.remove(history.size() - 1);
                lastMove = history.get(history.size() - 1);
            }

            // undo last move
            board.move(lastMove.getTo().getKey(), lastMove.getTo().getValue(),
                    lastMove.getFrom().getKey(), lastMove.getFrom().getValue());
            // if castling was done, return rook to it's starting position
            if(castling)
                board.getCell(lastMove.getFrom().getKey(), lastMove.getFrom().getValue()).getPiece().setHasMoved(false);

            history.remove(history.size() - 1);
        }
    }

    // This method is called when game is over
    public String gameOver(int winner) throws SQLException {
        dbconnection.saveGame(history, winner, player1, player2);
        // Return whether player has won, lose or it's draw
        if(winner==0){
            return "Draw";
        }
        if(curPlayer == player1){
            if(winner == 1)
                return "You Win";
            else
                return "You Lose";
        }else{
            if(winner == 2)
                return "You Win";
            else
                return "You Lose";
        }
    }

    // Called when one of players leaves the game
    public synchronized void leaveGame(int userID) throws SQLException {
        // Check if opponent already left earlier
        if(this.playerLeftGame){
            gameOver(winnerByGameLeft);
            GameManager.getInstance().endGame(game_ID);
            return;
        }

        this.playerLeftGame = true;

        // found out who won
        if(player1.getAccount().getID() == userID)
            winnerByGameLeft = 2;
        else
            winnerByGameLeft = 1;
    }

    // Returns copy of board
    public String getBoardState(){
        String result="";

        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {


                if (board.getCell(row,col).getPieceType()==Constants.pieceType.emptyCell){
                    result+="00";
                } else {
                    if (board.getCell(row,col).getPieceColor()==Constants.pieceColor.white) {
                        result += 'W';
                    } else {
                        result += 'B';
                    }
                    if (board.getCell(row,col).getPieceType()==Constants.pieceType.Knight) {
                        result += 'N';
                    } else {
                        result += board.getCell(row, col).getPieceType().toString().charAt(0);
                    }

                }
            }
        }

        return result;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }
}
