package Tests.GameTests;

import Game.Model.Board;
import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import Game.Model.Pieces.Bishop;
import com.sun.corba.se.spi.ior.ObjectKey;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    private static Bishop white;
    private static Bishop black;

    // method which creates new empty cell
    private Vector<Vector<Cell>> createEmptyState() {
        Vector<Vector<Cell>> state = new Vector<>();

        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            state.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                state.get(row).add(new Cell(row, col));
            }
        }

        return state;
    }

    @BeforeAll
    static void createBishops(){
        white = new Bishop(Constants.pieceColor.white);
        black = new Bishop(Constants.pieceColor.black);
    }

    @Test
    void testGetType(){
        assertEquals(Constants.pieceType.Bishop, white.getType());
        assertEquals(Constants.pieceType.Bishop, black.getType());
    }

    @Test
    void testGetColor(){
        assertEquals(Constants.pieceColor.white, white.getColor());
        assertEquals(Constants.pieceColor.black, black.getColor());
    }

    @Test
    void testHasMoved(){
        assertEquals(false, white.getHasMoved());
        white.setHasMoved(true);
        assertEquals(true, white.getHasMoved());

        assertEquals(false, black.getHasMoved());
        black.setHasMoved(true);
        assertEquals(true, black.getHasMoved());

        white.setHasMoved(false);
        assertEquals(false, white.getHasMoved());

        white.hasMoved();
        assertEquals(true, white.getHasMoved());
    }

    @Test
    void testToString(){
        assertEquals("Black Bishop", black.toString());
        assertEquals("White Bishop", white.toString());
    }

    @Test
    void testPossibleMoves1(){
        // board with just bishop on coordinate 3,3 and king on 0,2
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(3).get(3).putPiece(white);
        state.get(0).get(2).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be vertical lines
        // from 4,4 to 7,7; from 2,4 to 0,6; from 4,2 to 6,0; 2,2 to 0,0
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        for(int i = 4; i <= 7; i++)
            actualResult.add(new Pair<>(i, i));

        actualResult.add(new Pair<>(2, 4));
        actualResult.add(new Pair<>(1, 5));
        actualResult.add(new Pair<>(0, 6));

        actualResult.add(new Pair<>(4, 2));
        actualResult.add(new Pair<>(5, 1));
        actualResult.add(new Pair<>(6, 0));

        for(int i = 2; i >= 0; i--)
            actualResult.add(new Pair<>(i, i));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(3, 3,
                state, new Pair<>(0, 2));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves2(){
        // board with just bishop on coordinate 0, 0
        // king at 0,7 and opponent's piece on 4,4
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(0).putPiece(white);
        state.get(4).get(4).putPiece(black);
        state.get(0).get(7).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be vertical line from 1,1 to 4,4
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        for(int i = 1; i <= 4; i++)
            actualResult.add(new Pair<>(i, i));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 0,
                state, new Pair<>(0, 7));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves3(){
        // board with just bishop on coordinate 6,6
        // king at 3,3 and opponent's bishop on 7,7
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(6).get(6).putPiece(white);
        state.get(7).get(7).putPiece(black);
        state.get(3).get(3).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 7,7; 5,5; 4,4
        // but not 7,5 and 5,7 because check is caused
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(7, 7));
        actualResult.add(new Pair<>(5, 5));
        actualResult.add(new Pair<>(4, 4));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(6, 6,
                state, new Pair<>(3, 3));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }
}