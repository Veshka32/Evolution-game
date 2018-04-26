package model;

import com.google.gson.Gson;
import entities.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class Game {
    private final int START_NUMBER_OF_CARDS = 6;
    private final int START_CARD_INDEX = 1;
    private final int NUMBER_OF_PLAYER = 2;
    private int animalID = START_CARD_INDEX;
    private int cardID = START_CARD_INDEX;

    private Phase[] phases = Phase.values();
    private int currentState; //default 0
    Phase phase = Phase.OFF;
    private int playerOnMoveIndex;
    private HashMap<String, Player> players = new HashMap<>();
    private String[] playersNames;
    private String lastMove = "New game started";
    private List<Animal> animals = new ArrayList<>();
    private int DONE_count; //default 0
    //private PropertyChangeSupport changeFlag =new PropertyChangeSupport(this);

    public void addPlayer(String userName) {
        Player player = new Player(userName);
        players.put(userName, player);
        //changeFlag.firePropertyChange("game", true, false);
    }

    public void start() {
        playersNames = players.keySet().toArray(new String[players.size()]);
        for (String name : playersNames)
            addCardsOnStart(players.get(name));
        playerOnMoveIndex = 0;
    }

    private void switchPlayerOnMove() {
        if (playerOnMoveIndex == 0)
            playerOnMoveIndex = 1;
        else playerOnMoveIndex = 0;
    }

    private void addCardsOnStart(Player player) {
        Random r = new Random();
        for (int i = 0; i < START_NUMBER_OF_CARDS; i++) {
            player.addCard(new Card(r.nextInt(3), cardID++));
        }
    }

    public String getAllPlayers(){
        String[] all =players.keySet().toArray(new String[players.size()]);
        return Arrays.toString(all);
    }

    private String getAnimals() {
        StringBuilder builder = new StringBuilder();
        builder.append(animals.stream().map(x -> x.convertToJsonString()).collect(Collectors.joining("/")));
        return builder.toString();

    }

    public boolean isFull() {
        return players.size() == NUMBER_OF_PLAYER;
    }

    public void switchStatus() {
        if (currentState == phases.length - 1)
            currentState = 1;
        else currentState++;
        phase = phases[currentState];
    }

    public void makeMove(Move move) {
        lastMove = move.toString();
        switch (move.getMove()) {
            case "Make animal":
                Player player=players.get(move.getPlayer());
                Animal animal = new Animal(animalID++,player.getName());
                animals.add(animal);

                player.deleteCard(move.getId());
                player.addAnimal(animal);
                break;
            case "Done":
                DONE_count++;
                break;
        }
        switchPlayerOnMove();
        if (DONE_count == NUMBER_OF_PLAYER) {
            switchStatus();
            DONE_count = 0;
        }
    }

    public String convertToJsonString(String name) {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("players", getAllPlayers())
                .add("phase", phase.toString())
                .add("moves", lastMove);
        if (players.get(name).hasCards())
            builder.add("cards", players.get(name).getCards());

        if (!animals.isEmpty())
            builder.add("animals", getAnimals());

        try {if (playersNames[playerOnMoveIndex].equals(name))
            builder.add("status", true);
        else builder.add("status", false);} catch (NullPointerException e){}

        JsonObject json = builder.build();
        return json.toString();
    }

    //    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        changeFlag.addPropertyChangeListener(listener);
//    }
}
