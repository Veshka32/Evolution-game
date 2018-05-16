package game.controller;

import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;

public class HelloAction<T> implements Action<T> {

    String what;

    public HelloAction(String what) {
        this.what = what;
    }

    public void execute(T stateful,
                        String event,
                        Object ... args) throws RetryException {
        System.out.println("Hello " + what);
    }
}