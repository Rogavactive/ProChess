package Game.Controller;


import Accounting.Model.Account;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/gamechat/{game-id}", configurator = ChatSocketConfig.class)
public class IngameChatSocket {

    private static ConcurrentHashMap<String,Set<Session>> chatusers = new ConcurrentHashMap<>();
    private static ReentrantLock onmessage_lock = new ReentrantLock();

    @OnOpen
    public void onOpen(@PathParam("game-id") String gameID, Session session, EndpointConfig config) {
//        System.out.println("chat onOpen::" + session.getId());
        HttpSession httpSession;
        try{
            httpSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
            Account acc = (Account) httpSession.getAttribute("Account");
            session.getUserProperties().put("Username",acc.getUsername());
        }catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Something(httpsession,acc or username)" +
                    " is null. This occured at WebSocketSession: " + session.getId());
            return;
        }
        if(gameID==null){
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(chatusers.containsKey(gameID)){
            Set<Session> room_users = chatusers.get(gameID);
            room_users.add(session);
        }else{
            Set<Session> newSet = Collections.synchronizedSet(new HashSet<Session>());
            newSet.add(session);
            chatusers.put(gameID,newSet);
        }
//        try {
//            sendMessageToAll("ProChessBot", acc.getUsername() + " joined chat!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @OnClose
    public void onClose(@PathParam("game-id") String gameID,Session session) {
        if(!chatusers.containsKey(gameID))
            return;
        Set<Session> chatRoom = chatusers.get(gameID);
        chatRoom.remove(session);
        if(chatRoom.size()==0){
            chatusers.remove(gameID);
        }
    }

    @OnMessage
    public void onMessage(@PathParam("game-id") String gameID,String message, Session session) throws IOException {
//        System.out.println("chat onMessage::From=" + session.getId() + " Message=" + message);
        if(message.equals(""))
            return;
        String username = (String) session.getUserProperties().get("Username");
        sendMessageToAll(gameID,username,message);
//        System.out.println("chat after onMessage::From=" + session.getId() + " Message=" + message);
    }

    private void sendMessageToAll(String gameID, String username, String message) throws IOException {
        onmessage_lock.lock();
        Iterator<Session> it = chatusers.get(gameID).iterator();
        while(it.hasNext()) it.next().getBasicRemote().sendText(BuildJson(username, message));
        onmessage_lock.unlock();
    }

    private String BuildJson(String username, String msg){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
        }catch (JSONException e){
            e.printStackTrace();
        }
        jsonObject.append("message",msg);
        jsonObject.append("username",username);
        return jsonObject.toString();
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError(chat)::" + t.getMessage());
    }
}