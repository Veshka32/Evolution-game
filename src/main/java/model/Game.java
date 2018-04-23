package model;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class Game {

    private HashMap<String, String> players=new HashMap<>();
    private PropertyChangeSupport changeFlag =
            new PropertyChangeSupport(this);

    private List<String> moves=new ArrayList<>();

    public void addPlayer(HttpSession session, String userName){
        players.put(session.getId(),userName);
        changeFlag.firePropertyChange("game",true,false);
    }

    public String getPlayerByID(HttpSession session){
        return players.get(session.getId());
    }

    public String playersList(){
        List<String> playersList=new ArrayList(players.values());
        return playersList.stream().collect(Collectors.joining(", "));
    }

    public boolean isFull(){
        return players.size()>1;
    }

    public void
    addPropertyChangeListener(PropertyChangeListener listener) {
        changeFlag.addPropertyChangeListener(listener);
    }

    public void
    removePropertyChangeListener(PropertyChangeListener listener) {
        changeFlag.removePropertyChangeListener(listener);
    }

    public void makeMove(String move){
        moves.add(move);
    }

    public String printMoves(){
        return moves.stream().collect(Collectors.joining("/"));
    }

    public JsonObject convertToJson() {
        JsonProvider provider = JsonProvider.provider();
        JsonObject json = provider.createObjectBuilder()
                .add("players", playersList())
                .add("moves", printMoves())
                .build();
        return json;
    }

//    public JsonObject convertToJson(String name){
//
//    }
}
