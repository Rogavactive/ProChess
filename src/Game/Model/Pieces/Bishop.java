package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Bishop implements Piece {
    private boolean color;
    private boolean hasMove;

    public Bishop(boolean color){
        this.color = color;
        this.hasMove = false;
    }

    @Override
    public Vector<Pair<Integer, Integer>> possibleMoves(int ind1, int ind2, Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        return null;
    }

    @Override
    public boolean getColor() {
        return color;
    }

    @Override
    public boolean getHasMove() {
        return hasMove;
    }

    @Override
    public pieceType getType() {
        return pieceType.Bishop;
    }
}
