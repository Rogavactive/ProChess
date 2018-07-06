package Tests.GameTests;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import Game.Model.Pieces.Rook;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {
    private static Rook white;
    private static Rook black;

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
    static void createRooks(){
        white = new Rook(Constants.pieceColor.white);
        black = new Rook(Constants.pieceColor.black);
    }

    @Test
    void testGetType(){
        assertEquals(Constants.pieceType.Rook, white.getType());
        assertEquals(Constants.pieceType.Rook, black.getType());
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
        black.hasMoved();
        assertEquals(true, black.getHasMoved());

        white.setHasMoved(false);
        assertEquals(false, white.getHasMoved());

        white.hasMoved();
        assertEquals(true, white.getHasMoved());
    }

    @Test
    void testToString(){
        assertEquals("Black Rook", black.toString());
        assertEquals("White Rook", white.toString());
    }

    @Test
    void testPossibleMoves1(){
        // board with just rook on coordinate 3,3 and king on 0,0
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(3).get(3).putPiece(white);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be straight lines
        // from 4,3 to 7,3; from 2,3 to 0,3; from 3,4 to 3,7; from 3,2 to 3,0
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        for(int i = 4; i <= 7; i++)
            actualResult.add(new Pair<>(i, 3));

        for(int i = 2; i >= 0; i--)
            actualResult.add(new Pair<>(i, 3));

        for(int i = 4; i <= 7; i++)
            actualResult.add(new Pair<>(3, i));

        for(int i = 2; i >= 0; i--)
            actualResult.add(new Pair<>(3, i));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(3, 3,
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
        // board with just rook on coordinate 0, 0
        // king at 0,3 and opponent's piece on 5,0
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(0).putPiece(white);
        state.get(5).get(0).putPiece(black);
        state.get(0).get(3).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be straight lines
        // from 0,1 to 0,2; from 1,0 to 5,0
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        for(int i = 1; i <= 5; i++)
            actualResult.add(new Pair<>(i, 0));

        for(int i = 1; i <= 2; i++)
            actualResult.add(new Pair<>(0, i));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 0,
                state, new Pair<>(0, 3));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves3(){
        // board with just bishop on coordinate 1,1
        // king at 0,1 and opponent's rook on 7,1
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(1).get(1).putPiece(white);
        state.get(7).get(1).putPiece(black);
        state.get(0).get(1).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be straight line from 2,1 to 7,1
        // but not from 1,2 to 1,7 and 1,0 because check is caused
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        for(int i = 2; i <= 7; i++)
            actualResult.add(new Pair<>(i, 1));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(1, 1,
                state, new Pair<>(0, 1));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }
}