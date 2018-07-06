package Tests.GameTests;

import Game.Model.Cell;
import Game.Model.Constants;
import Game.Model.Piece;
import Game.Model.Pieces.Pawn;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    private static Pawn white;
    private static Pawn black;

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
    static void createPawns(){
        white = new Pawn(Constants.pieceColor.white);
        black = new Pawn(Constants.pieceColor.black);
    }

    @Test
    void testGetType(){
        assertEquals(Constants.pieceType.Pawn, white.getType());
        assertEquals(Constants.pieceType.Pawn, black.getType());
    }

    @Test
    void testGetColor(){
        assertEquals(Constants.pieceColor.white, white.getColor());
        assertEquals(Constants.pieceColor.black, black.getColor());
    }

    @Test
    void testHasMoved(){
        white.setHasMoved(false);
        black.setHasMoved(false);

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
        assertEquals("Black Pawn", black.toString());
        assertEquals("White Pawn", white.toString());
    }

    @Test
    void testPossibleMovesForWhitePawn1(){
        // board with just pawn on coordinate 1,1 and king on 0,0
        Vector<Vector<Cell>> state = createEmptyState();
        white.setHasMoved(false);
        state.get(1).get(1).putPiece(white);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // because pawn hasn't done a move
        // it can make two steps 2,1 and 3,1
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(2,1));
        actualResult.add(new Pair<>(3,1));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(1, 1,
                state, new Pair<>(0, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }

        // Check when pawn has made a move
        white.setHasMoved(true);
        state = createEmptyState();
        state.get(3).get(1).putPiece(white);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // Pawn can make only one move
        actualResult = new Vector<>();
        actualResult.add(new Pair<>(4,1));

        result = white.possibleMoves(3, 1, state, new Pair<>(0, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMovesForBlackPawn1(){
        // board with just pawn on coordinate 6,1 and king on 7,0
        Vector<Vector<Cell>> state = createEmptyState();
        black.setHasMoved(false);
        state.get(6).get(1).putPiece(black);
        state.get(7).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.black));

        // because pawn hasn't done a move
        // it can make two steps 5,1 and 4,1
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(5,1));
        actualResult.add(new Pair<>(4,1));

        Vector<Pair<Integer, Integer>> result = black.possibleMoves(6, 1,
                state, new Pair<>(7, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }

        // Check when pawn has made a move
        black.setHasMoved(true);
        state = createEmptyState();
        state.get(6).get(1).putPiece(white);
        state.get(7).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.black));

        // Pawn can make only one move
        actualResult = new Vector<>();
        actualResult.add(new Pair<>(5,1));

        result = black.possibleMoves(6, 1, state, new Pair<>(7, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMovesForWhitePawn2(){
        // board with just pawn on coordinate 1, 1
        // king at 0,0 and opponent's piece on 2,0
        Vector<Vector<Cell>> state = createEmptyState();
        white.setHasMoved(false);
        state.get(1).get(1).putPiece(white);
        state.get(2).get(0).putPiece(black);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 2,1; 3,1; 2,0
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(2,1));
        actualResult.add(new Pair<>(3,1));
        actualResult.add(new Pair<>(2,0));


        Vector<Pair<Integer, Integer>> result = white.possibleMoves(1, 1,
                state, new Pair<>(0, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMovesForBlackPawn2(){
        // board with just pawn on coordinate 6, 1
        // king at 7,0 and opponent's piece on 5,0
        Vector<Vector<Cell>> state = createEmptyState();
        black.setHasMoved(true);
        state.get(6).get(1).putPiece(black);
        state.get(5).get(0).putPiece(white);
        state.get(7).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.black));

        // result should be 5,1; 5,0
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(5,1));
        actualResult.add(new Pair<>(5,0));


        Vector<Pair<Integer, Integer>> result = black.possibleMoves(6, 1,
                state, new Pair<>(7, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMovesForWhitePawn3(){
        // board with just pawn on coordinate 1,1
        // king at 1,0 and opponent's bishop on 2,1
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(1).get(1).putPiece(white);
        state.get(2).get(1).putPiece(black);
        state.get(1).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be empty, because check is caused
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(1, 1,
                state, new Pair<>(1, 0));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    void testPossibleMovesForBlackPawn3(){
        // board with just pawn on coordinate 6,1
        // king at 6,2 and opponent's bishop on 5,1
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(6).get(1).putPiece(black);
        state.get(5).get(1).putPiece(white);
        state.get(6).get(2).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.black));

        // result should be empty, because check is caused
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();

        Vector<Pair<Integer, Integer>> result = black.possibleMoves(6, 1,
                state, new Pair<>(6, 2));

        // Checking that result is correct
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves4(){
        // board with just pawn on coordinate 0,2
        // king at 0,0 and opponent's piece on 1,1
        Vector<Vector<Cell>> state = createEmptyState();
        state.get(0).get(2).putPiece(white);
        state.get(1).get(1).putPiece(black);
        state.get(0).get(0).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 1,1, check is caused but white pawn can solve it
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(1, 1));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(0, 2,
                state, new Pair<>(0, 0));

        // Checking that result is correct
        System.out.println(result.size());
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }

    @Test
    void testPossibleMoves5(){
        // board with just pawn on coordinate 1,3
        // king at 2,2 and opponent's bishop on 4,4
        Vector<Vector<Cell>> state = createEmptyState();
        white.setHasMoved(false);
        state.get(1).get(3).putPiece(white);
        state.get(4).get(4).putPiece(Piece.createPiece(Constants.pieceType.Bishop,
                Constants.pieceColor.black));
        state.get(2).get(2).putPiece(Piece.createPiece(Constants.pieceType.King,
                Constants.pieceColor.white));

        // result should be 3,3 when pawn saves king
        // otherwise it's check
        Vector<Pair<Integer, Integer>> actualResult = new Vector<>();
        actualResult.add(new Pair<>(3, 3));

        Vector<Pair<Integer, Integer>> result = white.possibleMoves(1, 3,
                state, new Pair<>(2, 2));

        // Checking that result is correct
        System.out.println(result.size());
        assertEquals(actualResult.size(), result.size());
        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getKey(), actualResult.get(i).getKey());
            assertEquals(result.get(i).getValue(), actualResult.get(i).getValue());
        }
    }
}