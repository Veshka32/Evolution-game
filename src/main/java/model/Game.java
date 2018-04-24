package model;

import entities.Move;
import entities.Player;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class Game {

    private HashMap<String, Player> players=new HashMap<>();
    private PropertyChangeSupport changeFlag =
            new PropertyChangeSupport(this);
    private String lastMove="New game started";

    public void addPlayer(String userName){
        players.put(userName,new Player(userName));
        changeFlag.firePropertyChange("game",true,false);
    }

    public String playersList(){
        Set<String> names=players.keySet();
        String[] names1 = names.toArray(new String[names.size()]);
        String playersList=Arrays.toString(names1);
        return playersList;
    }

    public boolean isFull(){
        return players.size()>1;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeFlag.addPropertyChangeListener(listener);
    }

    public void
    removePropertyChangeListener(PropertyChangeListener listener) {
        changeFlag.removePropertyChangeListener(listener);
    }

    public void makeMove(Move move){
        lastMove=move.toString();
    }

//    public String printMoves(){
//        return moves.stream().map(Object::toString).collect(Collectors.joining("/"));
//    }

    public JsonObject convertToJson(String name){
        JsonProvider provider = JsonProvider.provider();
        JsonObject json = provider.createObjectBuilder()
                .add("players", playersList())
                .add("moves",lastMove)
                .add("cards", players.get(name).getCards())
                .build();
        return json;
    }
}
