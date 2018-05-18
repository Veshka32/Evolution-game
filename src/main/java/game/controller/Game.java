package game.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import game.constants.Constants;
import game.constants.Phase;
import game.entities.Animal;
import game.entities.Card;
import game.entities.Move;
import game.entities.Player;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

@Named
@ApplicationScoped
public class Game {

    private transient List<Card> cardList;
    private transient int animalID = Constants.START_CARD_INDEX.getValue();
    private transient int whoStartPhase; //default 0
    transient List<String> playersTurn = new LinkedList<>();
    transient int playerOnMoveIndex;
    transient String error;
    private transient HashMap<Integer, Animal> animalList = new HashMap<>();
    private transient CardGenerator generator = new CardGenerator();

    //go to json
    private String moves;
    Phase phase = Phase.START; //package access to use in tests. Not good practice
    private HashMap<String, Player> players = new HashMap<>();
    private final String[] playersList = new String[Constants.NUMBER_OF_PLAYER.getValue()];

    public void makeMove(Move move) {
        error = null;
        moves = move.getPlayer() + " " + move.getLog();
        switch (phase) {
            case EVOLUTION:
                try {
                    EvolutionPhase ep = new EvolutionPhase();
                    ep.playProperty(this, move);
                } catch (GameException e) {
                    error = e.getMessage();
                }
        }
    }

    public void playerEndsPhase(String name) {
        playersTurn.remove(name);
        if (playersTurn.isEmpty()) {goToNextPhase();return;}
        playerOnMoveIndex=playerOnMoveIndex%playersTurn.size(); //if name was last in array, after array becomes smaller, go to ind 0
    }


    public void switchPlayerOnMove() {
        playerOnMoveIndex = (playerOnMoveIndex + 1) % Constants.NUMBER_OF_PLAYER.getValue(); // circular array
    }

    public void goToNextPhase() {
        switch (phase) {
            case START:
                phase = Phase.EVOLUTION;
                break;
            case EVOLUTION:
                phase = Phase.FEED;
                break;
            case FEED:
                phase = Phase.DEAD;
                break;
            case DEAD:
                phase = Phase.EVOLUTION;
                break;
        }
        playersTurn = new LinkedList<>(Arrays.asList(players.keySet().toArray(new String[players.size()])));
        playerOnMoveIndex = 0;
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public String convertToJsonString(String name) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //with string
        if (error != null && playersTurn.get(playerOnMoveIndex).equals(name)) {
            element.getAsJsonObject().addProperty("error", error);
        } else {
            if (playersTurn.get(playerOnMoveIndex).equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        }

        return gson.toJson(element);
    }

    public void addPlayer(String userName) {
        playersList[playerOnMoveIndex++] = userName;
        players.put(userName, new Player(userName));

        if (isFull()) {
            goToNextPhase();
            start();
        }
    }

    public void deletePlayer(String userName) {
        players.remove(userName);
        moves = userName + " hasAnimal left the game";
        phase = Phase.START;
    }

    public void start() {
        cardList = generator.getCards();
        playersTurn = new LinkedList<>(Arrays.asList(Arrays.copyOf(playersList, playersList.length)));
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMoveIndex = 0;
    }

    private void addCardsOnStart(Player player) {
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++) {
            player.addCard(cardList.remove(cardList.size() - 1));
        }
    }

    public String getAllPlayers() {
        return new ArrayList<>(players.keySet()).toString();
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
        animalList.put(animal.getId(), animal);
    }

    public Animal getAnimal(int i) {
        return animalList.get(i);
    }

    public void setGenerator(CardGenerator generator) {
        this.generator = generator;
    }

}
