package Game.Model.Pieces;

import Game.Model.*;


import java.util.Vector;

public class Knight implements Piece {
    private boolean color;

    public Knight(Board board, boolean color){

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
        return Types.KNIGHT;
    }
}
