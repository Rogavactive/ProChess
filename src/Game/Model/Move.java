package Game.Model;

import javafx.util.Pair;

public class Move {
    public static final int deadRow = -1;
    public static final int deadCol = -1;

    private boolean pieceColor;
    private Piece.pieceType pieceType;
    private Pair<Integer, Integer> from;
    private Pair<Integer, Integer> to;

    // Constructor
    public Move(int srcRow, int srcCol, int dstRow, int dstCol, Piece.pieceType pieceType, boolean pieceColor) {
        this.from = new Pair<>(srcRow, srcCol);
        this.to = new Pair<>(dstRow, dstCol);
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
    }

    // Returns destination cell of move
    public Pair<Integer, Integer> getTo(){
        return this.to;
    }

    // Returns source cell of move
    public Pair<Integer, Integer> getFrom(){
        return this.from;
    }

    // Returns piece color, which was moved
    public boolean getColor(){
        return this.pieceColor;
    }

    // Returns piece type, which was moved
    public Piece.pieceType getType(){
        return this.pieceType;
    }
}
