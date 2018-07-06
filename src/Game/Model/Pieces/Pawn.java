package Game.Model.Pieces;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.Vector;

public class Pawn extends Piece {
    private final Constants.pieceColor color;
    private boolean hasMoved;

    // Constructor
    public Pawn(Constants.pieceColor color){
        this.color = color;
        this.hasMoved =false;
    }

    public Pawn(Constants.pieceColor color, boolean hasMoved){
        this.color = color;
        this.hasMoved = hasMoved;
    }

    // This method checks if pawns move is valid
    // and if it is adds move into possible moves vector
    private boolean Step(int curRow, int curCol, Vector<Vector<Cell>> state,
                      Vector< Pair<Integer, Integer> > result, int step,
                      Constants.pieceColor color, Pair<Integer,Integer> allieKingPos){
        // If pawn is white, it moves throw positive direction on board
        if(color == Constants.pieceColor.white){
            // Checks if board has enough rows to make move
            if(curRow + step >= Constants.NUMBER_OF_ROWS)
                return false;

            // Checks if cell is empty, to make move
            if( !(state.get(curRow + step).get(curCol).hasPiece()) ) {
                if(noCheckCaused(curRow, curCol, curRow + step, curCol,
                        state, allieKingPos)){
                    result.add(new Pair<>(curRow + step, curCol));
                }
                return true;
            }else
                return false;
        }else{
            // If pawn is black, it moves throw negative direction

            // Checks if board has enough rows to make move
            if(curRow - step < 0)
                return false;

            // Checks if cell is empty, to make move
            if( !(state.get(curRow - step).get(curCol).hasPiece()) ) {
                if(noCheckCaused(curRow, curCol, curRow - step, curCol,
                        state, allieKingPos) ){
                    result.add(new Pair<>(curRow - step, curCol));
                }
                return true;
            }else
                return false;
        }
    }

    // This method checks if given cell has piece of opponent
    private boolean hasPieceToKill(Cell cellToKill){
        if(cellToKill.hasPiece() && cellToKill.getPieceColor() != this.getColor())
            return true;

        return false;
    }

    // This method checks if there is opponent's piece
    // which can be killed by pawn
    private void checkKill(int curRow, int curCol, int step,
                           Vector<Vector<Cell>> state,
                           Vector< Pair<Integer, Integer> > result,
                           Pair<Integer,Integer> allieKingPos){
        // Checks if pawn can make move to right
        if(curCol + 1 < Constants.NUMBER_OF_COLUMNS){
            Cell cellToKill = state.get(curRow + step).get(curCol + 1);

            // if cellToKill contains opponent's piece, pawn can kill it
            if(hasPieceToKill(cellToKill) &&
                    noCheckCaused(curRow, curCol, curRow + step, curCol + 1,
                            state, allieKingPos))
                result.add(new Pair<>(curRow + step, curCol + 1));
        }

        // Checks if pawn can move left
        if(curCol - 1 >= 0){
            Cell cellToKill = state.get(curRow + step).get(curCol - 1);

            // if opponent's piece is there, pawn can kill it
            if(hasPieceToKill(cellToKill) &&
                    noCheckCaused(curRow, curCol, curRow + step, curCol - 1,
                            state, allieKingPos))
                result.add(new Pair<>(curRow + step, curCol - 1));
        }
    }

    // This method checks if pawn can kill opponent's piece
    private void pawnCanKill(int curRow, int curCol, Vector<Vector<Cell>> state,
                             Vector< Pair<Integer, Integer> > result,
                             Constants.pieceColor color, Pair<Integer,Integer> allieKingPos){

        if(color == Constants.pieceColor.white){
            // Checks if there is space in front of pawn
            if(curRow + 1 >= Constants.NUMBER_OF_ROWS)
                return;

            checkKill(curRow, curCol, 1, state, result, allieKingPos);
        }else{
            // Checks if there is space in front of pawn
            if(curRow - 1 < 0)
                return;

            checkKill(curRow, curCol, -1, state, result, allieKingPos);
        }
    }

    @Override
    // Called when pawn has done a move
    public void hasMoved() {
        this.hasMoved = true;
    }

    @Override
    // This method returns every possible move for pawn
    public Vector<Pair<Integer, Integer>> possibleMoves(int curRow, int curCol,
                                                        Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        Vector< Pair<Integer, Integer> > result = new Vector<>();

        if(hasMoved){
            // if pawn has moved, it can only make one step forward
            Step(curRow, curCol, state, result, 1, this.color, allieKingPos);
        }else{
            // if pawn hasn't moved, it can make either one or two steps forward
            boolean canMakeSecondStep = Step(curRow, curCol, state, result, 1, this.color, allieKingPos);
            // two step only can be made, when one step is possible
            if(canMakeSecondStep)
                Step(curRow, curCol, state, result, 2, this.color, allieKingPos);
        }

        // pawn can kill opponent's pieces which are one diagonal step away from it
        pawnCanKill(curRow, curCol, state, result, this.color, allieKingPos);

        return result;
    }

    @Override
    // This method returns color of pawn
    public Constants.pieceColor getColor() {
        return this.color;
    }

    @Override
    public boolean getHasMoved() {
        return this.hasMoved;
    }

    @Override
    public void setHasMoved(boolean b){
        this.hasMoved = b;
    }

    @Override
    // This method returns type of piece
    public Constants.pieceType getType() {
        return Constants.pieceType.Pawn;
    }

    @Override
    public String toString() {
        String col = "White";
        if(this.color == Constants.pieceColor.black)
            col = "Black";

        return col + " Pawn";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Pawn newPawn = new Pawn(this.getColor(), this.getHasMoved());

        return newPawn;
    }
}
