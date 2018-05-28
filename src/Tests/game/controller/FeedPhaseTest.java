package game.controller;

import game.constants.Constants;
import game.constants.Phase;
import game.entities.Animal;
import game.entities.Card;
import game.entities.Move;
import game.entities.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    public void eatFood() {
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
        game.setFood(2);
        game.makeMove(new Move("pop",0,1,0,"eatFood",null,null));
        game.makeMove(new Move("test",0,2,0,"eatFood",null,null));
        assert (game.getFood()==0);

        //food=8;
        game.phase=Phase.FEED;
        game.setFood(Constants.MAX_FOOD.getValue());
        for (Player pl : game.players.values()
                ) {
            pl.resetFields();
        }
        game.makeMove(new Move("pop",0,1,0,"eatFood",null,null));
        game.makeMove(new Move("test",0,2,0,"eatFood",null,null));
        game.makeMove(new Move("pop",0,0,0,"EndPhase",null,null));
        game.makeMove(new Move("test",0,0,0,"EndPhase",null,null));
        assert (game.getFood()==6);

    }
    @Test
    void tailLoss() throws GameException {
        Game game=new Game();
        game.setGenerator(new TestCardGenerator());
        game.addPlayer(player1);
        game.addPlayer(player2);
        Player test=game.getPlayer(player1);
        Player pop=game.getPlayer(player2);
        Animal tailLoss=new Animal(1,test);
        Animal predator=new Animal(2,pop);
        tailLoss.addProperty("Tail loss");
        predator.addProperty("Predator");
        test.addAnimal(tailLoss);
        pop.addAnimal(predator);
        game.animalList.put(1,tailLoss);
        game.animalList.put(2,predator);
        game.phase=Phase.FEED;
        game.makeMove(new Move("test",0,2,1,"attack",null,null));
        assert(game.phase.equals(Phase.FEED));
        assert (game.extraMessage.getPlayerOnAttack().equals("test"));
        assert (game.extraMessage.getPredator()==2);
        game.makeMove(new Move("pop",0,1,0,"DeleteProperty","Tail loss",null));
        assert (game.phase.equals(Phase.FEED));
        assert (game.extraMessage ==null);
        assert (!predator.notHungry());
        assert (pop.isDoEat());
        assert (!tailLoss.hasProperty("Tail loss"));



    }


}