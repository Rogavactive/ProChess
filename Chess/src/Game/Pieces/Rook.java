package Game.Model.Pieces;

import Game.Model.*;

import java.util.Vector;

public class Rook implements Piece {
    private boolean hasMoved;
    private boolean color;

    public Rook(Board board, boolean color){

    }

    @Override
    public Vector<Pair<Integer, Integer>> possibleMoves(int ind1, int ind2, Vector<Vector<Cell>> state) {
        return null;
    }

        @Override
    public boolean getColor() {
        return false;
    }

    @Override
    public Types getType() {
        return Types.ROOK;
    }
}
