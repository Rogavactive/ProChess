package Game.Model;

import Game.Model.Game;
import javafx.util.Pair;

public class Move {
    public static final int deadRow = -1;
    public static final int deadCol = -1;
    private Pair<Integer, Integer> from;
    private Pair<Integer, Integer> to;
    private Piece piece;

    // Constructor
    public Move(int srcRow, int srcCol, int dstRow, int dstCol, Piece piece){
        this.from = new Pair<>(srcRow, srcCol);
        this.to = new Pair<>(dstRow, dstCol);
        this.piece = piece;
    }
}
