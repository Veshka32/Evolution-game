package game.controller;

import game.constants.Phase;
import game.constants.Property;
import game.entities.Animal;
import game.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeedPhaseTest {

    @Spy
    private Deck deck =new Deck();

    @InjectMocks
    Game game;

    String player1 = "pop";
    String player2 = "test";

    @Test
    public void tailLoss() throws GameException {
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setPhase(Phase.EVOLUTION);
        Player pop=game.getPlayer(player1);
        Player test=game.getPlayer(player2);
        Animal tailLoss=new Animal(1,test);
        Animal predator=new Animal(2,pop);
        tailLoss.addProperty(Property.TAIL_LOSS);
        predator.addProperty(Property.PREDATOR);
        test.addAnimal(tailLoss);
        pop.addAnimal(predator);
        game.goToNextPhase();
        game.makeMove(new Move("pop",0,2,1,"ATTACK",null,null));
        assert(game.getPhase().equals(Phase.FEED));
        assert (game.getExtraMessage().getPlayerOnAttack().equals("pop"));
        assert (game.getExtraMessage().getPredator()==2);
        game.makeMove(new Move("test",0,1,0,"DELETE_PROPERTY","TAIL_LOSS",null));
        assert (game.getPhase().equals(Phase.FEED));
        assert (game.getExtraMessage() ==null);
        assert (!predator.notHungry());
        assert (pop.isDoEat());
        assert (!tailLoss.hasProperty(Property.TAIL_LOSS));
    }
}