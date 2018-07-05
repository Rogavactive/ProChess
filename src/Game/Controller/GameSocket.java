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


@ServerEndpoint(value = "/game", configurator = SocketConfig.class)
public class GameSocket {
    private ConcurrentHashMap<Account, Session> sessions;
    private static ReentrantLock onopen_lock = new ReentrantLock();
    private static ReentrantLock onmessage_lock = new ReentrantLock();
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
        String ID =(String) httpSession.getAttribute("gameID");
        GameManager manager = (GameManager) httpSession.getServletContext().getAttribute("GameManager") ;
        Account acc = (Account)httpSession.getAttribute("Account") ;
        session.getUserProperties().put("Account", acc);
        session.getUserProperties().put("GameID", ID);
        session.getUserProperties().put("GameManager", manager);

        sessions.put(acc,session);
        Game game = manager.getGameByID(ID);
        try {
            session.getBasicRemote().sendText(game.getBoardState() + " " + game.getCurrentPossibleMoves(acc));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("onOpen::" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getUserProperties().get("Account"));
        System.out.println("onClose::" +  session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        Account acc = (Account)session.getUserProperties().get("Account");
        GameManager manager = (GameManager) session.getUserProperties().get("GameManager");
        String ID = (String)session.getUserProperties().get("GameID");
        Game game = manager.getGameByID(ID);
        Account Opponent;
        if(game.getPlayer1().getAccount() == acc)
            Opponent = game.getPlayer2().getAccount();
        else
            Opponent = game.getPlayer1().getAccount();
        //if it is not players turn to play
        if(acc != game.getCurPlayer().getAccount())
            return;
        executeMessage(message,session,game,Opponent,acc);
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

    }

    private void executeMessage(String message, Session session, Game game, Account Opponent, Account acc) {
        int srcRow = Character.getNumericValue(message.charAt(0));
        int srcCol = Character.getNumericValue(message.charAt(1));
        int dstRow = Character.getNumericValue(message.charAt(2));
        int dstCol = Character.getNumericValue(message.charAt(3));
        try {
            game.pieceMoved(srcRow,srcCol,dstRow,dstCol);
            //the player already used his move so the opponent becomes the currentPlayer and these moves are
            //for him
            session.getBasicRemote().sendText(game.getBoardState() + " " + game.getCurrentPossibleMoves(acc));
            Session OpponentSession = sessions.get(Opponent);
            OpponentSession.getBasicRemote().sendText(game.getBoardState() + " "+ game.getCurrentPossibleMoves(Opponent));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError(game)::" + t.getMessage());
    }
}