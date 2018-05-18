package game.controller;

import game.constants.Phase;
import game.entities.Card;
import game.entities.Move;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    public class TestCardGenerator extends CardGenerator {
        @Override
        public List<Card> getCards() {
            return cardList; //no shuffle
        }
    }

    String player1 = "pop";
    String player2 = "test";
    String[] playersList = new String[]{player1, player2};

    @Test
    void endPhase() {
        Game game = new Game();
        //start game when necessary number of players is added;
        game.setGenerator(new TestCardGenerator());
        game.addPlayer(player1);
        game.addPlayer(player2);
        assert (game.phase.equals(Phase.EVOLUTION));
        assert (game.getAllPlayers().equals(Arrays.toString(playersList)));
        assert (game.playerOnMoveIndex == 0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));

        //players make moves
        Move move = new Move("pop", 84, 0, 0, "MakeAnimal", null, null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex == 1);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player2));

        move = new Move("test", 78, 0, 0, "MakeAnimal", null, null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex == 0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));

        //player pop ends phase
        move = new Move("pop", 0, 0, 0, "EndPhase", null, null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex == 0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player2));

        //player test ends phase
        move = new Move("test", 0, 0, 0, "EndPhase", null, null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex == 0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));
        assert (game.phase.equals(Phase.FEED));
    }

    @Test
    void endPhase1() {
        Game game = new Game();
        //start game when necessary number of players is added;
        game.setGenerator(new TestCardGenerator());
        game.addPlayer(player1);
        game.addPlayer(player2);
        assert (game.phase.equals(Phase.EVOLUTION));
        assert (game.getAllPlayers().equals(Arrays.toString(playersList)));
        assert (game.playerOnMoveIndex == 0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));

        //players make moves
        Move move = new Move("pop", 84, 0, 0, "MakeAnimal", null, null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex == 1);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player2));

        //player test ends phase
        move = new Move("test", 0, 0, 0, "EndPhase", null, null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex == 0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));
        assert (game.phase.equals(Phase.EVOLUTION));
    }

    @Test
    void sendError() {
        Game game = new Game();
        //start game when necessary number of players is added;
        game.setGenerator(new TestCardGenerator());
        game.addPlayer(player1);
        game.addPlayer(player2);

        //players make moves

        game.makeMove(new Move("pop", 84, 0, 0, "MakeAnimal", null, null));
        game.makeMove(new Move("test", 78, 0, 0, "MakeAnimal", null, null));

        game.makeMove(new Move("pop", 83, 2, 0, "PlayProperty", "Swimming", null));
        assert (game.error.equals("It's not your animal"));
        


    }


}