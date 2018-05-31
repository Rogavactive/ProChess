package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Piece;
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
