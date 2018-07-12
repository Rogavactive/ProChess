package GameHistory;

import Game.Model.Board;
import Game.Model.Constants;
import Game.Model.Move;
import Game.Model.Piece;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class GameHistory {
    private Board board;
    private Vector<Move> history;
    private databaseConnection con = databaseConnection.getInstance();
    int currentMove;

    public GameHistory(int gameID) throws SQLException {
        board = new Board();
        history = con.findMoves(gameID);
        currentMove = 0;
    }

    public Pair<String, String> nextMove(){
        String move = new String();
        if(currentMove < history.size()){
            Move curMove = history.get(currentMove);
            currentMove++;

            if(currentMove < history.size()){
                Move nextMove = history.get(currentMove);
                if(nextMove.getTo().getKey() == Constants.deadRow){
                    currentMove++;
                }else if(nextMove.getType() == Constants.pieceType.King &&
                        Math.abs(nextMove.getTo().getValue() - nextMove.getFrom().getValue()) > 1){
                    currentMove++;
                    board.move(nextMove.getFrom().getKey(), nextMove.getFrom().getValue(),
                            nextMove.getTo().getKey(), nextMove.getTo().getValue());
                }
            }

            board.move(curMove.getFrom().getKey(), curMove.getFrom().getValue(),
                    curMove.getTo().getKey(), curMove.getTo().getValue());

            if(curMove.getColor() == Constants.pieceColor.white)
                move += 'W';
            else
                move += 'B';

            if(curMove.getType() == Constants.pieceType.King)
                move += 'K';
            else if(curMove.getType() == Constants.pieceType.Pawn)
                move += 'P';
            else if(curMove.getType() == Constants.pieceType.Rook)
                move += 'R';
            else if(curMove.getType() == Constants.pieceType.Knight)
                move += 'N';
            else if(curMove.getType() == Constants.pieceType.Bishop)
                move += 'B';
            else
                move += 'Q';



            move += curMove.getFrom().getKey();
            move += curMove.getFrom().getValue();
            move += curMove.getTo().getKey();
            move += curMove.getTo().getValue();
        }

        return new Pair<>(getBoardState(), move);
    }

    public String previousMove(){
        if(currentMove - 1 >= 0){
            currentMove--;
            Move curMove = history.get(currentMove);

            if(curMove.getTo().getKey() == Constants.deadRow){
                currentMove--;
                Move prewMove = history.get(currentMove);

                board.move(prewMove.getTo().getKey(), prewMove.getTo().getValue(),
                        prewMove.getFrom().getKey(), prewMove.getFrom().getValue());

                board.getCell(curMove.getFrom().getKey(), curMove.getFrom().getValue()).putPiece(
                        Piece.createPiece(curMove.getType(), curMove.getColor())
                );

                return getBoardState();
            }else if(curMove.getType() == Constants.pieceType.King
                    && Math.abs(curMove.getTo().getValue() - curMove.getFrom().getValue()) > 1 ){
                board.move(curMove.getTo().getKey(), curMove.getTo().getValue(),
                        curMove.getFrom().getKey(), curMove.getFrom().getValue());

                currentMove--;
                curMove = history.get(currentMove);
            }

            board.move(curMove.getTo().getKey(), curMove.getTo().getValue(),
                    curMove.getFrom().getKey(), curMove.getFrom().getValue());

        }

        return getBoardState();
    }

    private String getBoardState(){
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
}
