package model;

import entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.management.PlatformLoggingMXBean;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class Game {
    final int START_NUMBER_OF_CARDS=6;
    final int START_CARD_INDEX=1;
    final int NUMBER_OF_PLAYER=2;
    private Phase phase= Phase.OFF;
    private String playerOnMove;

    private HashMap<String, Player> players=new HashMap<>();
    private PropertyChangeSupport changeFlag =
            new PropertyChangeSupport(this);
    private String lastMove="New game started";
    private List<Animal> animals=new ArrayList<>();
    int animalID=START_CARD_INDEX;
    int cardID=START_CARD_INDEX;

    public void addPlayer(String userName){
        Player player=new Player(userName);
        Random r=new Random();
        for (int i=0;i<START_NUMBER_OF_CARDS;i++){
            player.addCard(new Card(r.nextInt(3),cardID++));
        }
        players.put(userName,player);
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
        return players.size()==NUMBER_OF_PLAYER;
    }

    public void setStatus(Phase status){
        phase=status;
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
                .add("status",phase.toString())
                .add("moves",lastMove)
                .add("cards", players.get(name).getCards());
        if (!animals.isEmpty())
            builder.add("animals",getAnimals());

        JsonObject json=builder.build();
        return json.toString();
    }
}
