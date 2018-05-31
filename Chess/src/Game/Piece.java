package Game;

import javafx.util.Pair;

import java.util.Vector;

public interface Piece {
    public Vector< Pair<Integer, Integer> > possibleMoves(int ind1, int ind2, Vector< Vector<Cell> > state);
    public boolean getColor();
}
