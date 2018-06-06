package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Knight extends Piece {

    private int[] dr,dc;
    private final int MAX_MOVES_COUNT = 8;
    public Knight(boolean color){
        super(color);

        //initialise possible move arrays ( knight can move from (r,c) to (r+dr[i],c+dc[i]))
        this.dr = new int[] {2, 2, 1, 1, -1, -1, -2, -2};
        this.dc = new int[] {-1, 1, -2, 2, -2, 2, -1, 1};
    }

    @Override
    public Vector<Pair<Integer, Integer>> possibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        Vector<Pair<Integer, Integer> > result = new Vector<Pair<Integer, Integer> >();
        for(int i=0; i<MAX_MOVES_COUNT; i++){
            if(inBounds(row + dr[i], col + dc[i]) && noAllies(row + dr[i], col+dc[i], state) &&
                    noCheckCaused(row,col,row+dr[i], col + dc[i], state,allieKingPos)){
                result.add(new Pair<Integer, Integer>(row+dr[i], col+dc[i]));
            }
        }
        return result;
    }

    //returns if there is an allie piece standing on the given location
    private boolean noAllies(int r, int c, Vector<Vector<Cell> > state){
        if(state.get(r).get(c).hasPiece() && state.get(r).get(c).getPieceColor() != this.color) return true;
        return false;
    }

    //returns if the given location is on the board
    private boolean inBounds(int r, int c){
        if(r<0 || r> Constants.NUMBER_OF_ROWS) return false;
        if (c<0|| c> Constants.NUMBER_OF_COLUMNS) return false;
        return true;
    }

    @Override
    public pieceType getType() {
        return pieceType.Knight;
    }
}
