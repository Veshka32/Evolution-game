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
        //what if food==0, but somebody want's to eat somebody?
        if (game.food==0) {game.goToNextPhase(); return;} //if new phase, do not switch player, because playersTurn is update
        game.switchPlayerOnMove();

    }

    public void eatFood(Game game, Move move) throws GameException {
        Player player=game.getPlayer(move.getPlayer());
        Animal animal=player.getAnimal(move.getAnimalId());
        if (animal==null) throw  new GameException("It's not your animal");
        animal.eatMeet(player,game);
        player.resetFedFlag();
    }

}
