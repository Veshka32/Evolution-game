package servlets;

import model.Game;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/socket")
public class WebSocketServer {

    private SocketsHandler socketsHandler=SocketsHandler.getInstance();
    private Game game=Game.getInstance();

    @OnOpen
    public void open(Session session) {
        socketsHandler.addSession(session);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        game.makeMove(message);
        socketsHandler.sendToAllConnectedSessions(game.printMoves());
    }

    @OnClose
    public void close(Session session) {
        socketsHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }


}