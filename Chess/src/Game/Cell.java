package Game;

import javafx.util.Pair;

import java.util.Vector;

public class Cell implements Cloneable{
    private int row;
    private int col;
    private Piece piece;

    public Cell(){

    }

    public Vector<Pair<Integer, Integer>> getMoves(Vector< Vector<Cell> > state){
        return new Vector< Pair<Integer, Integer> >();
    }

    public boolean getPieceColor(){
        return false;
    }
}
