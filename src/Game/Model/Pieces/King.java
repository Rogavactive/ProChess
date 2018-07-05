package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class King extends Piece {
    private final Constants.pieceColor color;
    private boolean hasMoved;

    // Constructor
    public King(Constants.pieceColor color){
        this.color = color;
        this.hasMoved = false;
    }

    public King(Constants.pieceColor color, boolean hasMoved){
        this.color = color;
        this.hasMoved = hasMoved;
    }

    @Override
    // called when king has done a move
    public void hasMoved() {
        this.hasMoved = true;
    }

    @Override
    // Returns every possible move for king
    public Vector<Pair<Integer, Integer>> possibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        Vector< Pair<Integer, Integer> > result = new Vector<>();

        for(int dr = -1; dr <= 1; dr++)
            for(int dc = -1; dc <= 1; dc++)
                if(!(dr==0 && dc==0)){
                    if(inBounds(row + dr, col + dc) && noAllies(row + dr, col+dc, state) &&
                            noCheckCaused(row,col, row+dr, col+dc, state, new Pair<>(row+dr,col+dc)))
                        result.add(new Pair<>(row+dr,col+dc));
                }

        castling(result, row, col, state);
        return result;
    }

    // This method checks if castling is possible
    private void castling(Vector<Pair<Integer, Integer>> result, int row, int col, Vector<Vector<Cell>> state){
        if(this.hasMoved) return;

        if(!state.get(row).get(col+1).hasPiece() && !state.get(row).get(col+2).hasPiece() &&
                (state.get(row).get(col+3).hasPiece() && !state.get(row).get(col+3).getPiece().getHasMove()))
            result.add(new Pair<>(row, col + 2));
        if(!state.get(row).get(col-1).hasPiece() && !state.get(row).get(col-2).hasPiece() && !state.get(row).get(col-3).hasPiece() &&
                (state.get(row).get(col-4).hasPiece() && !state.get(row).get(col-4).getPiece().getHasMove()))
            result.add(new Pair<>(row, col - 3));
    }

    //returns if there is an allie piece standing on the given location
    private boolean noAllies(int r, int c, Vector<Vector<Cell> > state){
        if(state.get(r).get(c).hasPiece() && state.get(r).get(c).getPieceColor() == this.color) return false;

        return true;
    }

    //returns if the given location is on the board
    private boolean inBounds(int r, int c){
        if(r<0 || r> Constants.NUMBER_OF_ROWS) return false;
        if (c<0|| c> Constants.NUMBER_OF_COLUMNS) return false;

        return true;
    }

    @Override
    public Constants.pieceColor getColor() { return this.color; }

    @Override
    public boolean getHasMove() {
        return this.hasMoved;
    }

    @Override
    public Constants.pieceType getType() {
        return Constants.pieceType.King;
    }

    @Override
    public String toString() {
        String col = "White";
        if(this.color == Constants.pieceColor.black)
            col = "Black";

        return col + " King";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        King newKing = new King(this.getColor(), this.getHasMove());

        return newKing;
    }
}
