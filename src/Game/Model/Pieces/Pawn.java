package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Pawn implements Piece {
    // Private variables
    private boolean color;
    private boolean hasMoved;

    // Constructor
    public Pawn(boolean color){
        this.color = color;
        hasMoved = false;
    }

    // This method checks if pawns move is valid
    // and if it is adds move into possible moves vector
    private void Step(int curRow, int curCol, Vector<Vector<Cell>> state,
                      Vector< Pair<Integer, Integer> > result, int step){
        // Checks if board has enough rows to make move
        if(curRow + step >= Constants.NUMBER_OF_ROWS)
            return;

        // Checks if cell is empty, to make move
        if(state.get(curRow + step).get(curCol) == null){
            result.add(new Pair<>(curRow + step, curCol));
        }
    }

    // This method checks if given cell has piece of opponent
    private boolean hasPieceToKill(Cell cellToKill){
        if(cellToKill.hasPiece() && cellToKill.getPieceColor() != this.getColor()){
            return true;
        }
        return false;
    }

    // This method checks if pawn can kill opponent's piece
    private void pawnCanKill(int curRow, int curCol, Vector<Vector<Cell>> state,
                             Vector< Pair<Integer, Integer> > result){
        // Checks if there is space in front of pawn
        if(curRow + 1 >= Constants.NUMBER_OF_ROWS)
            return;

        // Checks if pawn can make move to right
        if(curCol + 1 < Constants.NUMBER_OF_COLUMNS){
            Cell cellToKill = state.get(curRow + 1).get(curCol + 1);

            // if cellToKill contains opponent's piece, pawn can kill it
            if(hasPieceToKill(cellToKill))
                result.add(new Pair<>(curRow + 1, curCol + 1));
        }

        // Checks if pawn can move left
        if(curCol - 1 >= 0){
            Cell cellToKill = state.get(curRow + 1).get(curCol - 1);

            // if opponent's piece is there, pawn can kill it
            if(hasPieceToKill(cellToKill))
                result.add(new Pair<>(curRow + 1, curCol - 1));
        }
    }

    @Override
    // This method returns every possible move for pawn
    public Vector<Pair<Integer, Integer>> possibleMoves(int curRow, int curCol,
                                                        Vector<Vector<Cell>> state) {
        Vector< Pair<Integer, Integer> > result = new Vector<>();

        if(hasMoved){
            // if pawn has moved, it can only make one step forward
            Step(curRow, curCol, state, result, 1);
        }else{
            // if pawn hasn't moved, it can make either one or two steps forward
            Step(curRow, curCol, state, result, 1);
            // two step only can be made, when one step is possible
            if(result.size() != 0)
                Step(curRow, curCol, state, result, 2);
        }

        // pawn can kill opponent's pieces which are one diagonal step away from it
        pawnCanKill(curRow, curCol, state, result);

        return result;
    }

    // This method is called when pawn has made move
    public void moveMade(){
        hasMoved = true;
    }

    @Override
    // This method returns color of pawn
    public boolean getColor() {
        return color;
    }

    @Override
    // This method returns whether Pawn has already made a move
    public boolean getHasMove() {
        return hasMoved;
    }

    @Override
    // This method returns type of piece
    public pieceType getType() {
        return pieceType.Pawn;
    }
}
