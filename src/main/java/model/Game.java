package model;

import entities.Animal;
import entities.Move;
import entities.Player;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
    private List<Animal> animals=new ArrayList<>();
    int animalID=1;

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

    public String getAnimals(){
        StringBuilder builder=new StringBuilder();
        builder.append(animals.stream().map(x->x.convertToJsonString()).collect(Collectors.joining("/")));
        String result=builder.toString();
        return result;
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

        if (move.getMove().equals("Make animal")){
            Animal animal=new Animal(animalID++);
            animals.add(animal);
        }
    }

//    public String printMoves(){
//        return moves.stream().map(Object::toString).collect(Collectors.joining("/"));
//    }

    public String convertToJsonString(String name){
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("players", playersList())
                .add("moves",lastMove)
                .add("cards", players.get(name).getCards());
        if (!animals.isEmpty())
            builder.add("animals",getAnimals());

        JsonObject json=builder.build();
        return json.toString();
    }
}
