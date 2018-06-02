package game.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import game.constants.CardHolder;
import game.constants.Constants;
import game.constants.Phase;
import game.entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Named
//@ApplicationScoped
public class Game implements Serializable {
    private transient CardHolder cardHolder;

    @OneToMany(cascade = CascadeType.ALL) //cards shared among games
    private List<Card> cardList;
    private int animalID;
    @ElementCollection(fetch = EAGER)
    private List<String> playersTurn = new LinkedList<>();
    private int round = 0;
    private int playerOnMove = round;
    private String error;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true) //no game - no animals
    private Map<Integer, Animal> animalList = new HashMap<>();
    private String winners;
    private String log="";
    @Embedded
    private ExtraMessage extraMessage;

    @ManyToMany(mappedBy = "games",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Users> users=new HashSet<>();

    //include in json
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private Phase phase = Phase.START; //package access to use in tests. Not good practice
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true) //no game - no players
    private Map<String, Player> players = new LinkedHashMap<>();
    private int food;

    public Game(){}

    @Inject
    public Game(CardHolder cardHolder){
        this.cardHolder=cardHolder;
    }

    public void clearError() {
        error = null;
    }

    public String convertToJsonString(String name) {
        Gson gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson=new Gson();
        JsonElement element = new JsonObject();
        if (error != null) {
            if (playersTurn.get(playerOnMove).equals(name)) {
                element.getAsJsonObject().addProperty("error", error);
                return gson.toJson(element);
            } else return null;
        }

        element.getAsJsonObject().add("phase", gson.toJsonTree(phase));//add object
        element.getAsJsonObject().add("players", gsonExpose.toJsonTree(players));
        element.getAsJsonObject().addProperty("food", food); //add primitive
        element.getAsJsonObject().addProperty("id",id);
        element.getAsJsonObject().addProperty("player", name);
        element.getAsJsonObject().addProperty("playersList", new ArrayList<>(players.keySet()).toString());
        element.getAsJsonObject().addProperty("log", log);

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

    public void playTailLoss(Animal predator, Animal victim) {
        extraMessage = new ExtraMessage(predator.getOwner().getName(), predator.getId(), victim.getOwner().getName(), victim.getId(), "tailLoss");
    }

    public void afterTailLoss() {
        String pl = extraMessage.getPlayerOnAttack();
        playerOnMove = playersTurn.indexOf(pl);
        extraMessage = null;
    }

    public void playMimicry(Animal predator, Animal victim, List<Integer> list) {
        extraMessage = new MimicryMessage(predator.getOwner().getName(), predator.getId(), victim.getOwner().getName(), victim.getId(), "mimicry", list);
    }

    public void afterMimicry() {
        String pl = extraMessage.getPlayerOnAttack();
        playerOnMove = playersTurn.indexOf(pl);
        extraMessage = null;
    }

    public void makeMove(Move move) {
        StringBuilder sb=new StringBuilder(log);
        sb.append("\n").append(move.getPlayer()).append(" ").append(move.getLog()).append(" at ").append(new Date());
        log=sb.toString();
        if (move.getMove().equals("EndPhase")) {
            playerEndsPhase(move.getPlayer());
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
                if (player.getCardNumber()==0) flag--;
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

    public void addPlayer(String userName){
        players.put(userName, new Player(userName));
        StringBuilder sb=new StringBuilder(log);
        sb.append(userName).append(" joined game at ").append(new Date()).append("\n");
        log=sb.toString();
        if (players.size() == Constants.NUMBER_OF_PLAYER.getValue()) {
            goToNextPhase(); //start game
        }
    }

    private void start() {
        animalID = Constants.START_CARD_INDEX.getValue();
        //cardList = new CardGenerator().getCards();
        cardList=cardHolder.getCards();
        playersTurn = new LinkedList<>(players.keySet());
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMove = 0;
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

    void addLogMessage(String...s){
        StringBuilder sb=new StringBuilder(log);
        for (String str:s
             ) {
            sb.append(str);
        }
        log=sb.toString();
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

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public boolean containsPlayer(String name){
        return players.containsKey(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public List<String> getPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(List<String> playersTurn) {
        this.playersTurn = playersTurn;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPlayerOnMove() {
        return playerOnMove;
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

    public Map<Integer, Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(HashMap<Integer, Animal> animalList) {
        this.animalList = animalList;
    }

    public String getWinners() {
        return winners;
    }

    public void setWinners(String winners) {
        this.winners = winners;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public ExtraMessage getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(ExtraMessage extraMessage) {
        this.extraMessage = extraMessage;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }
}
