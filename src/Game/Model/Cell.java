package Game.Model;

import javafx.util.Pair;

import java.util.Vector;

public class Cell implements Cloneable{
    private int row;
    private int col;
    private Piece piece;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
    }

    public boolean hasPiece(){
        return piece != null;
    }

    public Vector<Pair<Integer, Integer>> getMoves(Vector< Vector<Cell> > state, Pair<Integer, Integer> allieKingPos){
        if(this.piece == null) return  new Vector< Pair<Integer, Integer> >();
        return piece.possibleMoves(this.row, this.col, state, allieKingPos);
    }

    public boolean getPieceColor(){
        assert(this.piece != null);
        return this.piece.getColor();
    }

    public Piece getPiece() {
        return piece;
    }

    public void putPiece(Piece piece){
        this.piece = piece;
    }

    public void removePiece(){
        this.piece = null;
    }

    public Piece.pieceType getPieceType(){
        assert (this.piece != null);
        return piece.getType();
    }

    public boolean pieceHasMoved(){
        return piece.getHasMove();
    }
}
