package Game.Model;

import Game.Model.Game;
import javafx.util.Pair;

import java.util.Vector;

public class Board {
    private Vector< Vector<Cell> > board;
    private Cell selectedCell;
    private int countOfWhiteFigures;
    private int countOfBlackFigures;

    // Constructor
    public Board(){
        board = new Vector<>(Constants.NUMBER_OF_ROWS);

        for(int row = 0; row < Constants.NUMBER_OF_ROWS; row++){
            board.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
        }

        selectedCell = null;
        countOfWhiteFigures = Constants.NUMBER_OF_PIECES;
        countOfBlackFigures = Constants.NUMBER_OF_PIECES;
    }

    // Checks if given cell of board is empty or not
    public boolean isFilled(int row, int col){
        if(row < 0 || row >= Constants.NUMBER_OF_ROWS || col < 0 || col >= Constants.NUMBER_OF_COLUMNS)
            return false;

        return board.get(row).get(col).hasPiece();
    }

    public int getCountOfWhiteFigures(){
        return countOfWhiteFigures;
    }

    public int getCountOfBlackFigures(){
        return countOfBlackFigures;
    }
}
