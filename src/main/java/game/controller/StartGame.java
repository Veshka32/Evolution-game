package game.controller;


import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;

public class StartGame<T> implements Action<T> {

    private GameFSM game;

    public StartGame(GameFSM game){
        this.game=game;
    }

    public void execute(T stateful,
                        String event,
                        Object ... args) throws RetryException {
        game.start();
        System.out.println("Give cards");
    }
}
