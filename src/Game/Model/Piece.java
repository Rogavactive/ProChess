package Game.Model;

import javafx.util.Pair;

import java.util.Vector;

public interface Piece {
    enum pieceType{
        Bishop,
        King,
        Knight,
        Pawn,
        Queen,
        Rook
    }

    Vector< Pair<Integer, Integer> > possibleMoves(int row, int col,
                                                          Vector<Vector<Cell>> state, Pair<Integer,Integer> allieKingPos);
    boolean getColor();
    boolean getHasMove();
    pieceType getType();
}
