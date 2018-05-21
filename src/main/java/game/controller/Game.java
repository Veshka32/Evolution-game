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
    transient HashMap<Integer, Animal> animalList = new HashMap<>();
    private transient CardGenerator generator = new CardGenerator();

    //go to json
    private String moves;
    Phase phase = Phase.START; //package access to use in tests. Not good practice
    private Map<String, Player> players = new LinkedHashMap<>();
    int food;

    public void deleteFood() {
        food--;
        if (food == 0)
            goToNextPhase();
    }

    public void makeMove(Move move) {
        error = null;
        moves = move.getPlayer() + " " + move.getLog();

        if (move.getMove().equals("Restart")) {
            restart();
            return;
        }

        try {
            switch (phase) {
                case EVOLUTION:
                    EvolutionPhase ep = new EvolutionPhase();
                    ep.playProperty(this, move);
                    break;

                case FEED:
                    FeedPhase fp = new FeedPhase();
                    fp.eat(this, move);
                    break;
            }
        } catch (GameException e) {
            error = e.getMessage();
        }
    }

    void playerEndsPhase(String name) {
        playersTurn.remove(name);
        if (playersTurn.isEmpty())
            goToNextPhase();

    }


    void switchPlayerOnMove() {
        playerOnMove = (playerOnMove + 1) % playersTurn.size(); // circular array;
    }

    void goToNextPhase() {
        switch (phase) {
            case START:
                phase = Phase.EVOLUTION;
                start();
                break;
            case EVOLUTION:
                Random r = new Random();
                food = r.nextInt(Constants.MAX_FOOD.getValue() - 1) + Constants.MIN_FOOD.getValue();
                for (Player pl:players.values()
                     ) {
                    pl.reserCurrentHungry();
                }
                phase = Phase.FEED;
                break;
            case FEED:
                phase = Phase.DEAD;
                break;
            case DEAD:
                phase = Phase.EVOLUTION;
                break;
        }
        playersTurn = new LinkedList<>(players.keySet());
        playerOnMove = 0;
    }

    public boolean onProgress() {
        return !phase.equals(Phase.START);
    }

    public String convertToJsonString(String name) {

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //with string
        element.getAsJsonObject().addProperty("playersList", new ArrayList<>(players.keySet()).toString());
        if (error != null && playersTurn.get(playerOnMove).equals(name)) {
            element.getAsJsonObject().addProperty("error", error);
        } else {
            if (playersTurn.size() > 0 && playersTurn.get(playerOnMove).equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        }

        return gson.toJson(element);
    }

    public void addPlayer(String userName) {

        players.put(userName, new Player(userName));

        if (players.size() == Constants.NUMBER_OF_PLAYER.getValue()) {
            goToNextPhase();
        }
    }

    private void start() {
        cardList = generator.getCards();
        playersTurn = new LinkedList<>(players.keySet());
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMove = 0;
    }

    private void restart() {
        cardList = generator.getCards();
        playersTurn = new LinkedList<>(players.keySet());
        for (String name : playersTurn) {
            players.replace(name, new Player(name));
            addCardsOnStart(players.get(name));
        }
        playerOnMove = 0;
        phase = Phase.EVOLUTION;
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
        Animal animal = new Animal(animalID++, player);
        player.deleteCard(move.getCardId());
        player.addAnimal(animal);
        animalList.put(animal.getId(), animal);

    }

    public Animal getAnimal(int i) {
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
