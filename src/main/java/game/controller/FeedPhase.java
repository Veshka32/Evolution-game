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
            case "attack":
                attack(game, move);
                break;
        }
        //what if food==0, but somebody want's to eat somebody?
        if (game.getFood()==0) {game.goToNextPhase(); return;} //if new phase, do not switch player, because playersTurn is update
        game.switchPlayerOnMove();

    }

    public void attack(Game game,Move move)throws GameException{
        Player player=game.getPlayer(move.getPlayer());
        Animal predator=player.getAnimal(move.getAnimalId());
        if (predator==null) throw  new GameException("It's not your animal");

        if (move.getAnimalId() == 0 || move.getSecondAnimalId() == 0)
            throw new GameException("You must pick two animals to play this card");

        else if (move.getAnimalId() == move.getSecondAnimalId())
            throw new GameException("You must play this property on two different animals");

        Animal victim=game.getAnimal(move.getSecondAnimalId());
        predator.attack(victim);
    }

    public void eatFood(Game game, Move move) throws GameException {
        Player player=game.getPlayer(move.getPlayer());
        Animal animal=player.getAnimal(move.getAnimalId());
        animal.eatMeet(player,game);
        player.resetFedFlag();
    }

}
