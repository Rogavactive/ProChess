package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Knight implements Piece {
    private boolean color;
    private boolean hasMove;
    private int[] dr,dc;
    private final int MAX_MOVES_COUNT = 8;
    public Knight(boolean color){
        this.color = color;
        this.hasMove = false;

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

    private boolean noCheckCaused(int row, int col, int newRow, int newCol,
                                  Vector<Vector<Cell>> state, Pair<Integer,Integer> allieKingPos) {
        boolean check = false;
        Piece killedPiece = state.get(newRow).get(newCol).getPiece();
        Piece curPiece = state.get(row).get(col).getPiece();
        state.get(row).get(col).removePiece();
        state.get(newRow).get(newCol).putPiece(curPiece);
        if(((King)state.get(allieKingPos.getKey()).get(allieKingPos.getValue()).getPiece()).checkForCheck(state, allieKingPos))
            check = true;
        state.get(row).get(col).putPiece(curPiece);
        state.get(newRow).get(newCol).putPiece(killedPiece);
        return check;
    }

    //returns if there is an allie piece standing on the given location
    private boolean noAllies(int r, int c, Vector<Vector<Cell> > state){
        if(state.get(r).get(c).hasPiece() && state.get(r).get(c).getPieceColor() != this.color) return true;
        return false;
    }

    //returns if the given location is on the board
    private boolean inBounds(int r, int c){
        if(r<0 || r>7) return false;
        if (c<0|| c>7) return false;
        return true;
    }
    @Override
    public boolean getColor() {
        return color;
    }

    @Override
    public boolean getHasMove() {
        return hasMove;
    }

    @Override
    public pieceType getType() {
        return pieceType.Knight;
    }
}
