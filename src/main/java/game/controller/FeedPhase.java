package game.controller;

import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

import java.util.Random;

public class FeedPhase {
    private Game game;

    public FeedPhase(Game game){
        this.game=game;
    }

    void eat(Move move) throws GameException {

        switch (move.getMove()) {
            case "eatFood":
                eatFood(move);
                break;
            case "attack":
                attack(move);
                break;
            case "playAnimalProperty": //piracy, Hibernation, tail loss,Crazing, fat (eatFat), mimicry
                playAnimalProperty(move);
                return; //do not switch player?
            case "endMove":
                game.switchPlayerOnMove();
                game.getPlayer(move.getPlayer()).resetEatFlag();
        }
        //if (game.phase.equals(Phase.FEED)) //if new phase, do not switch player, because playersTurn is update

    }

    public void playAnimalProperty(Move move) throws GameException{
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

    public void attack(Move move) throws GameException {
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
            victim.getOwner().deleteAnimal(victim.getId());
            player.resetFedFlag();
            game.feedScavenger(move.getPlayer());
        }
        player.doEat();
    }

    public void eatFood(Move move) throws GameException {
        if (game.getFood()==0) throw new GameException("There is no more food");
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        if (animal==null) throw new GameException("Feeding stranger animal is danger!");
        animal.eatMeet(player, game);
        player.resetFedFlag();
        player.doEat();
    }
}
