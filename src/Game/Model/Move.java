package Game.Model;

import javafx.util.Pair;

public class Move {
    private Constants.pieceColor pieceColor;
    private Constants.pieceType pieceType;
    private Pair<Integer, Integer> from;
    private Pair<Integer, Integer> to;

    // Constructor
    public Move(int srcRow, int srcCol, int dstRow, int dstCol,
                Constants.pieceType pieceType, Constants.pieceColor pieceColor) {
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
    public Constants.pieceColor getColor(){
        return this.pieceColor;
    }

    // Returns piece type, which was moved
    public Constants.pieceType getType(){
        return this.pieceType;
    }
}
