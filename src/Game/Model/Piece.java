package Game.Model;

import Game.Model.Pieces.King;
import javafx.util.Pair;

import java.util.Vector;

public abstract class Piece {
    public enum pieceType{
        Bishop,
        King,
        Knight,
        Pawn,
        Queen,
        Rook
    }
    protected boolean color;
    protected boolean hasMoved;

    public Piece(boolean color){
        this.color = color;
        hasMoved = false;
    }

    protected boolean noCheckCaused(int row, int col, int newRow, int newCol,
                                  Vector<Vector<Cell>> state, Pair<Integer,Integer> allieKingPos) {
        boolean check = false;
        Piece killedPiece = state.get(newRow).get(newCol).getPiece();
        Piece curPiece = state.get(row).get(col).getPiece();
        state.get(row).get(col).removePiece();
        state.get(newRow).get(newCol).putPiece(curPiece);
        if(((King)state.get(allieKingPos.getKey()).get(allieKingPos.getValue()).getPiece()).checkForCheck(state, allieKingPos))
            check = true;
        state.get(row).get(col).putPiece(curPiece);
        state.get(newRow).get(newCol).putPiece(killedPiece);
        return check;
    }

    public abstract Vector< Pair<Integer, Integer> > possibleMoves(int row, int col,
                                                          Vector<Vector<Cell>> state, Pair<Integer,Integer> allieKingPos);
    public boolean getColor(){
        return this.color;
    }
    public boolean getHasMove(){
        return hasMoved;
    }
    public abstract pieceType getType();
}
