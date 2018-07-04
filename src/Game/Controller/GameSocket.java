package Game.Controller;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/game", configurator = SocketConfig.class)
public class GameSocket {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = ((HttpSession)config.getUserProperties().get("HttpSession"));
        String ID =(String) httpSession.getAttribute("gameID");

        System.out.println("onOpen::" + session.getId());
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

        try {
            session.getBasicRemote().sendText("Hello Client " + session.getId() + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError(game)::" + t.getMessage());
    }
}