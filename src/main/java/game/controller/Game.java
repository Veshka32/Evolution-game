package game.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import game.constants.Phase;
import game.entities.*;
import game.constants.Constants;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

@Named
@ApplicationScoped
public class Game {

    private transient List<Card> cardList;
    private transient int animalID = Constants.START_CARD_INDEX.getValue();
    private transient Phase[] phases = Phase.values();
    private transient int currentPhase; //default 0
    private transient int whoStartPhase; //default 0
    private transient List<String> playersTurn = new ArrayList<>();
    private transient int playerOnMoveIndex;
    private transient String error;
    private transient EvolutionPhase evolutionPhase = new EvolutionPhase();

    //go to json
    private String moves;
    private Phase phase = Phase.START;
    private HashMap<String, Player> players = new HashMap<>();

    public void makeMove(Move move) {
        error = null;
        moves = move.toString();
        switch (phase) {
            case EVOLUTION:
                try {
                    evolutionPhase.playProperty(this, move);
                } catch (GameException e) {
                    error = e.getMessage();
                }
        }
    }

    public void playerEndsPhase(String name) {
        playersTurn.remove(name);
    }

    public boolean isPhaseEnded() {
        return playersTurn.isEmpty();
    }

    public void goToNextPhase() {
        if (currentPhase == phases.length - 1)
            currentPhase = 1;
        else currentPhase++;
        phase = phases[currentPhase];
        playersTurn = new ArrayList<>(Arrays.asList(players.keySet().toArray(new String[players.size()])));
    }

    public boolean containsPlayer(String name) {
        return players.containsKey(name);
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public String convertToJsonString(String name) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //with string
        if (error != null) {
            element.getAsJsonObject().addProperty("error", error);
        }
        element.getAsJsonObject().addProperty("playersList",Arrays.asList(players.keySet().toArray(new String[players.size()])).toString());


        if (playersTurn.contains(name) && playersTurn.get(playerOnMoveIndex).equals(name))
            element.getAsJsonObject().addProperty("status", true);
        else element.getAsJsonObject().addProperty("status", false);

        return gson.toJson(element);
    }

    public void addPlayer(String userName) {
        players.put(userName, new Player(userName));

        if (isFull()) {
            goToNextPhase();
            start();
        }
    }

    public void deletePlayer(String userName) {
        players.remove(userName);
        moves = userName + " has left the game";
        phase = Phase.START;
    }

    public void start() {
        createCards();
        playersTurn = new ArrayList<>(Arrays.asList(players.keySet().toArray(new String[players.size()])));
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMoveIndex = 0;
    }

    public void switchPlayerOnMove() {
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


    public void makeAnimal(Move move) {
        Player player = players.get(move.getPlayer());
        Animal animal = new Animal(animalID++, player.getName());
        player.deleteCard(move.getCardId());
        player.addAnimal(animal);
    }

    private void createCards() {
        int cardID = Constants.START_CARD_INDEX.getValue();
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
}
