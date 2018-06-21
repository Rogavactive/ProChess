package Game.Controller;


import Accounting.Model.Account;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/gamechat", configurator = ChatServletConfig.class)
public class IngameChatEndpoint {

    static Set<Session> chatusers = Collections.synchronizedSet(new HashSet<Session>());
    private static ReentrantLock onopen_lock = new ReentrantLock();
    private static ReentrantLock onmessage_lock = new ReentrantLock();

    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
//        System.out.println("chat onOpen::" + session.getId());
        onopen_lock.lock();
        HttpSession httpSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
        if(httpSession==null){
            System.out.println("HttpSession is null. This occured at WebSocketSession: " + session.getId());
            onopen_lock.unlock();
            return;
        }
        Account acc = (Account) httpSession.getAttribute("Account");
        if(acc==null){
            System.out.println("Account is null. This occured at WebSocketSession: " + session.getId());
            onopen_lock.unlock();
            return;
        }
        chatusers.add(session);
        session.getUserProperties().put("Account",acc);
        onopen_lock.unlock();
//        try {
//            sendMessageToAll("ProChessBot", acc.getUsername() + " joined chat!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @OnClose
    public void onClose(Session session) {
        chatusers.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//        System.out.println("chat onMessage::From=" + session.getId() + " Message=" + message);
        if(message.equals(""))
            return;
        String username = ((Account) session.getUserProperties().get("Account")).getUsername();
        sendMessageToAll(username,message);
//        System.out.println("chat after onMessage::From=" + session.getId() + " Message=" + message);
    }

    private void sendMessageToAll(String username, String message) throws IOException {
        onmessage_lock.lock();
        Iterator<Session> it = chatusers.iterator();
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
        System.out.println("onError::" + t.getMessage());
    }
}