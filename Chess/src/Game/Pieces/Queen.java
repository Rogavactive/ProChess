package Game.Model.Pieces;

import Game.Model.*;

import java.util.Vector;

public class Queen implements Piece {
    private boolean color;

    public Queen(boolean color){
        this.color = color;
    }

    @Override
    public Vector<Pair<Integer, Integer>> possibleMoves(int ind1, int ind2, Vector<Vector<Cell>> state) {
        return null;
    }

    @Override
    public boolean getColor() {
        return color;
    }

    @Override
    public Types getType() {
        return Types.QUEEN;
    }
}
