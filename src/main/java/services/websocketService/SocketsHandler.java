package services.websocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.HashMap;

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
