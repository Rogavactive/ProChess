package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Knight extends Piece {
    private final Constants.pieceColor color;
    private boolean hasMoved;
    private int[] dr,dc;
    private final int MAX_MOVES_COUNT = 8;

    // Constructor
    public Knight(Constants.pieceColor color){
        this.color = color;
        this.hasMoved = false;

        //initialise possible move arrays ( knight can move from (r,c) to (r+dr[i],c+dc[i]))
        this.dr = new int[] {2, 2, 1, 1, -1, -1, -2, -2};
        this.dc = new int[] {-1, 1, -2, 2, -2, 2, -1, 1};
    }

    public Knight(Constants.pieceColor color, boolean hasMoved){
        this.color = color;
        this.hasMoved = hasMoved;

        //initialise possible move arrays ( knight can move from (r,c) to (r+dr[i],c+dc[i]))
        this.dr = new int[] {2, 2, 1, 1, -1, -1, -2, -2};
        this.dc = new int[] {-1, 1, -2, 2, -2, 2, -1, 1};
    }

    @Override
    // Called when knight has done a move
    public void hasMoved() {
        this.hasMoved = true;
    }

    @Override
    // Returns every possible move for knight
    public Vector<Pair<Integer, Integer>> possibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        Vector<Pair<Integer, Integer> > result = new Vector<>();

        for(int i=0; i < MAX_MOVES_COUNT; i++){
            if(inBounds(row + dr[i], col + dc[i]) && noAllies(row + dr[i], col+dc[i], state) &&
                    noCheckCaused(row,col,row+dr[i], col + dc[i], state,allieKingPos)){
                result.add(new Pair<Integer, Integer>(row+dr[i], col+dc[i]));
            }
        }

        return result;
    }

    @Override
    public Constants.pieceColor getColor() {
        return this.color;
    }

    @Override
    public boolean getHasMove() {
        return this.hasMoved;
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
    public Constants.pieceType getType() {
        return Constants.pieceType.Knight;
    }

    @Override
    public String toString() {
        String col = "White";
        if(this.color == Constants.pieceColor.black)
            col = "Black";

        return col + " Knight";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Knight newKnight = new Knight(this.getColor(), this.getHasMove());

        return newKnight;
    }
}
