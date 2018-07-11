package Puzzles;

import Game.Model.Board;
import Game.Model.Constants;
import Game.Model.Move;
import javafx.util.Pair;

import java.util.Vector;

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

    public Pair< Pair<Integer, Integer>, Pair<Integer, Integer> > getComputersMove(){
        if(currentComputerMove >= computerMoves.size())
            return null;

        int rowFrom = computerMoves.get(currentComputerMove).getKey();
        int colFrom = computerMoves.get(currentComputerMove).getValue();
        Pair<Integer, Integer> from = new Pair<>(rowFrom, colFrom);

        currentComputerMove++;
        int rowTo = computerMoves.get(currentComputerMove).getKey();
        int colTo = computerMoves.get(currentComputerMove).getValue();
        Pair<Integer, Integer> to = new Pair<>(rowTo, colTo);

        return new Pair<>(from, to);
    }

    public Pair< Pair<Integer, Integer>, Pair<Integer, Integer> > getCorrectMove(){
        if(currentUserMove >= correctMoves.size())
            return null;

        int rowFrom = correctMoves.get(currentUserMove).getKey();
        int colFrom = correctMoves.get(currentUserMove).getValue();
        Pair<Integer, Integer> from = new Pair<>(rowFrom, colFrom);

        currentUserMove++;
        int rowTo = correctMoves.get(currentUserMove).getKey();
        int colTo = correctMoves.get(currentUserMove).getValue();
        Pair<Integer, Integer> to = new Pair<>(rowTo, colTo);

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
}
