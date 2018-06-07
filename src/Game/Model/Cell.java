package Game.Model;

import javafx.util.Pair;

import java.util.Vector;

public class Cell implements Cloneable{
    private int row;
    private int col;
    private Piece piece;

    // Constructor
    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        piece = null;
    }

    // This method returns whether Cell has peace
    public boolean hasPiece(){
        return piece != null;
    }

    // This method returns every possible move for
    // piece on this cell
    public Vector<Pair<Integer, Integer>> getMoves(Vector< Vector<Cell> > state,
                                                   Pair<Integer, Integer> allieKingPos){
        // if cell is empty, there is no possible moves
        if( !this.hasPiece() ) return  new Vector< Pair<Integer, Integer> >();
        // otherwise piece has it's own possible moves
        return piece.possibleMoves(this.row, this.col, state, allieKingPos);
    }

    // This method returns color of piece on cell
    public boolean getPieceColor(){
        assert(this.piece != null);
        return this.piece.getColor();
    }

    // This method returns piece, which is placed on given cell
    public Piece getPiece() {
        return piece;
    }

    // This method adds given piece on cell
    public void putPiece(Piece piece){
        this.piece = piece;
    }

    // This method remoce piece from cell
    public void removePiece(){
        this.piece = null;
    }

    // This method returns type of piece, which is placed on cell
    public Piece.pieceType getPieceType(){
        assert (this.piece != null);
        return piece.getType();
    }

    // This method returns whether piece,
    // placed on this cell, has already done a move
    public boolean pieceHasMoved(){
        return piece.getHasMove();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Cell newCell = new Cell(row, col);
        newCell.putPiece(this.getPiece());

        return newCell;
    }
}
