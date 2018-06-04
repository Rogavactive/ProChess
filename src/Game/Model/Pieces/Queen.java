package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Queen implements Piece {
    // Private variable
    private boolean color;

    // Constructor
    public Queen(boolean color){
        this.color = color;
    }

    // This method finds all possible moves
    // for given coordinates and given direction
    private void findPossibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                   Vector< Pair<Integer, Integer> > result, int dir1, int dir2){
        // making first step
        int curRow = row + dir1;
        int curCol = col + dir2;

        // checking if after this step queen will stay in board
        // and if that cell will be empty
        while(curRow >= 0 && curRow <= Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && !(state.get(curRow).get(curCol).hasPiece()) ){
            result.add(new Pair<>(curRow, curCol));

            // make another step
            curRow += dir1;
            curCol += dir2;
        }

        // Checking if after last step, queen is in board
        // and given cell has oponent's cell, so queen can kill it
        if(curRow >= 0 && curRow <= Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && state.get(curRow).get(curCol).hasPiece()
                && state.get(curRow).get(curCol).getPieceColor() != this.getColor() ){
            result.add(new Pair<>(curCol, curRow));
        }
    }

    @Override
    // This method returns all possible cells, where queen can make move
    public Vector< Pair<Integer, Integer> > possibleMoves(int row, int col,
                                                          Vector<Vector<Cell>> state) {
        Vector< Pair<Integer, Integer> > result = new Vector<>();

        // trying all directions, to find moves
        findPossibleMoves(row, col, state, result, 0, 1);
        findPossibleMoves(row, col, state, result, 0, -1);
        findPossibleMoves(row, col, state, result, 1, 0);
        findPossibleMoves(row, col, state, result, -1, 0);
        findPossibleMoves(row, col, state, result, 1, 1);
        findPossibleMoves(row, col, state, result, 1, -1);
        findPossibleMoves(row, col, state, result, -1, 1);
        findPossibleMoves(row, col, state, result, -1, -1);

        return result;
    }

    @Override
    // This method returns color of given queen
    public boolean getColor() {
        return color;
    }

    @Override
    // This method returns whether Pawn has already made a move
    public boolean getHasMove() {
        return true;
    }

    @Override
    // This method returns type of piece
    public pieceType getType() {
        return pieceType.Queen;
    }
}
