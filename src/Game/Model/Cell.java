package Game.Model;

import javafx.util.Pair;

import java.util.Vector;

public class Cell{
    private int row;
    private int col;
    private Piece piece;

    // Constructor
    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        piece = null;
    }

    public Cell(Cell cell) {
        this.row = cell.row;
        this.col = cell.col;

        if(cell.piece == null)
            this.piece = null;
        else{
            try {
                this.piece = (Piece) cell.piece.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.out.println("Clone not supported");
            }
        }
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
        if( !this.hasPiece() ) return  new Vector<>();
        // otherwise piece has it's own possible moves
        return piece.possibleMoves(this.row, this.col, state, allieKingPos);
    }

    // This method returns color of piece on cell
    public Constants.pieceColor getPieceColor(){
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

    // This method removes piece from cell
    public void removePiece(){
        this.piece = null;
    }

    // This method returns type of piece, which is placed on cell
    public Constants.pieceType getPieceType(){
        if(piece == null) return Constants.pieceType.emptyCell;
        return piece.getType();
    }
    public String toString(){
        return row + "-" + col + "-" + getPieceType();
    }

}
