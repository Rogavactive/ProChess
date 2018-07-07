package Tests.GameTests;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import Game.Model.Pieces.King;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    private static King white;
    private static King black;

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
        white = new King(Constants.pieceColor.white);
        black = new King(Constants.pieceColor.black);
    }

    @Test
    void testGetType(){
        assertEquals(Constants.pieceType.King, white.getType());
        assertEquals(Constants.pieceType.King, black.getType());
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
        assertEquals("Black King", black.toString());
        assertEquals("White King", white.toString());
    }

    @Test
    void testPossibleMoves1(){
        // board with king on coordinate 3,3
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(3).get(3).putPiece(white);

        // result should be all cells around 3,3
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        actualResult.add(new Pair<>(2,2));
        actualResult.add(new Pair<>(2,3));
        actualResult.add(new Pair<>(2,4));
        actualResult.add(new Pair<>(3,2));
        actualResult.add(new Pair<>(3,4));
        actualResult.add(new Pair<>(4,2));
        actualResult.add(new Pair<>(4,3));
        actualResult.add(new Pair<>(4,4));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(3, 3,
                state, new Pair<>(3, 3));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves2(){
        // board with white king on coordinate 0,3
        // white pawn on 1,3 and opponent's piece on 0,4
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(3).putPiece(white);
        state.get(0).get(4).putPiece(black);
        state.get(1).get(3).putPiece(Piece.createPiece(Constants.pieceType.Pawn,
                Constants.pieceColor.white));

        // result should be 0,2; 1,2; 0,4, 1,4
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        actualResult.add(new Pair<>(0,2));
        actualResult.add(new Pair<>(0,4));
        actualResult.add(new Pair<>(1,2));
        actualResult.add(new Pair<>(1,4));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 3,
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
        // board with king on coordinate 0,2
        // and opponent's rook on 1,7
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(2).putPiece(white);
        state.get(1).get(7).putPiece(Piece.createPiece(Constants.pieceType.Rook,
                Constants.pieceColor.black));

        // result should be 0,2; 0,3 otherwise check is caused
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        actualResult.add(new Pair<>(0,1));
        actualResult.add(new Pair<>(0,3));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 2,
                state, new Pair<>(0, 2));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testCastling1(){
        // board with king on coordinate 0,4
        // and rook on 0,7
        Vector<Vector<Cell>> state = createEmptyState();
        white.setHasMoved(false);
        state.get(0).get(4).putPiece(white);
        state.get(0).get(7).putPiece(Piece.createPiece(Constants.pieceType.Rook,
                Constants.pieceColor.white));

        // result should be 0,3;, 0,5; 1,3; 1,4; 1,5
        // castling also should be available on 0,6
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        actualResult.add(new Pair<>(0,3));
        actualResult.add(new Pair<>(0,5));
        actualResult.add(new Pair<>(1,3));
        actualResult.add(new Pair<>(1,4));
        actualResult.add(new Pair<>(1,5));
        actualResult.add(new Pair<>(0,6));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 4,
                state, new Pair<>(0, 4));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }

        // board with king on coordinate 0,4
        // and rook on 0,0
        state = createEmptyState();
        white.setHasMoved(false);
        state.get(0).get(4).putPiece(white);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.Rook,
                Constants.pieceColor.white));

        // result should be 0,3;, 0,5; 1,3; 1,4; 1,5
        // castling also should be available on 0,1
        actualResult = new Vector<>();

        actualResult.add(new Pair<>(0,3));
        actualResult.add(new Pair<>(0,5));
        actualResult.add(new Pair<>(1,3));
        actualResult.add(new Pair<>(1,4));
        actualResult.add(new Pair<>(1,5));
        actualResult.add(new Pair<>(0,1));

        result = white.possibleMoves(0, 4,
                state, new Pair<>(0, 4));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testCastling2(){
        // board with king on coordinate 0,4
        // and rook on 0,7
        Vector<Vector<Cell>> state = createEmptyState();
        white.setHasMoved(false);
        state.get(0).get(4).putPiece(white);
        state.get(0).get(7).putPiece(Piece.createPiece(Constants.pieceType.Rook,
                Constants.pieceColor.white));
        state.get(0).get(7).getPiece().setHasMoved(true);

        // result should be 0,3;, 0,5; 1,3; 1,4; 1,5
        // castling should be unavailable cause rook has done a move
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        actualResult.add(new Pair<>(0,3));
        actualResult.add(new Pair<>(0,5));
        actualResult.add(new Pair<>(1,3));
        actualResult.add(new Pair<>(1,4));
        actualResult.add(new Pair<>(1,5));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 4,
                state, new Pair<>(0, 4));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }
}