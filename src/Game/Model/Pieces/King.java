package Game.Model.Pieces;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Piece;
import javafx.util.Pair;
 
import java.util.Vector;

public class King extends Piece {
    public King(boolean color){
        super(color);
    }

    @Override
    public Vector<Pair<Integer, Integer>> possibleMoves(int ind1, int ind2, Vector<Vector<Cell>> state,
                                                        Pair<Integer,Integer> allieKingPos) {
        return null;
    }

    public boolean checkForCheck( Vector<Vector<Cell>> state,
                                  Pair<Integer,Integer> pos){
        return false;
    }

    @Override
    public pieceType getType() {
        return pieceType.King;
    }
}
