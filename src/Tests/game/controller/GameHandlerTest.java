package game.controller;

import game.constants.CardHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class GameHandlerTest {


    @Spy
    CardHolder cardHolder=new CardHolder();

    @InjectMocks
    GameHandler handler=new GameHandler();


    @Test
    public void createGame() {
        int popGame=handler.createGame("pop");
        int testGame=handler.createGame("test");
        handler.getGame(popGame).addPlayer("test");
    }
}