package Game.Pieces;

import Game.Board;
import Game.Cell;
import Game.Piece;
import javafx.util.Pair;

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
}
