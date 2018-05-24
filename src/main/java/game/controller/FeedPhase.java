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
            case "playAnimalProperty": //piracy, Hibernation, tail loss,Crazing, fat (eatFat), mimicry
                playAnimalProperty(game,move);
                return; //do not switch player?
            case "makeFatSupply":
                makeFatSupply(game, move);
                break;
        }
//        //what if food==0, but somebody want's to eat somebody?
//        if (game.getFood() == 0) {
//            game.goToNextPhase();
//            return;
//        }
        if (game.phase.equals(Phase.FEED)) game.switchPlayerOnMove(); //if new phase, do not switch player, because playersTurn is update

    }

    public void playAnimalProperty(Game game,Move move) throws GameException{
        String property=move.getProperty();
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        switch (property){
            case "Hibernation":
                animal.hibernate(game.round);
                break;
            case "Piracy":
                //??
                break;
            case "Tail loss": //only if animal is attacked
                //??
                break;
            case "Grazing":
                if (game.getFood()<1) throw new GameException("There is no food left");
                game.deleteFood();
                break;
            case "Fat":
                animal.eatFat();
                break;
        }
    }

    public void makeFatSupply(Game game, Move move) throws GameException {
        if (game.getFood() == 0) throw new GameException("There is nothing to make fat supply of");
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        if (animal == null) throw new GameException("It's not your animal");
        animal.addFat();
        game.deleteFood();
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
            game.feedScavenger(move.getPlayer());
        }
    }

    public void eatFood(Game game, Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        if (animal==null) throw new GameException("Feeding stranger animal is danger!");
        animal.eatMeet(player, game);
        player.resetFedFlag();
    }

}
