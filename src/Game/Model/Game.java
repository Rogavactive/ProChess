package Game.Model;

import Accounting.Model.Account;
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
    private boolean playerLeftGame;
    private int winnerByGameLeft;

    // Constructor
    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
        manager = DataBaseMainManager.getInstance();
        playerLeftGame = false;
    }

    // for mocking and testing
    public Game(Player player1, Player player2, DataBaseTestManager manager){
        this.player1 = player1;
        this.player2 = player2;
        this.curPlayer = player1;
        board = new Board();
        history = new Vector<>();
        this.manager = manager;
        playerLeftGame = false;
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
        // if player left game, it's over
        if(playerLeftGame){
            return gameOver(winnerByGameLeft);
        }

        if(curPlayer.getAccount() != acc){
            if(player2.getAccount()==acc && player2.getColor()==Constants.pieceColor.black
                    || player1.getAccount()==acc && player1.getColor()==Constants.pieceColor.black)
                return "B";
            return "W";
        }

        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = board.getAllPossibleMoves(curPlayer.getColor());

        if(noMoveIsPossible(result)){
            if(curPlayer == player1)
                return gameOver(2);
            else
                return gameOver(1);
        }

        return Stringify(result);
    }

    // Making string of possible moves
    private String Stringify( ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result){
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

    // Called when one of players leaves the game
    public synchronized void leaveGame(int userID) throws SQLException {
        // Check if opponent already left earlier
        if(this.playerLeftGame){
            gameOver(winnerByGameLeft);
            return;
        }

        this.playerLeftGame = true;

        // found out who won
        if(player1.getAccount().getID() == userID)
            winnerByGameLeft = 2;
        else
            winnerByGameLeft = 1;
    }

    // This method is called when game is over
    public String gameOver(int winner) throws SQLException {
        saveGame(winner);
        // Return whether player has won, lose or it's draw
        if(curPlayer == player1){
            if(winner == 0)
                return "Draw";
            else if(winner == 1)
                return "You Win";
            else
                return "You Lose";
        }else{
            if(winner == 0)
                return "Draw";
            else if(winner == 2)
                return "You Win";
            else
            return "You Lose";
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
    private String findGameStatement(int winner){
        return "select ID from games where player1ID = " + player1.getAccount().getID() + " and " +
                "player2ID = " + player2.getAccount().getID() + " and " + "ColorOfPlayer1 = " +
                player1.getColorAsBool() + " and " + "ColorOfPlayer2 = " + player2.getColorAsBool() +
                " and winner = " + winner + " order by time DESC";
    }

    // Statement for creating new game in database
    private String insertGameStatement(int winner){
        return "insert into games (player1ID, player2ID, colorOfPlayer1, colorOfPlayer2, winner, time) " +
                "values (" + player1.getAccount().getID() + ", " + player2.getAccount().getID() +
                ", " + player1.getColorAsBool() + ", " + player2.getColorAsBool() + ", " + winner +
                ", '" + getCurrentTime() + "')";
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
