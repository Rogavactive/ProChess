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
            JSONObject json_message;
            if(possibleMoves.equals("You Win")||possibleMoves.equals("You Lose")||possibleMoves.equals("Draw")){
                json_message = GenerateWinnerJSON(possibleMoves);
            }else
                json_message = GenerateBoardJSON(boardState,possibleMoves,color);
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
        Account Opponent = game.getOpponent(acc).getAccount();

        //if it is not players turn to play



            JSONObject json = null;
            try {
                json = new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (message.equals("drawRequested")) {
                json.put("type", "drawRequested");
                sendToUser(json.toString(),Opponent);
                return;
            }
            if (message.equals("undoRequested")) {
                if (game.getCurPlayer().getAccount()==acc) return;
                json.put("type", "undoRequested");
                sendToUser(json.toString(),Opponent);
                return;
            }

            if (message.equals("drawAccepted")) {
                //draw both side agreement
                drawAccepted(game,Opponent,acc);
                return;
            }

            if (message.equals("undoAccepted")) {
                //undo both side agreement
                undoAccepted(game,session,Opponent,acc);
                return;
            }

            if (message.equals("drawDeclined")) {
                json.put("type", "drawDeclined");
                sendToUser(json.toString(),Opponent);
            }

            if (message.equals("undoDeclined")) {
                json.put("type", "undoDeclined");
                sendToUser(json.toString(),Opponent);
                return;
            }

        if (message.equals("resignRequested")) {
            playerResigned(game,Opponent,acc);
            return;
        }

        if(acc != game.getCurPlayer().getAccount())
            return;

        executeMessage(message,session,ID,manager,Opponent,acc);
//        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

    }
    private void playerResigned(Game game,Account Opponent, Account acc){
        try {
            ////////////////////send winner message//////////////////
            JSONObject json = null;
            try {
                json = new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            game.gameOver(0);

            json.put("type","endgame");
            json.put("status","Win");
            sendToUser(json.toString(),Opponent);
            ////////////////////send loser message//////////////////
            JSONObject json2 = null;
            try {
                json2 = new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            game.gameOver(0);

            json2.put("type","endgame");
            json2.put("status","Win");
            sendToUser(json2.toString(),acc);

        } catch (SQLException e){

        }
    }
    private void drawAccepted(Game game,Account Opponent, Account acc){

        try {
            JSONObject json = null;
            try {
                json = new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            game.gameOver(0);

            json.put("type","endgame");
            json.put("status","Draw");
            sendToUser(json.toString(),Opponent);
            sendToUser(json.toString(),acc);

        } catch (SQLException e){

        }
    }

    private void undoAccepted(Game game,Session session, Account Opponent, Account acc){
        game.undo();

        try {
            JSONObject opponent_json;
            JSONObject curr_json;

            Session OpponentSession = sessions.get(Opponent.getID());
            String opponentMoves = game.getCurrentPossibleMoves(Opponent);
            String currMoves = game.getCurrentPossibleMoves(acc);
            String currColor = game.getPlayerColor(acc);
            String opponentColor = game.getPlayerColor(Opponent);
            String boardState = game.getBoardState();

            opponent_json = GenerateBoardJSON(boardState,opponentMoves,opponentColor);
            curr_json = GenerateBoardJSON(boardState,currMoves,currColor);
            try {
                OpponentSession.getBasicRemote().sendText(opponent_json.toString());
                session.getBasicRemote().sendText(curr_json.toString());
            } catch (IOException e){

            }
            JSONObject json = null;
            try {
                json = new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            json.put("type", "undoAccepted");
            sendToUser(json.toString(), Opponent);

            System.out.println("Undo in Game.");
        } catch (SQLException e){

        }
    }
    private void sendToUser(String msg,Account User){
        try {
            Session OpponentSession = sessions.get(User.getID());
            OpponentSession.getBasicRemote().sendText(msg);
        } catch (IOException e){
            e.printStackTrace();
        }
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

    private static JSONObject GenerateBoardJSON(String boardState, String currentMovesPossible, String playerColor){
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

    private static JSONObject GenerateWinnerJSON(String status){
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

    public static void sendMessage(Account acc, Game game, String status){
        //to do time passed feature
        if(status == "timeUp"){
            Session myS = sessions.get(acc.getID());
            Session opS = sessions.get(game.getOpponent(acc).getAccount().getID());
            try {
                myS.getBasicRemote().sendText(GenerateWinnerJSON("Time Is Up, You Lose").toString());
                opS.getBasicRemote().sendText(GenerateWinnerJSON("Opponents Time Is Up, You Win").toString());
                ((HttpSession)myS.getUserProperties().get("HttpSession")).removeAttribute("gameID");
                ((HttpSession)opS.getUserProperties().get("HttpSession")).removeAttribute("gameID");
            } catch (IOException e) {
                e.printStackTrace();
            }
            GameManager.getInstance().endGame(game.getId());
        }
        if(status == "OpponentLeft"){
            Session myS = sessions.get(acc.getID());
            try {
                myS.getBasicRemote().sendText(GenerateWinnerJSON("Opponent Left, You Won").toString());
                ((HttpSession)myS.getUserProperties().get("HttpSession")).removeAttribute("gameID");
            } catch (IOException e) {
                e.printStackTrace();
            }
            GameManager.getInstance().endGame(game.getId());
        }
        System.out.println("Time up for " + acc.getUsername() );
    }
}