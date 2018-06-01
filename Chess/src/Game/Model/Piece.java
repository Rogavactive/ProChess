package Game.Model;


import Game.Model.Pieces.Types;

import java.util.Vector;

public interface Piece {

     Vector< Pair<Integer, Integer> > possibleMoves(int ind1, int ind2, Vector< Vector<Cell> > state);
     boolean getColor();
     Types getType();
}
