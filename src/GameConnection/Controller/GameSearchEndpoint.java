package GameConnection.Controller;

import Accounting.Model.Account;
import Game.Controller.ServletConfig;
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

@ServerEndpoint(value = "/gamesearch", configurator = ServletConfig.class)
public class GameSearchEndpoint{

    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {

    }

    @OnClose
    public void onClose(Session session){

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

    }

    private void sendMessageToAll(String username, String message) throws IOException {

    }

    private String BuildJson(String username, String msg){
        return null;
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
}