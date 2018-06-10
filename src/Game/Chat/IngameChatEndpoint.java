package Game.Chat;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/gamechat")
public class IngameChatEndpoint {

    static Set<Session> chatusers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
//        System.out.println("chat onOpen::" + session.getId());
        chatusers.add(session);
    }
    @OnClose
    public void onClose(Session session) {
//        System.out.println("chat onClose::" +  session.getId());
        chatusers.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if(message.equals(""))
            return;
//        System.out.println("chat onMessage::From=" + session.getId() + " Message=" + message);
        String username = (String) session.getUserProperties().get("username");
        if(username==null){
            session.getUserProperties().put("username",message);
            username = "ProChessBot";
            message = message + " joined chat!";
        }
        Iterator<Session> it = chatusers.iterator();
        while(it.hasNext()) it.next().getBasicRemote().sendText(BuildJson(username, message));
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