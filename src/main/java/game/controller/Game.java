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
    transient int playerOnMove;
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

        if (move.getMove().equals("Restart")) {restart();return;}

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

    void playerEndsPhase(String name) {
        playersTurn.remove(name);
        if (playersTurn.isEmpty()) {
            goToNextPhase();
            return;
        }
        playerOnMove = playerOnMove % playersTurn.size(); //if name was last in array, after array becomes smaller, go to ind 0
    }


    void switchPlayerOnMove() {
        playerOnMove = (playerOnMove + 1) % Constants.NUMBER_OF_PLAYER.getValue(); // circular array
    }

    void goToNextPhase() {
        switch (phase) {
            case START:
                phase = Phase.EVOLUTION;
                start();
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
        playerOnMove = 0;
    }

    public boolean onProgress(){
        return !phase.equals(Phase.START);
    }

    public String convertToJsonString(String name) {

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //with string
        if (error != null && playersTurn.get(playerOnMove).equals(name)) {
            element.getAsJsonObject().addProperty("error", error);
        } else {
            if (playersTurn.size()>0 && playersTurn.get(playerOnMove).equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        }

        return gson.toJson(element);
    }

    public void addPlayer(String userName) {
        playersList[playerOnMove++] = userName;
        players.put(userName, new Player(userName));

        if (players.size() == Constants.NUMBER_OF_PLAYER.getValue()) {
            goToNextPhase();
        }
    }

    private void start() {
        cardList = generator.getCards();
        playersTurn = new LinkedList<>(Arrays.asList(Arrays.copyOf(playersList, playersList.length)));
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMove = 0;
    }

    private void restart(){
        cardList = generator.getCards();
        playersTurn = new LinkedList<>(Arrays.asList(Arrays.copyOf(playersList, playersList.length)));
        for (String name : playersTurn){
            players.replace(name,new Player(name));
            addCardsOnStart(players.get(name));}
        playerOnMove = 0;
    }

    private void addCardsOnStart(Player player) {
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++) {
            player.addCard(cardList.remove(cardList.size() - 1));
        }
    }


    public void setMoves(String s) {
        moves = s;
    }

    void makeAnimal(Move move) {
        Player player = players.get(move.getPlayer());
        Animal animal = new Animal(animalID++, player.getName());
        player.deleteCard(move.getCardId());
        player.addAnimal(animal);
        animalList.put(animal.getId(), animal);
    }

    Animal getAnimal(int i) {
        return animalList.get(i);
    }

    public String getAllPlayers() {
        return new ArrayList<>(players.keySet()).toString();
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    void setGenerator(CardGenerator generator) {
        this.generator = generator;
    }

    public void deletePlayer(String userName) {
        players.remove(userName);
        moves = userName + "left the game";
        phase = Phase.START;
    }

}
