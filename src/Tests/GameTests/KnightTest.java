package Tests.GameTests;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import Game.Model.Pieces.Knight;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {
    private static Knight white;
    private static Knight black;

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
        white = new Knight(Constants.pieceColor.white);
        black = new Knight(Constants.pieceColor.black);
    }

    @Test
    void testGetType(){
        assertEquals(Constants.pieceType.Knight, white.getType());
        assertEquals(Constants.pieceType.Knight, black.getType());
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
        assertEquals("Black Knight", black.toString());
        assertEquals("White Knight", white.toString());
    }

    @Test
    void testPossibleMoves1(){
        // board with just knight on coordinate 0,3 and king on 0,0
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(3).putPiece(white);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 2,2; 2,4; 1,1; 1,5;
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(2, 2));
        actualResult.add(new Pair<>(2, 4));
        actualResult.add(new Pair<>(1, 1));
        actualResult.add(new Pair<>(1, 5));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 3,
                state, new Pair<>(0, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves2(){
        // board with knight on coordinate 0, 0
        // king at 1,2 and opponent's piece on 2,1
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(0).putPiece(white);
        state.get(2).get(1).putPiece(black);
        state.get(1).get(2).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 2,1; because 1,2 is filled by king
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(2, 1));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 0,
                state, new Pair<>(1, 2));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves3(){
        // board with knight on coordinate 0,3
        // king at 1,0 and opponent's rook on 1,5
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(3).putPiece(white);
        state.get(1).get(5).putPiece(Piece.createPiece(Constants.pieceType.Rook,
                Constants.pieceColor.black));
        state.get(1).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 1,1 and 1,5, otherwise check is caused
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(1, 1));
        actualResult.add(new Pair<>(1, 5));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 3,
                state, new Pair<>(1, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }
}