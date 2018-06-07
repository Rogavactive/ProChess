package Game.Model.Pieces;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Bishop extends Piece {
    private boolean color;
    private boolean hasMoved;

    public Bishop(boolean color){
        this.color = color;
        this.hasMoved = false;
    }

    public Bishop(boolean color, boolean hasMoved){
        this.color = color;
        this.hasMoved = hasMoved;
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

    @Override
    public boolean getColor() {
        return this.color;
    }

    @Override
    public boolean getHasMove() {
        return this.hasMoved;
    }

    private void findPossibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                   Vector<Pair<Integer, Integer>> result, Pair<Integer, Integer> allieKingPos, int dir1, int dir2){
        // making first step
        int curRow = row + dir1;
        int curCol = col + dir2;

        // checking if after this step bishop will stay in board
        // and if that cell will be empty
        while(curRow >= 0 && curRow < Constants.NUMBER_OF_ROWS
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
        if(curRow >= 0 && curRow < Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && state.get(curRow).get(curCol).hasPiece()
                && state.get(curRow).get(curCol).getPieceColor() != this.color
                && noCheckCaused(row,col,curRow,curCol,state,allieKingPos)) {
            result.add(new Pair<>(curCol, curRow));
        }
    }


    @Override
    public pieceType getType() {
        return pieceType.Bishop;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Bishop newBishop = new Bishop(this.getColor(), this.getHasMove());

        return newBishop;
    }
}
