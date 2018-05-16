package game.controller;


import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;

public class StartGameAction<T> implements Action<T> {

    private GameFSM game;

    public StartGameAction(GameFSM game){
        this.game=game;
    }

    public void execute(T stateful,
                        String event,
                        Object ... args) throws RetryException {
        game.start();
        System.out.println("Give cards");
    }
}
