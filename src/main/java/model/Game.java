package model;

import entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class Game {
    final int START_NUMBER_OF_CARDS = 6;
    final int START_CARD_INDEX = 1;
    final int NUMBER_OF_PLAYER = 2;
    private Phase phase = Phase.OFF;
    private String playerOnMove;

    private HashMap<String, Player> players = new HashMap<>();
    private String[] playersNames;
    //private PropertyChangeSupport changeFlag =new PropertyChangeSupport(this);
    private String lastMove = "New game started";
    private List<Animal> animals = new ArrayList<>();
    int animalID = START_CARD_INDEX;
    int cardID = START_CARD_INDEX;

    public void addPlayer(String userName) {
        Player player = new Player(userName);
        players.put(userName, player);
        //changeFlag.firePropertyChange("game", true, false);
    }

    public void start() {
        playersNames  = players.keySet().toArray(new String[players.size()]);
        for (String name : playersNames)
            addCardsOnStart(players.get(name));
    }

    void addCardsOnStart(Player player) {
        Random r = new Random();
        for (int i = 0; i < START_NUMBER_OF_CARDS; i++) {
            player.addCard(new Card(r.nextInt(3), cardID++));
        }
    }

    public String playersList() {
        return Arrays.toString(playersNames);
    }

    public String getAnimals() {
        StringBuilder builder = new StringBuilder();
        builder.append(animals.stream().map(x -> x.convertToJsonString()).collect(Collectors.joining("/")));
        String result = builder.toString();
        return result;
    }

    public boolean isFull() {
        return players.size() == NUMBER_OF_PLAYER;
    }

    public void setStatus(Phase status) {
        phase = status;
    }

    public void makeMove(Move move) {
        lastMove = move.toString();

        if (move.getMove().equals("Make animal")) {
            Animal animal = new Animal(animalID++);
            animals.add(animal);
        }
    }

    public String convertToJsonString(String name) {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("players", playersNames.toString())
                .add("status", phase.toString())
                .add("moves", lastMove);
        if (players.get(name).hasCards())
            builder.add("cards", players.get(name).getCards());
        if (!animals.isEmpty())
            builder.add("animals", getAnimals());

        JsonObject json = builder.build();
        return json.toString();
    }

    //    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        changeFlag.addPropertyChangeListener(listener);
//    }
}
