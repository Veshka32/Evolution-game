package game.controller;

import game.constants.Constants;
import game.entities.Card;
import game.entities.Move;
import game.entities.Player;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.util.*;

public class GameFSM {

    private transient List<Card> cardList;
    private transient int animalID = Constants.START_CARD_INDEX.getValue();
    private transient int currentPhase; //default 0
    private int whoStartPhase; //default 0
    private transient List<String> playersTurn = new ArrayList<>();
    private transient int playerOnMoveIndex;
    private transient String error;
    private transient Move lastMove;

    //go to json
    @org.statefulj.persistence.annotations.State
    private String state;
    private String moves;
    private HashMap<String, Player> players = new HashMap<>();

    public void setMove(Move move) {
        lastMove=move;
    }

    public void addPlayer(String userName) {
        players.put(userName, new Player(userName));

//        if (isFull()) {
//            switchStatus();
//            start();
//        }
    }



    public void start() {
        createCards();
        playersTurn = new ArrayList<>(Arrays.asList(players.keySet().toArray(new String[players.size()])));
        for (String name : playersTurn)
            addCardsOnStart(players.get(name));
        playerOnMoveIndex = 0;
    }

    private void addCardsOnStart(Player player) {
        for (int i = 0; i < Constants.START_NUMBER_OF_CARDS.getValue(); i++) {
            player.addCard(cardList.remove(cardList.size() - 1));
        }
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

    public static void main(String[] args) throws TooBusyException {

        //construct the FSM
        final GameFSM test=new GameFSM();
        test.addPlayer("first");
        test.addPlayer("second");

        //define events
        final String gameIsFull = "GAME_IS_FULL";
        final String eventB = "Event B";

        //define states
        State<GameFSM> START = new StateImpl<>("START");
        State<GameFSM> EVOLUTION = new StateImpl<>("EVOLUTION");
        State<GameFSM> FEED = new StateImpl<>("FEED");
        State<GameFSM> DEAD=new StateImpl<>("DEAD");
        State<GameFSM> END=new StateImpl<>("END",true);// End State

        // In-Memory Persister
        List<State<GameFSM>> states = new LinkedList<>();
        states.add(START);
        states.add(EVOLUTION);
        states.add(FEED);
        states.add(DEAD);
        states.add(END);

        //define action
        Action<GameFSM> startGame = new StartGameAction<>(test);
        Action<GameFSM> actionB = new HelloAction("Folks");

        /* Deterministic Transitions */
        START.addTransition(gameIsFull, EVOLUTION, startGame);

        /* Non-Deterministic Transitions */
//        stateB.addTransition(eventA, new Transition<GameFSM>() {
//            public StateActionPair<GameFSM> getStateActionPair(GameFSM testFSM, String s, Object... objects) throws RetryException {
//                State<GameFSM> next = null;
//                if (test.flag) {
//                    next = stateB;
//                } else {
//                    next = stateC;
//                }
//
//                // Move to the next state without taking any action
//                //
//                return new StateActionPairImpl<GameFSM>(next, null);
//            }
//        });



        MemoryPersisterImpl<GameFSM> persister = new MemoryPersisterImpl<>(
                states,   // Set of States
                START);  // Start Stat




        FSM<GameFSM> fsm = new FSM<>("Foo FSM", persister);

        fsm.onEvent(test, gameIsFull);  // stateA(EventA) -> stateB/actionA
    }
}
