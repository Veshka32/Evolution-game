package game.controller;

import game.constants.Phase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class EvolutionPhaseTest {

    @Spy
    private Deck deck =new Deck();

    @InjectMocks
    Game game;

    String player1 = "pop";
    String player2 = "test";
    String[] playersList = new String[]{player1, player2};

    @Test
    public void endPhase1() {
        game.setCardList(deck.getCards());
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();

        //player test ends phase
        game.makeMove(new Move("test", 0, 0, 0, "END_PHASE", null, null));
        assert (game.getPlayerOnMove() == 0);
        assert (game.getPlayersOrder().get(game.getPlayerOnMove()).equals(player1));
        assert (game.getPhase().equals(Phase.EVOLUTION));

        game.makeMove(new Move("pop", 0, 0, 0, "END_PHASE", null, null));
        assert (game.getPlayerOnMove() == 0);
        assert (game.getPlayersOrder().size()==2);
        assert (game.getPlayersOrder().get(game.getPlayerOnMove()).equals(player1));
        assert (game.getPhase().equals(Phase.FEED));
    }
}