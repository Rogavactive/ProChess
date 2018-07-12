package GameConnection.Controller;

import Accounting.Model.Account;
import Game.Model.Constants;
import Game.Model.GameManager;
import Game.Model.GameType;
import Game.Model.Player;
import GameConnection.Model.GameSearchManager;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/gamesearch", configurator = SearchServletConfig.class)
public class GameSearchEndpoint {

    private static ConcurrentHashMap<Integer , Session > users_in_queue = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer , Session > users_waiting_friend = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
//        System.out.println("chat onOpen::" + session.getId());
        HttpSession currSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
        Account acc = (Account) currSession.getAttribute("Account");
        session.getUserProperties().put("userID",acc.getID());
        session.getUserProperties().put("HttpSession",currSession);
    }

    @OnClose
    public void onClose(Session session) {
        int accID = (Integer)session.getUserProperties().get("userID");
        GameSearchManager searchManager = GameSearchManager.getInstance();
        searchManager.removeFromQueue(accID);
        if(users_in_queue.containsKey(accID)) {
            users_in_queue.remove(accID);
        }
        else if(users_waiting_friend.containsKey(accID)) {
            users_waiting_friend.remove(accID);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//        System.out.println("chat onMessage::From=" + session.getId() + " Message=" + message);
        HttpSession httpSession = (HttpSession)session.getUserProperties().get("HttpSession");
        String gameid = (String)httpSession.getAttribute("gameID");
        if(gameid!=null){
            JSONObject response_json = new JSONObject();
            response_json.put("type", -1);
            String response_text = response_json.toString();
            session.getBasicRemote().sendText(response_text);
            return;
        }
        JSONObject jsonObject = new JSONObject(message);
        int gameType = Integer.parseInt((String)jsonObject.get("game_type"));
        if(gameType==0){
            findRandomOpponent(httpSession,jsonObject,session);
        }else if(gameType==1){
            int opponentID = Integer.parseInt((String)jsonObject.get("opponent_id"));
            System.out.println(opponentID);
            Account acc = (Account) httpSession.getAttribute("Account");
            if(opponentID==0){
                users_waiting_friend.put(acc.getID(),session);
                String time_primary = (String) jsonObject.get("time_primary");
                String time_bonus = (String) jsonObject.get("time_bonus");
                session.getUserProperties().put("time_primary",time_primary);
                session.getUserProperties().put("time_bonus",time_bonus);
                JSONObject response_json = new JSONObject();
                response_json.put("type", 3);
                response_json.put("link", "http://localhost:8080/main.jsp?id=" + acc.getID());
                String response_text = response_json.toString();
                session.getBasicRemote().sendText(response_text);
            }else{
                Session opponentSession = users_waiting_friend.get(opponentID);
                if(!opponentSession.isOpen()){
                    return;
                }
                HttpSession opponentHttpSession = (HttpSession) opponentSession.getUserProperties().get("HttpSession");
                Account opponentAcc = (Account) opponentHttpSession.getAttribute("Account");
                GameManager gameManager = GameManager.getInstance();
                //////////////
                String time_primary = (String) opponentSession.getUserProperties().get("time_primary");
                String time_bonus = (String) opponentSession.getUserProperties().get("time_bonus");
                /////////////

                String id = gameManager.registerGame(new Player(acc,Constants.pieceColor.white),new Player(opponentAcc,Constants.pieceColor.black),
                        new GameType(time_primary,time_bonus));
                if(id==null)
                    return;
                JSONObject response_json = new JSONObject();
                response_json.put("type", 1);
                response_json.put("id", id);//generate random non-repeatable id.
                httpSession.setAttribute("gameID",id);
                opponentHttpSession.setAttribute("gameID",id);
                String response_text = response_json.toString();
                session.getBasicRemote().sendText(response_text);
                opponentSession.getBasicRemote().sendText(response_text);
            }
        }else{
            //bot
        }
//        System.out.println("chat after onMessage::From=" + session.getId() + " Message=" + message);
    }

    private void findRandomOpponent(HttpSession httpSession,JSONObject jsonObject, Session session) throws IOException {
        Account acc = (Account) httpSession.getAttribute("Account");
        GameManager gameManager = (GameManager) httpSession.getServletContext().getAttribute("GameManager");
        GameSearchManager searchManager = (GameSearchManager) httpSession.getServletContext().getAttribute("GameSearchManager");
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
            String id = gameManager.registerGame(new Player(acc,Constants.pieceColor.white),new Player(opponentAcc,Constants.pieceColor.black),
                    new GameType(time_primary,time_bonus));
            if(id==null)
                return;
            //search for the game, store user's session and search for the game here. if found send them callbacks.
            //input: game_type:random, friendly, bot   ,   time_primary:1,2,5,10,15     ,    time_bonus:1,2,5,10
            JSONObject response_json = new JSONObject();
            response_json.put("type", 0);
            response_json.put("id", id);//generate random non-repeatable id.
            httpSession.setAttribute("gameID",id);
            opponentHttpSession.setAttribute("gameID",id);
            String response_text = response_json.toString();
            session.getBasicRemote().sendText(response_text);
            opponentSession.getBasicRemote().sendText(response_text);
            //output: type:(0 - random,1 - friendly,2 - bot), id:generated_match_id
        }
    }


    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
}