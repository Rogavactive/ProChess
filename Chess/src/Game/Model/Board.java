package Game.Model;

import Game.Model.Game;
import javafx.util.Pair;

import java.util.Vector;

public class Board {
    private static final int NUMBER_OF_ROWS = 8;
    private static final int NUMBER_OF_COLUMNS = 8;
    private Vector< Vector<Cell> > board;
    private Pair<Integer, Integer> whiteKing;
    private Pair<Integer, Integer> blackKing;
    private Cell selectedCell;
    private int CountOfWhiteFigures;
    private int CountOfBlackFigures;

    // Constructor
    public Board(){
        board = new Vector<>(NUMBER_OF_ROWS);

        for(int row = 0; row < NUMBER_OF_ROWS; row++){
            board.add(new Vector<Cell>(NUMBER_OF_COLUMNS));
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
