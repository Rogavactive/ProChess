package Game.Model;

import java.util.Vector;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Vector<Move> history;

    public Game(Player player1, Player player2){
        this.player1=player1;
        this.player2=player2;
        board = new Board(true);
        history = new Vector<Move>();
    }

    Player getPlayer1(){
        return player1;
    }

    Player getPlayer2(){
        return player2;
    }

}
