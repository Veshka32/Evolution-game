package game.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import game.entities.*;
import game.constants.Constants;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

@Named
@ApplicationScoped
public class Game {

    private transient int animalID = Constants.START_CARD_INDEX.getValue();
    private transient int cardID = Constants.START_CARD_INDEX.getValue();
    private transient Phase[] phases = Phase.values();
    private transient int currentState; //default 0
    private transient int playerOnMoveIndex;
    private transient HashMap<String, Player> playerHashMap = new HashMap<>();
    private transient String[] playersNames;
    private transient List<Animal> animalList;
    private transient List<Card> cardList;
    private transient int DONE_count; //default 0

    //go to json
    private String moves = "New game started";
    Phase phase = Phase.OFF;

    public boolean containsPlayer(String name) {
        return playerHashMap.containsKey(name);
    }

    public String convertToJsonString(String name) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //with string
        element.getAsJsonObject().addProperty("players", getAllPlayers());
        element.getAsJsonObject().add("cards", playerHashMap.get(name).getCards()); //with json array
        element.getAsJsonObject().add("animals", getAnimalList());

        try {
            if (playersNames[playerOnMoveIndex].equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        } catch (NullPointerException e) {
        }

        return gson.toJson(element);

    }

    public void addPlayer(String userName) {
        playerHashMap.put(userName, new Player(userName));
        if (isFull()) {
            switchStatus();
            start();
        }
    }

    public void deletePlayer(String userName) {
        playerHashMap.remove(userName);
        moves = userName + " has left the game";
        phase = Phase.OFF;
    }

    public void start() {
        createCards();
        animalList=new ArrayList<>();
        playersNames = playerHashMap.keySet().toArray(new String[playerHashMap.size()]);
        for (String name : playersNames)
            addCardsOnStart(playerHashMap.get(name));
        playerOnMoveIndex = 0;
    }

    private void switchPlayerOnMove() {
        playerOnMoveIndex=(playerOnMoveIndex==0) ? 1 : 0;
    }

    private void addCardsOnStart(Player player) {
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++) {
            player.addCard(cardList.remove(cardList.size() - 1));
        }
    }

    public String getAllPlayers() {
        String[] all = playerHashMap.keySet().toArray(new String[playerHashMap.size()]);
        return Arrays.toString(all);
    }

    private JsonArray getAnimalList() {
        Gson json = new Gson();
        JsonElement element = json.toJsonTree(animalList, new TypeToken<List<Animal>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public boolean isFull() {
        return playerHashMap.size() == Constants.NUMBER_OF_PLAYER.getValue();
    }

    public void setMoves(String s) {
        moves = s;
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
            case "MakeAnimal":
                makeAnimal(move);
                break;
            case "EndPhase":
                DONE_count++;
                break;
            case "PlayProperty":
                playProperty(move);
                break;
        }

        switchPlayerOnMove();
        if (DONE_count == Constants.NUMBER_OF_PLAYER.getValue()) {
            switchStatus();
            DONE_count = 0;
        }
    }

    public void playProperty(Move move){
        Player player=playerHashMap.get(move.getPlayer());
        player.deleteCard(move.getCardId());
        Animal animal=player.getAnimal(move.getAnimalId());
        animal.addProperty(move.getProperty());

    }

    public void makeAnimal(Move move){
        Player player = playerHashMap.get(move.getPlayer());
        Animal animal = new Animal(animalID++, player.getName());

        animalList.add(animal);
        animalList.sort(Comparator.comparing(Animal::getOwner));
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
        Game game=new Game();
        game.addPlayer("test");
        game.start();
        String str=game.convertToJsonString("test");
        System.out.println(str);
    }
}
