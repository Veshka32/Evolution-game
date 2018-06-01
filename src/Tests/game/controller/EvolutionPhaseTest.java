package game.controller;

import game.constants.CardHolder;
import game.constants.Phase;
import game.entities.Move;
import game.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;


@RunWith(MockitoJUnitRunner.class)
public class EvolutionPhaseTest {

    @Spy
    private CardHolder cardHolder=new CardHolder();

    @InjectMocks
    Game game;

    String player1 = "pop";
    String player2 = "test";
    String[] playersList = new String[]{player1, player2};

    @Test
    public void endPhase1() {
        game.addPlayer(player1);
        game.addPlayer(player2);

        //player test ends phase
        game.makeMove(new Move("test", 0, 0, 0, "EndPhase", null, null));
        assert (game.getPlayerOnMove() == 0);
        assert (game.getPlayersTurn().get(game.getPlayerOnMove()).equals(player1));
        assert (game.getPhase().equals(Phase.EVOLUTION));

        game.makeMove(new Move("pop", 0, 0, 0, "EndPhase", null, null));
        assert (game.getPlayerOnMove() == 0);
        assert (game.getPlayersTurn().size()==2);
        assert (game.getPlayersTurn().get(game.getPlayerOnMove()).equals(player1));
        assert (game.getPhase().equals(Phase.FEED));
    }
}