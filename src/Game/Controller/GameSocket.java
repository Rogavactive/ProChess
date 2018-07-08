package Game.Controller;

import Accounting.Model.Account;
import Game.Model.Game;
import Game.Model.GameManager;
import org.json.JSONException;
import org.json.JSONObject;

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
        session.getUserProperties().put("acc_ID",acc.getID());
        sessions.put(acc.getID(),session);
        Game game = manager.getGameByID(ID);
        try {
            String boardState = game.getBoardState();
            String color = game.getPlayerColor(acc);
            String possibleMoves = game.getCurrentPossibleMoves(acc);
            JSONObject json_message = GenerateBoardJSON(boardState,possibleMoves,color);
            session.getBasicRemote().sendText(json_message.toString());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove((Integer)session.getUserProperties().get("acc_ID"));
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
                JSONObject json = null;
                try {
                    json = new JSONObject();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                json.put("type","error");
                json.put("message","no_game_id");
                session.getBasicRemote().sendText(json.toString());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Game game = manager.getGameByID(ID);
        Account Opponent;
        if(game.getPlayer1().getAccount() == acc)
            Opponent = game.getPlayer2().getAccount();
        else
            Opponent = game.getPlayer1().getAccount();
        //if it is not players turn to play
        if(acc != game.getCurPlayer().getAccount())
            return;
        executeMessage(message,session,ID,manager,Opponent,acc);
//        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

    }

    private void executeMessage(String message, Session session,String GameID, GameManager manager, Account Opponent, Account acc) {

        Game game = manager.getGameByID(GameID);
        int srcRow = Character.getNumericValue(message.charAt(0));
        int srcCol = Character.getNumericValue(message.charAt(1));
        int dstRow = Character.getNumericValue(message.charAt(2));
        int dstCol = Character.getNumericValue(message.charAt(3));

        try {
            JSONObject opponent_json;
            JSONObject curr_json;

            game.pieceMoved(srcRow,srcCol,dstRow,dstCol);
            Session OpponentSession = sessions.get(Opponent.getID());
            String opponentMoves = game.getCurrentPossibleMoves(Opponent);
            String currMoves = game.getCurrentPossibleMoves(acc);
            String currColor = game.getPlayerColor(acc);
            String opponentColor = game.getPlayerColor(Opponent);
            String boardState = game.getBoardState();

            if(opponentMoves.equals("You Win") || opponentMoves.equals("You Lose") || opponentMoves.equals("Draw")){
                opponent_json = GenerateWinnerJSON(opponentMoves);
                curr_json = GenerateWinnerJSON(currMoves);
                HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");
                httpSession.removeAttribute("gameID");
                if(OpponentSession.isOpen()) {
                    HttpSession opponentHttpSession = (HttpSession) OpponentSession.getUserProperties().get("HttpSession");
                    opponentHttpSession.removeAttribute("gameID");
                }
            }else{
                opponent_json = GenerateBoardJSON(boardState,opponentMoves,opponentColor);
                curr_json = GenerateBoardJSON(boardState,currMoves,currColor);
            }

            if(OpponentSession.isOpen()) {
                OpponentSession.getBasicRemote().sendText(opponent_json.toString());
            }
            session.getBasicRemote().sendText(curr_json.toString());
        } catch (CloneNotSupportedException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject GenerateBoardJSON(String boardState, String currentMovesPossible, String playerColor){
        JSONObject json = null;
        try {
            json = new JSONObject();
        }catch (JSONException e){
            e.printStackTrace();
        }
        json.put("type","board_state");
        json.put("board",boardState);
        json.put("player",playerColor);
        json.put("moves",currentMovesPossible);
        return json;
    }

    private JSONObject GenerateWinnerJSON(String status){
        JSONObject json = null;
        try {
            json = new JSONObject();
        }catch (JSONException e){
            e.printStackTrace();
        }
        json.put("type","endgame");
        json.put("status",status);
        return json;
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError(game)::" + t.getMessage());
    }
}