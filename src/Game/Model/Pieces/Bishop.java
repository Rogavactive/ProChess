package Game.Model.Pieces;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Bishop extends Piece {

    private final int MAX_MOVE_LENGTH=7;

    public Bishop(boolean color){
        super(color);
    }

    @Override
    public Vector<Pair<Integer, Integer>> possibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        Vector<Pair<Integer, Integer> > result = new Vector<Pair<Integer, Integer>>();

        findPossibleMoves(row, col, state, result, allieKingPos, 1, 1);
        findPossibleMoves(row, col, state, result, allieKingPos, -1, 1);
        findPossibleMoves(row, col, state, result, allieKingPos, 1, -1);
        findPossibleMoves(row, col, state, result, allieKingPos, -1, -1);

        return result;
    }

    private void findPossibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                   Vector<Pair<Integer, Integer>> result, Pair<Integer, Integer> allieKingPos, int dir1, int dir2){
        // making first step
        int curRow = row + dir1;
        int curCol = col + dir2;

        // checking if after this step bishop will stay in board
        // and if that cell will be empty
        while(curRow >= 0 && curRow <= Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && !(state.get(curRow).get(curCol).hasPiece()) ){

            if(noCheckCaused(row,col,curRow,curCol,state,allieKingPos))
                result.add(new Pair<>(curRow, curCol));
            // make another step
            curRow += dir1;
            curCol += dir2;
        }

        // Checking if after last step, bishop is in board
        // and given cell has oponent's cell, so queen can kill it
        if(curRow >= 0 && curRow <= Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && state.get(curRow).get(curCol).hasPiece()
                && state.get(curRow).get(curCol).getPieceColor() != this.color
                && noCheckCaused(row,col,curRow,curCol,state,allieKingPos)) {
            result.add(new Pair<>(curCol, curRow));
        }
    }

    private boolean checkIfValid(int row, int col, int newRow, int newCol,
                                 Vector<Vector<Cell>> state, Pair<Integer,Integer> allieKingPos) {
        if(!inbounds(newRow,newCol)) return false;
        if(!noAllies(newRow,newCol, state)) return false;
        if(!noCheckCaused(row,col,newRow,newCol,state,allieKingPos)) return false;
        return true;
    }

    private boolean noAllies(int newRow, int newCol, Vector<Vector<Cell>> state) {
        if(state.get(newRow).get(newCol).getPieceColor()!=this.color)
            return true;
        return false;
    }

    private boolean inbounds(int newRow, int newCol) {
        if(newRow<0 || newRow>7) return false;
        if (newCol<0|| newCol>7) return false;
        return true;
    }

    @Override
    public pieceType getType() {
        return pieceType.Bishop;
    }
}
