package Game.Pieces;

import Game.Board;
import Game.Cell;
import Game.Piece;
import javafx.util.Pair;

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
}
