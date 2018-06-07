package Game.Model;

import javafx.util.Pair;

import java.util.Vector;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Vector<Move> history;
    private Vector<Pair<Integer, Integer>> possibleMoves;
    private Player curPlayer;
    private int clickNum;
    private Pair<Integer, Integer> markedCell;


    public Game(Player player1, Player player2){
        this.clickNum = 0;
        this.player1=player1;
        this.player2=player2;
        this.curPlayer = player1;
        board = new Board(true);
        history = new Vector<Move>();
    }

    Player getPlayer1(){
        return player1;
    }

    Player getPlayer2(){
        return player2;
    }

    public void click(int row, int col, Player player){
        if(curPlayer!=player)
            return;
        if(clickNum == 0){
            if(!board.getCell(row,col).hasPiece() || board.getCell(row,col).getPieceColor()!=curPlayer.getColor())
                return;
            markedCell = new Pair<Integer, Integer>(row,col);
            possibleMoves = board.getMoves(row,col,curPlayer.getColor());
            // return board.getMoves(row,col,curPlayer.getColor());
        }
        if(markedCell.equals(new Pair<>(row, col))){
            clickNum = 0;
            markedCell = new Pair<>(-1,-1);
            possibleMoves = null;
            return;
        }
        if(!possibleMoves.contains(new Pair<>(row,col)))
            return;
        switchPlayer();
        clickNum=0;
        board.move(markedCell.getKey(), markedCell.getValue(), row, col);
        markedCell = new Pair<>(-1,-1);
        possibleMoves = null;
    }

    private void switchPlayer() {
        if(curPlayer==player1) {
            curPlayer = player2;
            return;
        }
        curPlayer = player1;
    }
}
