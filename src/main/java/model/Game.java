package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class Game {

    private int animalID = Constants.START_CARD_INDEX.getValue();
    private int cardID = Constants.START_CARD_INDEX.getValue();
    private Phase[] phases = Phase.values();
    private int currentState; //default 0
    Phase phase = Phase.OFF;
    private int playerOnMoveIndex;
    private HashMap<String, Player> playerHashMap = new HashMap<>();
    private String[] playersNames;
    private String moves = "New game started";
    private List<Animal> animals = new ArrayList<>();
    private int DONE_count; //default 0
    //private PropertyChangeSupport changeFlag =new PropertyChangeSupport(this);

    public String convertToJsonString(String name) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("players", getAllPlayers());
        element.getAsJsonObject().add("cards", playerHashMap.get(name).getCards());
        element.getAsJsonObject().add("animals", getAnimals());

        try {
            if (playersNames[playerOnMoveIndex].equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        } catch (NullPointerException e) {
        }

        String result = gson.toJson(element);
        return result;

    }

    public void addPlayer(String userName) {
        Player player = new Player(userName);
        playerHashMap.put(userName, player);
        //changeFlag.firePropertyChange("game", true, false);
    }

    public void start() {
        playersNames = playerHashMap.keySet().toArray(new String[playerHashMap.size()]);
        for (String name : playersNames)
            addCardsOnStart(playerHashMap.get(name));
        playerOnMoveIndex = 0;
    }

    private void switchPlayerOnMove() {
        if (playerOnMoveIndex == 0)
            playerOnMoveIndex = 1;
        else playerOnMoveIndex = 0;
    }

    private void addCardsOnStart(Player player) {
        Random r = new Random();
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++) {
            player.addCard(new Card(r.nextInt(3), cardID++));
        }
    }

    public String getAllPlayers() {
        String[] all = playerHashMap.keySet().toArray(new String[playerHashMap.size()]);
        return Arrays.toString(all);
    }

    private JsonArray getAnimals() {
        Gson json = new Gson();
        JsonElement element = json.toJsonTree(animals, new TypeToken<List<Animal>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
        //StringBuilder builder = new StringBuilder();
// builder.append(animals.stream().map(x -> x.convertToJsonString()).collect(Collectors.joining("/")));
//return builder.toString();

    }

    public boolean isFull() {
        return playerHashMap.size() == Constants.NUMBER_OF_PLAYER.getValue();
    }

    public void setMoves(String s){
        moves=s;
    }

    public void switchStatus() {
        if (currentState == phases.length - 1)
            currentState = 1;
        else currentState++;
        phase = phases[currentState];
    }

    public void makeMove(Move move) {
        moves = move.toString();
        switch (move.getMove()) {
            case "Make animal":
                Player player = playerHashMap.get(move.getPlayer());
                Animal animal = new Animal(animalID++, player.getName());
                animals.add(animal);

                player.deleteCard(move.getId());
                player.addAnimal(animal);
                break;
            case "Done":
                DONE_count++;
                break;
        }
        switchPlayerOnMove();
        if (DONE_count == Constants.NUMBER_OF_PLAYER.getValue()) {
            switchStatus();
            DONE_count = 0;
        }
    }


    //    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        changeFlag.addPropertyChangeListener(listener);
//    }
    public static void main(String[] args) {
        Game g = new Game();
        g.addPlayer("test");
        g.addPlayer("admin");
        g.start();
        System.out.println(g.convertToJsonString("test"));
        System.out.println(g.convertToJsonString("admin"));
    }
}
