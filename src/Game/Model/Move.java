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

    // Returns false if white piece was moved, true otherwise
    public boolean getColorAsBool(){
        if(this.pieceColor == Constants.pieceColor.white)
            return false;

        return true;
    }

    // Each type of piece has it's number
    public int getTypeAsInt(){
        if(this.pieceType == Constants.pieceType.King)
            return 0;
        else if(this.pieceType == Constants.pieceType.Pawn)
            return 1;
        else if(this.pieceType == Constants.pieceType.Bishop)
            return 2;
        else if(this.pieceType == Constants.pieceType.Knight)
            return 3;
        else if(this.pieceType == Constants.pieceType.Rook)
            return 4;
        else
            return 5;
    }
}
