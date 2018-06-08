package Game.Model;

import Game.Model.Game;
import Game.Model.Pieces.*;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Board {
    private Vector<Vector<Cell>> board;
    private int countOfWhiteFigures;
    private int countOfBlackFigures;
    private Pair<Integer, Integer> whiteKingPos;
    private Pair<Integer, Integer> blackKingPos;


    // Constructor
    public Board(boolean start) {
        // Board is vector's vector of size NUMBER_OF_ROWS x NUMBER_OF_ROWS
        board = new Vector<>(Constants.NUMBER_OF_ROWS);
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            board.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                board.get(row).add(new Cell(row, col));
            }
        }

        // If it's start of game, board is created in start state
        // else pieces aren't places
        if (start) {
            // Each player has NUMBER_OF_PIECES piece
            countOfWhiteFigures = Constants.NUMBER_OF_PIECES;
            countOfBlackFigures = Constants.NUMBER_OF_PIECES;

            // Placing pieces
            placePieces();
        }
    }

    // This method sets board to it's starting state
    private void placePieces() {
        // Placing second line of white pieces
        board.get(0).get(0).putPiece(new Rook(false));
        board.get(0).get(1).putPiece(new Knight(false));
        board.get(0).get(2).putPiece(new Bishop(false));
        board.get(0).get(3).putPiece(new Queen(false));
        board.get(0).get(4).putPiece(new King(false));
        board.get(0).get(5).putPiece(new Bishop(false));
        board.get(0).get(6).putPiece(new Knight(false));
        board.get(0).get(7).putPiece(new Rook(false));

        // Placing pawns of both color
        for (int i = 0; i < Constants.NUMBER_OF_COLUMNS; i++) {
            board.get(1).get(i).putPiece(new Pawn(false));
            board.get(Constants.NUMBER_OF_ROWS - 2).get(i).putPiece(new Pawn(true));
        }

        // Placing second line of black pieces
        board.get(Constants.NUMBER_OF_ROWS - 1).get(0).putPiece(new Rook(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(1).putPiece(new Knight(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(2).putPiece(new Bishop(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(3).putPiece(new Queen(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(4).putPiece(new King(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(5).putPiece(new Bishop(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(6).putPiece(new Knight(true));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(7).putPiece(new Rook(true));
    }

    public ConcurrentHashMap<Pair<Integer, Integer>, Vector<Pair<Integer, Integer>>> getAllPossibleMoves(boolean color) {
        ConcurrentHashMap<Pair<Integer, Integer>, Vector<Pair<Integer, Integer>>> result = new ConcurrentHashMap<>();

        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                Cell curCell = board.get(row).get(col);
                if (curCell.hasPiece() && curCell.getPieceColor() == color) {
                    if (color) {
                        result.put(new Pair<>(row, col), curCell.getMoves(this.getStateClone(), whiteKingPos));
                    } else {
                        result.put(new Pair<>(row, col), curCell.getMoves(this.getStateClone(), blackKingPos));
                    }
                } else {
                    result.put(new Pair<>(row, col), new Vector<>());
                }

            }
        }

        return result;
    }

    // Checks if given cell of board is empty or not
    public boolean isFilled(int row, int col) {
        if (row < 0 || row >= Constants.NUMBER_OF_ROWS || col < 0 || col >= Constants.NUMBER_OF_COLUMNS)
            return false;

        return board.get(row).get(col).hasPiece();
    }

    // This method is called when pieces is dead
    public void pieceDied(boolean color) {
        if (color) {
            countOfBlackFigures--;
        } else {
            countOfWhiteFigures--;
        }
    }

    public Cell getCell(int row, int col) {
        return new Cell(board.get(row).get(col));
    }

    public Vector<Pair<Integer, Integer>> getMoves(int row, int col, boolean color) {
        if (color) {
            return board.get(row).get(col).getMoves(getStateClone(), blackKingPos);
        }
        return board.get(row).get(col).getMoves(getStateClone(), whiteKingPos);
    }

    private Vector<Vector<Cell>> getStateClone() {
        Vector<Vector<Cell>> clone = new Vector<>();
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            clone.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                clone.get(row).add(new Cell(board.get(row).get(col)));
            }
        }
        return clone;
    }

    public void move(int srcRow, int srcCol, int dstRow, int dstCol) {
        board.get(srcRow).get(srcCol).getPiece().hasMoved();
        if (board.get(dstRow).get(dstCol).hasPiece())
            pieceDied(board.get(dstRow).get(dstCol).getPieceColor());
        board.get(dstRow).get(dstCol).putPiece(board.get(srcRow).get(srcCol).getPiece());
        board.get(srcRow).get(srcCol).removePiece();
    }
}
