package Game.Controller;

import Accounting.Model.Account;
import Game.Model.Game;
import Game.Model.GameManager;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


@ServerEndpoint(value = "/game", configurator = GameSocketConfig.class)
public class GameSocket {
    private static ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<>();
    private static ReentrantLock onopen_lock = new ReentrantLock();
    private static ReentrantLock onmessage_lock = new ReentrantLock();
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
//        System.out.println("game onOpen::" + session.getId());
        HttpSession httpSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
        String ID =(String) httpSession.getAttribute("gameID");
        GameManager manager = (GameManager) httpSession.getServletContext().getAttribute("GameManager");
        Account acc = (Account)httpSession.getAttribute("Account");
        session.getUserProperties().put("HttpSession",httpSession);
        sessions.put(acc.getID(),session);
        Game game = manager.getGameByID(ID);
        try {

            session.getBasicRemote().sendText(game.getBoardState() +game.getCurrentPossibleMoves(acc));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        HttpSession httpSession = (HttpSession)session.getUserProperties().get("HttpSession");
        sessions.remove(((Account)httpSession.getAttribute("Account")).getID());
        System.out.println("onClose::" +  session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");
        Account acc = (Account)httpSession.getAttribute("Account");
        GameManager manager = (GameManager) httpSession.getServletContext().getAttribute("GameManager");
        String ID = (String)httpSession.getAttribute("gameID");
        if(ID==null){
            try {
                session.getBasicRemote().sendText("Error");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(message);
        Game game = manager.getGameByID(ID);
        Account Opponent;
        if(game.getPlayer1().getAccount() == acc)
            Opponent = game.getPlayer2().getAccount();
        else
            Opponent = game.getPlayer1().getAccount();
        //if it is not players turn to play
        if(acc != game.getCurPlayer().getAccount())
            return;
        System.out.println("Asd");
        executeMessage(message,session,ID,manager,Opponent,acc);
//        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

    }

    private void executeMessage(String message, Session session,String GameID, GameManager manager, Account Opponent, Account acc) {

        Game game = manager.getGameByID(GameID);
        int srcRow = Character.getNumericValue(message.charAt(0));
        int srcCol = Character.getNumericValue(message.charAt(1));
        int dstRow = Character.getNumericValue(message.charAt(2));
        int dstCol = Character.getNumericValue(message.charAt(3));
        System.out.println(srcRow);
        System.out.println(srcCol);
        System.out.println(dstRow);
        System.out.println(dstCol);
        try {
            game.pieceMoved(srcRow,srcCol,dstRow,dstCol);
            System.out.println("9");
            Session OpponentSession = sessions.get(Opponent.getID());
            System.out.println("10 " + Opponent.getID());
            if(OpponentSession==null){
                System.out.println("dis is truuu");
            }
            System.out.println(OpponentSession.getId());

            String CurrentMoves = game.getCurrentPossibleMoves(Opponent);

            System.out.println("11");
            String boardState = game.getBoardState();
            System.out.println("12");
            System.out.println(OpponentSession.toString());
            if(OpponentSession.isOpen()) {
                System.out.println("13");
                OpponentSession.getBasicRemote().sendText(boardState  + CurrentMoves);
            }
            //the player already used his move so the opponent becomes the currentPlayer and these moves are
            //for him
            System.out.println("14");
            if(CurrentMoves.equals("Winner1") || CurrentMoves.equals("Winner2") || CurrentMoves.equals("Draw")){
                System.out.println("15");
                session.getBasicRemote().sendText(boardState   + CurrentMoves);
                HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");
                httpSession.removeAttribute("gameID");
                if(OpponentSession.isOpen()) {
                    HttpSession opponentHttpSession = (HttpSession) OpponentSession.getUserProperties().get("HttpSession");
                    opponentHttpSession.removeAttribute("gameID");
                }
                manager.endGame(GameID);
            } else
                session.getBasicRemote().sendText(boardState  + game.getCurrentPossibleMoves(acc));
            System.out.println("16");
        } catch (CloneNotSupportedException | SQLException | IOException e) {

            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError(game)::" + t.getMessage());
    }
}