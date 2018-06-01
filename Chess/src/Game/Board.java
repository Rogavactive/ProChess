package Game.Model;

import Game.Model.*;

import java.util.Vector;

public class Board {
    private Vector< Vector<Cell> > board;
    private Pair<Integer, Integer> whiteKing;
    private Pair<Integer, Integer> blackKing;
    private Cell selectedCell;
    private int CountOfWhiteFigures;
    private int CountOfBlackFigures;

    // Constructor
    public Board(){
        board = new Vector<>(Constants.NUMBER_OF_ROWS);

        for(int row = 0; row < Constants.NUMBER_OF_ROWS; row++){
            board.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
        }

    }

    // Checks if given cell of board is empty or not
    public boolean isFilled(int ind1, int ind2){

        return true;
    }

    public int getCountOfWhiteFigures(){
        return 0;
    }

    public int getCountOfBlackFigures(){
        return 0;
    }

    public void select(int x, int y){

    }
}
