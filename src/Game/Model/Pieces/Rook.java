package Game.Model.Pieces;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Rook extends Piece {
    private boolean color;
    private boolean hasMoved;

    // Constructor
    public Rook(boolean color){
        this.color = color;
        this.hasMoved = false;
    }

    // This method checks if after given move piece stays in board,
    // if cell is empty and if move doesn't cause check
    private boolean validMove(int curRow, int curCol, Vector<Vector<Cell>> state){
        if(curRow >= 0 && curRow < Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && !(state.get(curRow).get(curCol).hasPiece()) ){
            return true;
        }
        return false;
    }

    // This method finds all possible moves for rook
    private void findPossibleMoves(int row, int col, Vector<Vector<Cell>> state,
                                   Vector<Pair<Integer, Integer>> result, Pair<Integer, Integer> allieKingPos, int dir1, int dir2){
        // making first step
        int curRow = row + dir1;
        int curCol = col + dir2;

        // checking if after this step rook will stay in board
        // and if that cell will be empty
        while( validMove(curRow, curCol, state) ){
            // Checking if check is caused
            if(noCheckCaused(row, col, curRow, curCol, state, allieKingPos))
                result.add(new Pair<>(curRow, curCol));

            // make another step
            curRow += dir1;
            curCol += dir2;
        }

        // Cheking if rook can kill opponent's piece
        if( canKill(row, col, curRow, curCol, state, allieKingPos) )
            result.add(new Pair<>(curRow, curCol));

        // if rook has not done a move yet
        // castling is possible
        if(!hasMoved){
            // Checking whether piece which first
            // meets rook is king of same color
            while( validMove(curRow, curCol, state) ){
                result.add(new Pair<>(curRow, curCol));
            }
        }
    }

    // This method checks if rook can kill
    // opponent's piece on given curRow and curCol
    private boolean canKill(int row, int col, int curRow, int curCol,
                            Vector<Vector<Cell>> state, Pair<Integer,Integer> allieKingPos) {
        if(curRow >= 0 && curRow < Constants.NUMBER_OF_ROWS
                && curCol >= 0 && curCol < Constants.NUMBER_OF_COLUMNS
                && state.get(curRow).get(curCol).hasPiece()
                && state.get(curRow).get(curCol).getPieceColor() != this.getColor()
                && noCheckCaused(row, col, curRow, curCol, state, allieKingPos)){
            return true;
        }
        return false;
    }

    @Override
    // This method returns every possible move for rook on given coordinate
    public Vector<Pair<Integer, Integer>> possibleMoves(int row, int col,
                                                        Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        Vector< Pair<Integer, Integer> > result = new Vector<>();

        // finding all moves on all directions
        findPossibleMoves(row, col, state, result, allieKingPos, 1, 0);
        findPossibleMoves(row, col, state, result, allieKingPos, -1, 0);
        findPossibleMoves(row, col, state, result, allieKingPos, 0, 1);
        findPossibleMoves(row, col, state, result, allieKingPos, 0, -1);

        return result;
    }

    @Override
    // This method returns color of rook
    public boolean getColor() {
        return this.color;
    }

    @Override
    // called when move has been done
    public boolean getHasMove() {
        return this.hasMoved;
    }

    @Override
    // This method returns type of piece
    public pieceType getType() {
        return pieceType.Rook;
    }
}
