package services.websocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.HashMap;

@ApplicationScoped
public class SocketsHandler {
    private HashMap<Session, String> sessions = new HashMap<>(); //session and name
    private HashMap<Session,Integer> gamesId=new HashMap<>();//session and gameId


    public void addSession(Session session, String name) {
        sessions.put(session,name);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        gamesId.remove(session);
    }

    public String getName(Session session){
        return sessions.get(session);
    }

    public void addGame(Session session,Integer gameId){gamesId.put(session,gameId);}

    public Integer getGameId(Session session){
        return gamesId.get(session);
    }


}
