package Game.Model;

import Game.Model.Pieces.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Board {
    private Vector<Vector<Cell>> board;
    private Pair<Integer, Integer> whiteKingPos;
    private Pair<Integer, Integer> blackKingPos;

    // Constructor
    public Board() {
        // Board is vector's vector of size NUMBER_OF_ROWS x NUMBER_OF_ROWS
        board = new Vector<>(Constants.NUMBER_OF_ROWS);
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            board.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                board.get(row).add(new Cell(row, col));
            }
        }

        // Placing pieces
        placePieces();

        whiteKingPos = new Pair<>(0, 4);
        blackKingPos = new Pair<>(7, 4);
    }

    // Constructor for tests
    public Board(HashMap<Piece, Pair<Integer, Integer>> state){
        board = new Vector<>(Constants.NUMBER_OF_ROWS);
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            board.add(new Vector<Cell>(Constants.NUMBER_OF_COLUMNS));
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                board.get(row).add(new Cell(row, col));
            }
        }

        // Placing every given piece on given place
        for (Piece p: state.keySet()) {
            board.get(state.get(p).getKey()).get(state.get(p).getValue()).putPiece(p);
        }
    }

    // Returns coordinates of given color's king
    public Pair<Integer,Integer> getKingPos(Constants.pieceColor color){
        if(color==Constants.pieceColor.white)
            return new Pair<>(whiteKingPos.getKey(),whiteKingPos.getValue());
        return new Pair<>(blackKingPos.getKey(),blackKingPos.getValue());
    }

    // This method sets board to it's starting state
    private void placePieces() {
        // Placing second line of white pieces
        board.get(0).get(0).putPiece(new Rook(Constants.pieceColor.white));
        board.get(0).get(1).putPiece(new Knight(Constants.pieceColor.white));
        board.get(0).get(2).putPiece(new Bishop(Constants.pieceColor.white));
        board.get(0).get(3).putPiece(new Queen(Constants.pieceColor.white));
        board.get(0).get(4).putPiece(new King(Constants.pieceColor.white));
        board.get(0).get(5).putPiece(new Bishop(Constants.pieceColor.white));
        board.get(0).get(6).putPiece(new Knight(Constants.pieceColor.white));
        board.get(0).get(7).putPiece(new Rook(Constants.pieceColor.white));

        // Placing pawns of both color
        for (int i = 0; i < Constants.NUMBER_OF_COLUMNS; i++) {
            board.get(1).get(i).putPiece(new Pawn(Constants.pieceColor.white));
            board.get(Constants.NUMBER_OF_ROWS - 2).get(i).putPiece(new Pawn(Constants.pieceColor.black));
        }

        // Placing second line of black pieces
        board.get(Constants.NUMBER_OF_ROWS - 1).get(0).putPiece(new Rook(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(1).putPiece(new Knight(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(2).putPiece(new Bishop(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(3).putPiece(new Queen(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(4).putPiece(new King(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(5).putPiece(new Bishop(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(6).putPiece(new Knight(Constants.pieceColor.black));
        board.get(Constants.NUMBER_OF_ROWS - 1).get(7).putPiece(new Rook(Constants.pieceColor.black));
    }

    // This method finds all possible moves
    // for all pieces of given color
    public ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > getAllPossibleMoves
    (Constants.pieceColor color) {
        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = new ConcurrentHashMap<>();
    try {
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                System.out.println(row+" "+col);
                Cell curCell = board.get(row).get(col);
                if (curCell.hasPiece() && curCell.getPieceColor() == color) {
                    if (color == Constants.pieceColor.white) {
                        result.put(new Pair<>(row, col), curCell.getMoves(this.getStateClone(), whiteKingPos));
                    } else {
                        result.put(new Pair<>(row, col), curCell.getMoves(this.getStateClone(), blackKingPos));
                    }
                } else {
                    result.put(new Pair<>(row, col), new Vector<Pair<Integer, Integer>>());
                }
            }
        }
    } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // Returns cell on given coordinates
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= Constants.NUMBER_OF_ROWS || col < 0 || col >= Constants.NUMBER_OF_COLUMNS)
            return null;

        return board.get(row).get(col);
    }

    // This method returns clone of board
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

    // This method makes move on board
    public void move(int srcRow, int srcCol, int dstRow, int dstCol) {
        if(whiteKingPos.getKey() == srcRow && whiteKingPos.getValue() == srcCol){
            whiteKingPos = new Pair<>(dstRow, dstCol);
        }else if(blackKingPos.getKey() == srcRow && blackKingPos.getValue() == srcCol){
            blackKingPos = new Pair<>(dstRow, dstCol);
        }

        board.get(srcRow).get(srcCol).getPiece().hasMoved();
        board.get(dstRow).get(dstCol).putPiece(board.get(srcRow).get(srcCol).getPiece());
        board.get(srcRow).get(srcCol).removePiece();
    }

    // This method adds given piece on given cell
    public void addPiece(int srcRow, int srcCol, Piece piece){
        board.get(srcRow).get(srcCol).putPiece(piece);
    }
}
