package Game.Model;

import Game.Model.Game;
import Game.Model.Pieces.*;
import javafx.util.Pair;

import java.util.Vector;

public class Board implements Cloneable {
    private Vector< Vector<Cell> > board;
    private int countOfWhiteFigures;
    private int countOfBlackFigures;

    // Constructor
    public Board(boolean start){
        // Board is vector's vector of size NUMBER_OF_ROWS x NUMBER_OF_ROWS
        board = new Vector<>(Constants.NUMBER_OF_ROWS);
        for(int row = 0; row < Constants.NUMBER_OF_ROWS; row++){
            board.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
            for(int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++){
                board.get(row).add( new Cell(row, col) );
            }
        }

        // If it's start of game, board is created in start state
        // else pieces aren't places
        if(start){
            // Each player has NUMBER_OF_PIECES piece
            countOfWhiteFigures = Constants.NUMBER_OF_PIECES;
            countOfBlackFigures = Constants.NUMBER_OF_PIECES;

            // Placing pieces
            placePieces();
        }
    }

    // This method sets board to it's starting state
    private void placePieces(){
        // Placing second line of white pieces
        board.get(0).get(0).putPiece(new Rook(false));
        board.get(0).get(1).putPiece(new Knight(false));
        board.get(0).get(2).putPiece(new Bishop(false));
        board.get(0).get(3).putPiece(new Queen(false));
        board.get(0).get(4).putPiece(new King(false));
        board.get(0).get(5).putPiece(new Bishop(false));
        board.get(0).get(6).putPiece(new Knight(false));
        board.get(0).get(7).putPiece(new Rook(false));

        // Placing pawns of both color
        for(int i = 0; i < Constants.NUMBER_OF_COLUMNS; i++){
            board.get(1).get(i).putPiece(new Pawn(false));
            board.get(Constants.NUMBER_OF_ROWS - 2).get(i).putPiece(new Pawn(true));
        }

        // Placing second line of black pieces
        board.get(Constants.NUMBER_OF_ROWS - 1).get(0).putPiece(new Rook(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(1).putPiece(new Knight(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(2).putPiece(new Bishop(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(3).putPiece(new Queen(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(4).putPiece(new King(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(5).putPiece(new Bishop(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(6).putPiece(new Knight(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(7).putPiece(new Rook(true));
    }

    // Checks if given cell of board is empty or not
    public boolean isFilled(int row, int col){
        if(row < 0 || row >= Constants.NUMBER_OF_ROWS || col < 0 || col >= Constants.NUMBER_OF_COLUMNS)
            return false;

        return board.get(row).get(col).hasPiece();
    }

    // This method is called when pieces is dead
    public void PieceDied(boolean color){
        if(color){
            countOfBlackFigures--;
        }else{
            countOfWhiteFigures--;
        }
    }

    // This method returns count of white pieces alive
    public int getCountOfWhiteFigures(){
        return countOfWhiteFigures;
    }

    // This method returns count of black pieces alive
    public int getCountOfBlackFigures(){
        return countOfBlackFigures;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Board newBoard = new Board(false);

        for(int row = 0; row < Constants.NUMBER_OF_ROWS; row++){
            for(int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++){
                board.get(row).get(col).putPiece(board.get(row).get(col).getPiece());
            }
        }

        return newBoard;
    }
}
