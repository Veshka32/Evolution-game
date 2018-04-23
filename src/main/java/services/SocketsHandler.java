package services;

import model.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class SocketsHandler {
    private HashMap<Session, String> sessions = new HashMap<>();

    public void addSession(Session session, String name) {
        sessions.put(session,name);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public String getName(Session session){
        return sessions.get(session);
    }


}
