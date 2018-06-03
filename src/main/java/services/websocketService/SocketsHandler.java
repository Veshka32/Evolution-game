package services.websocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SocketsHandler {
    private Map<Session, String> sessions = new HashMap<>(); //session and name
    private Map<Session,Integer> gamesId=new HashMap<>();//session and gameId
    private Map<Session,HttpSession> httpSessions=new HashMap<>();


    void addSession(Session session, String name,HttpSession httpSession,Integer gameId) {
        sessions.put(session,name);
        gamesId.put(session,gameId);
        httpSessions.put(session,httpSession);
    }

    void removeSession(Session session) {
        sessions.remove(session);
        gamesId.remove(session);
        httpSessions.remove(session);
    }

    HttpSession  getHttpSession(Session session){
        return httpSessions.get(session);
    }

    String getName(Session session){
        return sessions.get(session);
    }

    Integer getGameId(Session session){
        return gamesId.get(session);
    }


}
