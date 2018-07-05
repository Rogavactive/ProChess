package GameConnection.Controller;

import Accounting.Model.Account;
import Game.Model.Constants;
import Game.Model.GameManager;
import Game.Model.Player;
import GameConnection.Model.GameSearchManager;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/gamesearch", configurator = SearchServletConfig.class)
public class GameSearchEndpoint {

    private static ConcurrentHashMap<Integer , Session > users_in_queue = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
//        System.out.println("chat onOpen::" + session.getId());
        HttpSession currSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
        session.getUserProperties().put("HttpSession",currSession);
    }

    @OnClose
    public void onClose(Session session) {
        HttpSession httpSession = (HttpSession)session.getUserProperties().get("HttpSession");
        Account acc = (Account) httpSession.getAttribute("Account");
        GameSearchManager searchManager = (GameSearchManager) httpSession.getServletContext().getAttribute("GameSearchManager");
        searchManager.removeFromQueue(acc.getID());
        users_in_queue.remove(acc.getID());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//        System.out.println("chat onMessage::From=" + session.getId() + " Message=" + message);
        HttpSession httpSession = (HttpSession)session.getUserProperties().get("HttpSession");
        Account acc = (Account) httpSession.getAttribute("Account");
        GameManager gameManager = (GameManager) httpSession.getServletContext().getAttribute("GameManager");
        GameSearchManager searchManager = (GameSearchManager) httpSession.getServletContext().getAttribute("GameSearchManager");
        JSONObject jsonObject = new JSONObject(message);
        String gameType = (String) jsonObject.get("game_type");
        String time_primary = (String) jsonObject.get("time_primary");
        String time_bonus = (String) jsonObject.get("time_bonus");
        int opponent = searchManager.findOpponent(acc,time_primary,time_bonus);
        if(opponent==-1)
            return;
        if(opponent==0){
            users_in_queue.put(acc.getID(),session);
        }else{
            //create game with this opponent
            Session opponentSession = users_in_queue.get(opponent);
            HttpSession opponentHttpSession = (HttpSession) opponentSession.getUserProperties().get("HttpSession");
            Account opponentAcc = (Account) opponentHttpSession.getAttribute("Account");
            String id = gameManager.registerGame(new Player(acc,Constants.pieceColor.white),new Player(opponentAcc,Constants.pieceColor.black));
            if(id==null)
                return;
            //search for the game, store user's session and search for the game here. if found send them callbacks.
            //input: game_type:random, friendly, bot   ,   time_primary:1,2,5,10,15     ,    time_bonus:1,2,5,10
            JSONObject response_json = new JSONObject();
            response_json.put("type", Integer.parseInt(gameType));
            response_json.put("id", id);//generate random non-repeatable id.
            httpSession.setAttribute("gameID",id);
            opponentHttpSession.setAttribute("gameID",id);
            String response_text = response_json.toString();
            session.getBasicRemote().sendText(response_text);
            opponentSession.getBasicRemote().sendText(response_text);
            //output: type:(0 - random,1 - friendly,2 - bot), id:generated_match_id
        }
//        System.out.println("chat after onMessage::From=" + session.getId() + " Message=" + message);
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
}