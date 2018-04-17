package model;

import javax.servlet.http.HttpSession;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private static Game ourInstance = new Game();
    private HashMap<String, String> players;
    private PropertyChangeSupport changeFlag =
            new PropertyChangeSupport(this);

    private List<String> moves;


    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        players=new HashMap<>();
        moves=new ArrayList<>();
    }

    public void addPlayer(HttpSession session, String userName){
        players.put(session.getId(),userName);
        changeFlag.firePropertyChange("game",true,false);
    }

    public String playersList(){
        List<String> playersList=new ArrayList(players.values());
        return playersList.stream().collect(Collectors.joining(", "));
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
}
