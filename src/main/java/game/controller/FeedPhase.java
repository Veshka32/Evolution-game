package game.controller;

import game.constants.Phase;
import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

public class FeedPhase {

    void eat(Game game, Move move) throws GameException {

        switch (move.getMove()) {
            case "EndPhase":
                game.playerEndsPhase(move.getPlayer());
                break;
            case "eatFood":
                eatFood(game, move);
                break;

        }
        if (game.phase.equals(Phase.FEED)) game.switchPlayerOnMove(); //if new phase, do not switch player, because playersTurn is update

    }

    public void eatFood(Game game, Move move) throws GameException {
        Player player=game.getPlayer(move.getPlayer());
        Animal animal=player.getAnimal(move.getAnimalId());
        if (animal==null) throw  new GameException("It's not your animal");
        animal.eatMeet(player);
        player.resetFedFlag();
        game.deleteFood();

    }

}
