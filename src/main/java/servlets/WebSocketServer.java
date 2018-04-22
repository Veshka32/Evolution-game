package servlets;

import model.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint(value = "/socket/{param}",configurator = SocketConfigurator.class)
public class WebSocketServer {

    //private SocketsHandler socketsHandler=SocketsHandler.getInstance();
    @Inject
    private Game game;

    @OnOpen
    public void open(Session session, EndpointConfig config) throws IOException {
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String player= (String) httpSession.getAttribute("player");
        System.out.println(player);
        JsonObject gameStatus=game.convertToJSon(httpSession);
        sendToAll(gameStatus,session);
        System.out.println("queru="+session.getQueryString().split("=")[1]);
        //socketsHandler.addSession(session);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        game.makeMove(message);
        JsonObject gameStatus=game.convertToJson();
        sendToAll(gameStatus,session);

            //socketsHandler.sendToAllConnectedSessions(game.printMoves());
    }

    @OnClose
    public void close(Session session) {
        //socketsHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    private void sendToAll(JsonObject json, Session session){
        for (Session s : session.getOpenSessions()) {
            try{
                s.getBasicRemote().sendText(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }}
    }


}