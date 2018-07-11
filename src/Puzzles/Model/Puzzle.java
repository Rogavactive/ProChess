package Puzzles.Model;

import Game.Model.Board;
import Game.Model.Constants;
import Game.Model.Move;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Puzzle {
    private Board board;
    private Vector<Pair<Integer, Integer>> computerMoves;
    private int currentComputerMove;
    private Vector<Pair<Integer, Integer>> correctMoves;
    private int currentUserMove;

    public Puzzle(String startingState, String comMoves, String corMoves){
        board = new Board(startingState);
        computerMoves = stringToVector(comMoves);
        currentComputerMove = 0;
        correctMoves = stringToVector(corMoves);
        currentUserMove = 0;
    }

    public boolean pieceMoved(int srcRow, int srcCol, int dstRow, int dstCol){
        Pair< Pair<Integer, Integer>, Pair<Integer, Integer> > correctMove = getCorrectMove();

        board.move(srcRow, srcCol, dstRow, dstCol);

        if(correctMove.getKey().getKey() == srcRow && correctMove.getKey().getValue() == srcCol &&
                correctMove.getValue().getKey() == dstRow && correctMove.getValue().getValue() == dstCol)
            return true;

        return false;
    }

    public String doComputerMove(){
        Pair< Pair<Integer, Integer>, Pair<Integer, Integer> > move = getComputersMove();

        if(move == null)
            return "success";

        board.move(move.getKey().getKey(), move.getKey().getValue(), move.getValue().getKey(), move.getValue().getValue());

        return getBoardState();
    }

    private Pair< Pair<Integer, Integer>, Pair<Integer, Integer> > getCorrectMove(){
        if(currentUserMove >= correctMoves.size())
            return null;

        int rowFrom = correctMoves.get(currentUserMove).getKey();
        int colFrom = correctMoves.get(currentUserMove).getValue();
        Pair<Integer, Integer> from = new Pair<>(rowFrom, colFrom);

        currentUserMove++;
        int rowTo = correctMoves.get(currentUserMove).getKey();
        int colTo = correctMoves.get(currentUserMove).getValue();
        Pair<Integer, Integer> to = new Pair<>(rowTo, colTo);

        currentUserMove++;

        return new Pair<>(from, to);
    }

    private Pair< Pair<Integer, Integer>, Pair<Integer, Integer> > getComputersMove(){
        if(currentComputerMove >= computerMoves.size())
            return null;

        int rowFrom = computerMoves.get(currentComputerMove).getKey();
        int colFrom = computerMoves.get(currentComputerMove).getValue();
        Pair<Integer, Integer> from = new Pair<>(rowFrom, colFrom);

        currentComputerMove++;
        int rowTo = computerMoves.get(currentComputerMove).getKey();
        int colTo = computerMoves.get(currentComputerMove).getValue();
        Pair<Integer, Integer> to = new Pair<>(rowTo, colTo);

        currentComputerMove++;

        return new Pair<>(from, to);
    }

    private Vector<Pair<Integer, Integer>> stringToVector(String moves){
        Vector<Pair<Integer, Integer>> result = new Vector<>();

        for(int i = 0; i < moves.length(); i++){
            // get coordinate move is made from
            result.add(new Pair<>(moves.charAt(i) - '0', moves.charAt(i + 1) - '0'));
            // get coordinates move is made to
            result.add(new Pair<>(moves.charAt(i + 2) - '0', moves.charAt(i + 3) - '0'));

            i += 4;
        }

        return result;
    }

    public String getPossibleMoves(){
        ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result = board.getAllPossibleMoves(Constants.pieceColor.white);

        return Stringify(result);
    }

    private String Stringify( ConcurrentHashMap< Pair<Integer, Integer>, Vector< Pair<Integer, Integer> > > result){
        String res="";
//        if(curPlayer.getColor()== Constants.pieceColor.black)
//            res = "B";
//        else
//            res = "W";

        for(Pair<Integer,Integer> key : result.keySet()){
            Vector<Pair<Integer,Integer>> val = result.get(key);

            for(int i  = 0; i < val.size(); i++){
                res += key.getKey() + "" + key.getValue() + "" + val.get(i).getKey() + "" + val.get(i).getValue();
            }
        }

        return res;
    }

    public String getBoardState(){
        String result="";

        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {


                if (board.getCell(row,col).getPieceType()==Constants.pieceType.emptyCell){
                    result+="00";
                } else {
                    if (board.getCell(row,col).getPieceColor()==Constants.pieceColor.white) {
                        result += 'W';
                    } else {
                        result += 'B';
                    }
                    if (board.getCell(row,col).getPieceType()==Constants.pieceType.Knight) {
                        result += 'N';
                    } else {
                        result += board.getCell(row, col).getPieceType().toString().charAt(0);
                    }

                }
            }
        }

        return result;
    }
}
