package game.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import game.constants.Constants;
import game.constants.Phase;
import game.entities.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;

@Entity
public class Game implements Serializable {

    private transient String winners;
    private transient String lastLogMessage;

    private int numberOfPlayers=2;
    private int animalID;
    private int round = 0;
    private int playerOnMove = round;
    private String error;
    private StringBuilder log = new StringBuilder();

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Card> cardList;

//    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
//    private Set<Users> users=new HashSet<>();

    @ElementCollection(fetch = EAGER)
    @OrderColumn(name = "order_index")
    private List<String> playersTurn = new ArrayList<>();

    @Embedded
    private ExtraMessage extraMessage;

    //include in json
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int food;

    @Enumerated(EnumType.STRING)
    private Phase phase = Phase.START;

    @OneToMany(cascade = CascadeType.ALL) //no game - no players //orphanremoval=true
    private Map<String, Player> players = new HashMap<>();

    public Game() {
    }

    void setNumberOfPlayers(int n){
        numberOfPlayers =n;
    }

    void addPlayer(String userName) {
        players.put(userName, new Player(userName));
        lastLogMessage=userName + " joined game at " + new Date() + "\n";
        log.append(lastLogMessage);
    }

    boolean isFull() {
        return players.size() == numberOfPlayers;
    }

    void start() {
        animalID = Constants.START_CARD_INDEX.getValue();
        resetPlayersTurn();
        players.forEach((k, v) -> addCardsOnStart(v));
        playerOnMove = 0;
        phase = Phase.EVOLUTION;
    }

    private void resetPlayersTurn() {
        playersTurn = new ArrayList<>(players.keySet());
        playersTurn.sort(Comparator.comparingInt(s -> players.get(s).getId()));
    }

    public void clearError() {
        error = null;
    }

    public String errorToJson(String name,JsonElement element,Gson gson){
        if (playersTurn.get(playerOnMove).equals(name)) {
            element.getAsJsonObject().addProperty("error", error);
            return gson.toJson(element);
        } else return null;
    }

    public String getFullJson(String name) {
        Gson gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        if (error != null) {
            return errorToJson(name,element,gson);
        }

        element.getAsJsonObject().add("phase", gson.toJsonTree(phase));//add object
        element.getAsJsonObject().add("players", gsonExpose.toJsonTree(players));
        element.getAsJsonObject().addProperty("food", food); //add primitive
        element.getAsJsonObject().addProperty("id", id);
        element.getAsJsonObject().addProperty("player", name);
        element.getAsJsonObject().addProperty("playersList", new ArrayList<>(players.keySet()).toString());
        element.getAsJsonObject().addProperty("log", log.toString());
        element.getAsJsonObject().add("cards",gson.toJsonTree(players.get(name).getCards()));

        if (playersTurn.size() > 0 && playersTurn.get(playerOnMove).equals(name))
            element.getAsJsonObject().addProperty("status", true);
        else element.getAsJsonObject().addProperty("status", false);

        if (extraMessage != null)
            element.getAsJsonObject().add(extraMessage.getType(), gson.toJsonTree(extraMessage));

        if (phase.equals(Phase.END)) element.getAsJsonObject().addProperty("winners", winners);
        if (round == -1) element.getAsJsonObject().addProperty("last", 0);

        return gson.toJson(element);
    }

    public String getLightWeightJson(String name) {
        Gson gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        if (error != null) {
            return errorToJson(name,element,gson);
        }
        element.getAsJsonObject().add("phase", gson.toJsonTree(phase));//add object
        element.getAsJsonObject().add("players", gsonExpose.toJsonTree(players));
        element.getAsJsonObject().addProperty("log", lastLogMessage);

        if (phase.equals(Phase.EVOLUTION)) element.getAsJsonObject().add("cards",gson.toJsonTree(players.get(name).getCards()));
        if (phase.equals(Phase.FEED)) element.getAsJsonObject().addProperty("food", food); //add primitive

        if (playersTurn.size() > 0 && playersTurn.get(playerOnMove).equals(name))
            element.getAsJsonObject().addProperty("status", true);
        else element.getAsJsonObject().addProperty("status", false);

        if (extraMessage != null)
            element.getAsJsonObject().add(extraMessage.getType(), gson.toJsonTree(extraMessage));

        if (phase.equals(Phase.END)) element.getAsJsonObject().addProperty("winners", winners);
        if (round == -1) element.getAsJsonObject().addProperty("last", 0);
        return gson.toJson(element);
    }

    public void deleteFood() {
        food--;
    }

    void playTailLoss(Animal predator, Animal victim) {
        extraMessage = new ExtraMessage(predator.getOwner().getName(), predator.getId(), victim.getOwner().getName(), victim.getId(), "tailLoss");
    }

    void afterTailLoss() {
        String pl = extraMessage.getPlayerOnAttack();
        playerOnMove = playersTurn.indexOf(pl);
        extraMessage = null;
    }

    void playMimicry(Animal predator, Animal victim, List<Integer> list) {
        extraMessage = new MimicryMessage(predator.getOwner().getName(), predator.getId(), victim.getOwner().getName(), victim.getId(), "mimicry", list);
    }

    void afterMimicry() {
        String pl = extraMessage.getPlayerOnAttack();
        playerOnMove = playersTurn.indexOf(pl);
        extraMessage = null;
    }

    public void makeMove(Move move) {
        lastLogMessage="\n" + move.getPlayer() + " " + move.getLog() + " at " + new Date();
        log.append(lastLogMessage);
        if (move.getMove().equals("saveGame")) return;
        switch (move.getMove()) {
            case "EndPhase":
                playerEndsPhase(move.getPlayer());
                return;
            case "Leave game": //only log updates
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

    void goToNextPhase() {
        switch (phase) {
            case EVOLUTION:
                food = ThreadLocalRandom.current().nextInt(Constants.FOOD.minFoodFor(numberOfPlayers), Constants.FOOD.maxFoodFor(numberOfPlayers) + 1);
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
        resetPlayersTurn();
        playerOnMove = round % players.size(); //circular array; each round starts next player
    }

    private void endGame() {
        phase = Phase.END;
        List<Player> sorted=new ArrayList<>(players.values());
        sorted.sort(Comparator.comparing(Player::getPoints).thenComparing(Player::getUsedCards).reversed());
        winners=sorted.stream().map(Player::finalPoints).collect(Collectors.joining("\n"));
    }

    public boolean isEnd(){
        return phase.equals(Phase.END);
    }

    private void addCards() {

        while (!cardList.isEmpty()) {
            int flag = players.size();
            for (Player player : players.values()) {
                if (player.getCardNumber() == 0) flag--;
                else
                    player.addCard(cardList.remove(cardList.size() - 1));
                if (cardList.isEmpty()) break;
            }
            if (flag == 0) break;
        }
    }

    boolean onProgress() {
        return !phase.equals(Phase.START);
    }

    public int getFood() {
        return food;
    }

    public void setFood(int i) {
        food = i;
    }

    private void addCardsOnStart(Player player) {
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++)
            player.addCard(cardList.remove(cardList.size() - 1));

    }

    void feedScavenger(String name) {
        List<String> scavengerOwners = new ArrayList<>(players.keySet());
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

    void addLogMessage(String... s) {
        StringBuilder sb = new StringBuilder(log);
        for (String str : s)
            sb.append(str);

        lastLogMessage = sb.toString();
        log.append(lastLogMessage);
    }

    void makeAnimal(Move move) {
        Player player = players.get(move.getPlayer());
        Animal animal = new Animal(animalID++, player);
        player.deleteCard(move.getCardId());
        player.addAnimal(animal);
        //animalList.put(animal.getId(), animal);
    }

    Animal getAnimal(int i) {
        for (Player player : players.values()
                ) {
            if (player.getAnimals().containsKey(i))
                return player.getAnimals().get(i);
        }
        return null;
    }

    Player getPlayer(String name) {
        return players.get(name);
    }

    boolean containsPlayer(String name) {
        return players.containsKey(name);
    }

    List<String> getPlayersTurn() {
        return playersTurn;
    }

    int getPlayerOnMove() {
        return playerOnMove;
    }

    Phase getPhase() {
        return phase;
    }

    void setPhase(Phase phase) {
        this.phase = phase;
    }

    void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    List<Card> getCardList() {
        return cardList;
    }

    int getRound() {
        return round;
    }

    ExtraMessage getExtraMessage() {
        return extraMessage;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public void setPlayersTurn(List<String> playersTurn) {
        this.playersTurn = playersTurn;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setPlayerOnMove(int playerOnMove) {
        this.playerOnMove = playerOnMove;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getWinners() {
        return winners;
    }

    public void setWinners(String winners) {
        this.winners = winners;
    }

    public void setExtraMessage(ExtraMessage extraMessage) {
        this.extraMessage = extraMessage;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    @Override
    public String toString(){
        return "#"+id+", players: "+players.values().stream().map(Player::getName).collect(Collectors.joining(", "));
    }
}
