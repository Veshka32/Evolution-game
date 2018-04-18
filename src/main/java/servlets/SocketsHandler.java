package servlets;

import model.Game;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketsHandler {
    private Set<Session> sessions = new HashSet<>();
    private static SocketsHandler instance = new SocketsHandler();
    Game game = Game.getInstance();

    private SocketsHandler() {
    }

    public static SocketsHandler getInstance() {
        return instance;
    }


    void addSession(Session session) {
        sessions.add(session);
        String message = game.printMoves();
        sendToSession(session, message);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToAllConnectedSessions(String message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(SocketsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
