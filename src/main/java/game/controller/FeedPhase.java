package game.controller;

import game.constants.Phase;
import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

import java.util.Random;

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
            case "makeFatSupply":
                makeFatSupply(game, move);
                break;
            case "Piracy":
                break;
            case "Hibernation":
                break;
            case "Tail loss":
                break;
            case "eatFat":
                break;
        }
        //what if food==0, but somebody want's to eat somebody?
        if (game.getFood() == 0) {
            game.goToNextPhase();
            return;
        } //if new phase, do not switch player, because playersTurn is update
        game.switchPlayerOnMove();

    }

    public void makeFatSupply(Game game, Move move) throws GameException {
        if (game.getFood() == 0) throw new GameException("There is nothing to make fat supply of");
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        if (animal == null) throw new GameException("It's not your animal");
        animal.addFat();
    }

    public void attack(Game game, Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        Animal predator = player.getAnimal(move.getAnimalId());
        if (predator == null) throw new GameException("It's not your animal");

        if (move.getAnimalId() == 0 || move.getSecondAnimalId() == 0)
            throw new GameException("You must pick two animals to play this card");

        else if (move.getAnimalId() == move.getSecondAnimalId())
            throw new GameException("You must play this property on two different animals");

        Animal victim = game.getAnimal(move.getSecondAnimalId());
        boolean isSuccessful = predator.attack(victim);

        //probability 50/50
        if (victim.hasProperty("Running")) {
            isSuccessful = new Random().nextBoolean();
        }
        if (isSuccessful) {
            if (victim.hasProperty("Poisonous")) {
                predator.poison();
            }
            predator.eatFish(2);
            victim.die();
            game.feedScavenger();
        }
    }

    public void eatFood(Game game, Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        animal.eatMeet(player, game);
        player.resetFedFlag();
    }

}
