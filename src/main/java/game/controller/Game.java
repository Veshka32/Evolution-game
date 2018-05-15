package game.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import game.entities.*;
import game.constants.Constants;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import java.util.*;

@Named
@ApplicationScoped
public class Game {

    private transient List<Card> cardList;
    private transient int animalID = Constants.START_CARD_INDEX.getValue();
    private transient int cardID = Constants.START_CARD_INDEX.getValue();
    private transient Phase[] phases = Phase.values();
    private transient int currentState; //default 0
    private transient String[] playersTurn;
    private transient int playerOnMoveIndex;
    private transient int DONE_count; //default 0
    private transient String error;

    //go to json
    private String moves;
    private Phase phase = Phase.OFF;
    private HashMap<String, Player> players = new HashMap<>();

    public boolean containsPlayer(String name) {
        return players.containsKey(name);
    }

    public String convertToJsonString(String name) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //with string
        if (error!=null){
            element.getAsJsonObject().addProperty("error",error);
            error=null;
        }

        try {
            if (playersTurn[playerOnMoveIndex].equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        } catch (NullPointerException e) {
        }

        return gson.toJson(element);
    }

    public void generateError(String errorText) {
        JsonObject jo = Json.createObjectBuilder()
                .add("Error", errorText)
                .build();
        error=jo.toString();
    }

    public void addPlayer(String userName) {
        players.put(userName, new Player(userName));

        if (isFull()) {
            switchStatus();
            start();
        }
    }

    public void deletePlayer(String userName) {
        players.remove(userName);
        moves = userName + " has left the game";
        phase = Phase.OFF;
    }

    public void start() {
        createCards();
        playersTurn = players.keySet().toArray(new String[players.size()]);
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMoveIndex = 0;
    }

    private void switchPlayerOnMove() {
        playerOnMoveIndex = (playerOnMoveIndex == 0) ? 1 : 0;
    }

    private void addCardsOnStart(Player player) {
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++) {
            player.addCard(cardList.remove(cardList.size() - 1));
        }
    }

    public String getAllPlayers() {
        return Arrays.toString(players.keySet().toArray(new String[players.size()]));
    }

    public boolean isFull() {
        return players.size() == Constants.NUMBER_OF_PLAYER.getValue();
    }

    public void setMoves(String s) {
        moves = s;
    }

    public void evolution(Move move) {
        try{
        switch (move.getMove()) {
            case "EndPhase":
                DONE_count++;
                break;
            case "PlayProperty":
                    evolutionPlayProperty(move);
                break;
        }
        switchPlayerOnMove();
        if (DONE_count == Constants.NUMBER_OF_PLAYER.getValue()) {
            switchStatus();
            DONE_count = 0;
        }}
        catch (GameException e) {
            error=e.getMessage();
        }
    }

    public void switchStatus() {
        if (currentState == phases.length - 1)
            currentState = 1;
        else currentState++;
        phase = phases[currentState];
    }

    public void makeMove(Move move) {
        moves = move.toString();
        switch (phase) {
            case EVOLUTION:
                evolution(move);
        }
    }

    public void evolutionPlayProperty(Move move) throws GameException {
        if (move.getProperty().equals("MakeAnimal")) makeAnimal(move);
        else {
            Player player = players.get(move.getPlayer());
            Animal animal = player.getAnimal(move.getAnimalId());
            if (animal == null) {
                throw new GameException("this is not your animal");
            }
            player.deleteCard(move.getCardId());
            animal.addProperty(move.getProperty());
        }
    }

    public void makeAnimal(Move move) {
        Player player = players.get(move.getPlayer());
        Animal animal = new Animal(animalID++, player.getName());
        player.deleteCard(move.getCardId());
        player.addAnimal(animal);
    }

    private void createCards() {
        cardList = new ArrayList<>(Constants.TOTAL_NUMBER_OF_CARDS.getValue());
        for (int i = 0; i < 4; i++) {
            cardList.add(new Card(cardID++, "Camouflage"));
            cardList.add(new Card(cardID++, "Burrowing"));
            cardList.add(new Card(cardID++, "Sharp Vision"));
            cardList.add(new Card(cardID++, "Symbiosis"));
            cardList.add(new Card(cardID++, "Piracy"));
            cardList.add(new Card(cardID++, "Grazing"));
            cardList.add(new Card(cardID++, "Tail loss"));
            cardList.add(new Card(cardID++, "Hibernation"));
            cardList.add(new Card(cardID++, "Poisonous"));
            cardList.add(new Card(cardID++, "Communication"));
            cardList.add(new Card(cardID++, "Scavenger"));
            cardList.add(new Card(cardID++, "Running"));
            cardList.add(new Card(cardID++, "Mimicry"));
            cardList.add(new Card(cardID++, "Parasite", "Predator"));
            cardList.add(new Card(cardID++, "Parasite", "Fat"));
            cardList.add(new Card(cardID++, "Cooperation", "Predator"));
            cardList.add(new Card(cardID++, "Cooperation", "Fat"));
            cardList.add(new Card(cardID++, "Big", "Predator"));
            cardList.add(new Card(cardID++, "Big", "Fat"));
            cardList.add(new Card(cardID++, "Swimming"));
            cardList.add(new Card(cardID++, "Swimming"));
        }
        Collections.shuffle(cardList);
    }

    public static void main(String[] args) {
        Game game = new Game();
        //Move move=new Move("test",1,"MakeAnimal");
        game.addPlayer("test");
        game.addPlayer("pop");
        game.start();
        //game.makeMove(move);
        String str = game.convertToJsonString("test");
        System.out.println(str);
    }
}
