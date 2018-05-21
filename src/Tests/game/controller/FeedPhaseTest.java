package game.controller;

import game.constants.Constants;
import game.constants.Phase;
import game.entities.Card;
import game.entities.Move;
import game.entities.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedPhaseTest {

    String player1 = "pop";
    String player2 = "test";
    int cardIndex = 84;
    int cardIndex2 = cardIndex - Constants.START_NUMBER_OF_CARDS.getValue();

    public class TestCardGenerator extends CardGenerator {
        @Override
        public List<Card> getCards() {
            return cardList; //no shuffle
        }
    }

    @Test
    public void feedPhase() {
        Game game = new Game();
        game.setGenerator(new TestCardGenerator());
        game.addPlayer(player1);
        game.addPlayer(player2);

        //players make moves
        game.makeMove(new Move("pop", cardIndex--, 0, 0, "MakeAnimal", null, null));
        game.makeMove(new Move("test", cardIndex2--, 0, 0, "MakeAnimal", null, null));
        game.makeMove(new Move("pop", cardIndex--, 0, 0, "MakeAnimal", null, null));
        game.makeMove(new Move("test", cardIndex2--, 0, 0, "MakeAnimal", null, null));
        game.makeMove(new Move("pop", 0, 0, 0, "EndPhase", null, null));
        game.makeMove(new Move("test", 0, 0, 0, "EndPhase", null, null));
        assert (game.phase.equals(Phase.FEED));

        // food=2;
        game.food=2;
        game.makeMove(new Move("pop",0,1,0,"eatFood",null,null));
        game.makeMove(new Move("test",0,2,0,"eatFood",null,null));
        assert (game.phase.equals(Phase.DEAD));

        //food=8;
        game.phase=Phase.FEED;
        game.food=8;
        for (Player pl : game.players.values()
                ) {
            pl.resetCurrentHungry();
        }
        game.makeMove(new Move("pop",0,1,0,"eatFood",null,null));
        game.makeMove(new Move("test",0,2,0,"eatFood",null,null));
        game.makeMove(new Move("pop",0,0,0,"EndPhase",null,null));
        game.makeMove(new Move("test",0,0,0,"EndPhase",null,null));
        assert (game.phase.equals(Phase.DEAD));
        assert (game.food==6);

    }

}