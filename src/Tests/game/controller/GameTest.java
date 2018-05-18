package game.controller;

import game.constants.Phase;
import game.entities.Move;
import game.entities.Player;
import org.h2.schema.Constant;
import org.junit.jupiter.api.Test;
import services.dataBaseService.PasswordEncryptionService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game=new Game(new TestCardGenerator());
    String player1="pop";
    String player2="test";
    String[] playersList=new String[]{player1,player2};

    @Test
    void switchPlayerOnMove() {
        //start game when necessary number of players is added;
        game.addPlayer(player1);
        game.addPlayer(player2);
        assert (game.phase.equals(Phase.EVOLUTION));
        assert (game.getAllPlayers().equals(Arrays.toString(playersList)));
        assert (game.playerOnMoveIndex==0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));

        //players make moves
        Move move=new Move("pop",84,0,0,"MakeAnimal",null,null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex==1);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player2));

        move=new Move("test",78,0,0,"MakeAnimal",null,null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex==0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));

        //player pop ends phase
        move=new Move("pop",0,0,0,"EndPhase",null,null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex==0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player2));

        //player test ends phase
        move=new Move("test",0,0,0,"EndPhase",null,null);
        game.makeMove(move);
        assert (game.playerOnMoveIndex==0);
        assert (game.playersTurn.get(game.playerOnMoveIndex).equals(player1));
        assert (game.phase.equals(Phase.FEED));




    }
}