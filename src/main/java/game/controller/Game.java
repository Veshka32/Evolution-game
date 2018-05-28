package game.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import game.constants.Constants;
import game.constants.Phase;
import game.entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

@Named
@ApplicationScoped
public class Game {

    private transient List<Card> cardList;
    private transient int animalID = Constants.START_CARD_INDEX.getValue();
    transient List<String> playersTurn = new LinkedList<>();
    transient int round = 0;
    transient int playerOnMove = round;
    transient String error;
    transient HashMap<Integer, Animal> animalList = new HashMap<>();
    private transient CardGenerator generator = new CardGenerator();
    private transient String winners;
    transient StringBuilder log = new StringBuilder();
    transient TailLossMessage tailLossMessage;

    //go to json
    private String moves;
    Phase phase = Phase.START; //package access to use in tests. Not good practice
    Map<String, Player> players = new LinkedHashMap<>();
    private int food;

    public void deleteFood() {
        food--;
    }

    public void tailLoss(Animal predator, Animal victim) {
        tailLossMessage = new TailLossMessage(predator.getOwner().getName(), predator.getId(), victim.getOwner().getName(), victim.getId());
    }

    public void afterTailLoss(){
        String pl = tailLossMessage.getPlayerOnAttack();
        playerOnMove = playersTurn.indexOf(pl);
        tailLossMessage = null;
    }

    public void makeMove(Move move) {
        error = null;
        log.append("\n").append(move.getPlayer()).append(" ").append(move.getLog()).append(" at ").append(new Date());
        if (move.getMove().equals("EndPhase")) {
            playerEndsPhase(move.getPlayer());
            return;
        } else if (move.getMove().equals("Restart")) {
            restart();
            return;
        }
        try {
            switch (phase) {
                case EVOLUTION:
                    EvolutionPhase ep = new EvolutionPhase(this, move);
                    ep.processMove();
                    break;
                case FEED:
                    FeedPhase fp = new FeedPhase(this, move);
                    fp.processMove();
                    break;
                case END:
                    break;
            }
        } catch (GameException e) {
            error = e.getMessage();
        }
    }

    private void playerEndsPhase(String name) {
        playersTurn.remove(name);
        if (playersTurn.isEmpty())
            goToNextPhase();
        else switchPlayerOnMove();
    }

    void switchPlayerOnMove() {
        playerOnMove = (playerOnMove + 1) % playersTurn.size(); // circular array;
    }

    private void goToNextPhase() {
        switch (phase) {
            case START:
                phase = Phase.EVOLUTION;
                start();
                break;
            case EVOLUTION:
                food = new Random().nextInt(Constants.MAX_FOOD.getValue() - 1) + Constants.MIN_FOOD.getValue();
                phase = Phase.FEED;
                break;
            case FEED:
                if (cardList.isEmpty()) endGame();
                else {
                    for (Player pl : players.values()
                            ) {
                        pl.animalsDie();
                        pl.resetFields();
                        pl.setCardNumber();
                    }
                    addCards();
                    phase = Phase.EVOLUTION;
                    round++;

                }
                if (cardList.isEmpty()) round = -1;//last round
                break;
        }
        playersTurn = new LinkedList<>(players.keySet());
        playerOnMove = round % players.size(); //circular array; each round starts next player
    }

    public void endGame() {
        phase = Phase.END;
        List<Player> sorted = new ArrayList<>(players.values());
        sorted.sort(Comparator.comparing(Player::getPoints).thenComparing(Player::getUsedCards).reversed());
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(sorted.get(0).getName());
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i).getPoints() < sorted.get(i - 1).getPoints()) break;
            if (sorted.get(i).getUsedCards() < sorted.get(i - 1).getUsedCards()) break;
            joiner.add(sorted.get(i).getName()); //append another winners if points and usedCard are not less;
        }
        winners = joiner.toString();
    }

    public void addCards() {

        while (!cardList.isEmpty()) {
            int flag = players.size();
            for (Player player : players.values()) {
                if (!player.needCards()) flag--;
                else
                    player.addCard(cardList.remove(cardList.size() - 1));
                if (cardList.isEmpty()) break;
            }
            if (flag == 0) break;
        }
    }

    public boolean onProgress() {
        return !phase.equals(Phase.START);
    }

    public int getFood() {
        return food;
    }

    public void setFood(int i) {
        food = i;
    }

    public String convertToJsonString(String name) {

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        element.getAsJsonObject().addProperty("player", name); //add primitive
        element.getAsJsonObject().addProperty("playersList", new ArrayList<>(players.keySet()).toString());
        element.getAsJsonObject().addProperty("log", log.toString());
        if (error != null && playersTurn.get(playerOnMove).equals(name)) {
            element.getAsJsonObject().addProperty("error", error);
            element.getAsJsonObject().addProperty("status", true);
        } else {
            if (playersTurn.size() > 0 && playersTurn.get(playerOnMove).equals(name))
                element.getAsJsonObject().addProperty("status", true);
            else element.getAsJsonObject().addProperty("status", false);
        }

        if (tailLossMessage != null)
            element.getAsJsonObject().add("tailLoss", new Gson().toJsonTree(tailLossMessage)); //add object


        if (phase.equals(Phase.END)) element.getAsJsonObject().addProperty("winners", winners);
        if (round == -1) element.getAsJsonObject().addProperty("last", 0);

        return gson.toJson(element);
    }

    public void addPlayer(String userName) {
        players.put(userName, new Player(userName));
        log.append(userName).append(" joined game at ").append(new Date());
        if (players.size() == Constants.NUMBER_OF_PLAYER.getValue()) {
            goToNextPhase(); //start game
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

    public void feedScavenger(String name) {
        List<String> scavengerOwners = new ArrayList<String>(players.keySet());
        int start = 0;
        for (int i = 0; i < scavengerOwners.size(); i++) {
            if (scavengerOwners.get(i).equals(name)) {
                start = i;
                break;
            }
        }
        for (int i = start; i < scavengerOwners.size() + start; i++) {
            int k = i % scavengerOwners.size(); //circular array
            Player player = players.get(scavengerOwners.get(k));
            if (player.feedScavenger())
                break;
        }
    }

    void makeAnimal(Move move) {
        Player player = players.get(move.getPlayer());
        Animal animal = new Animal(animalID++, player);
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
