package game.controller;

import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

import java.util.Random;

public class FeedPhase {
    private Game game;
    private Move move;

    public FeedPhase(Game game,Move move){
        this.game=game;
        this.move=move;
    }

    void processMove() throws GameException {
        switch (move.getMove()) {
            case "eatFood":
                eatFood();
                break;
            case "attack":
                attack();
                break;
            case "playAnimalProperty": //piracy, Hibernation, tail loss,Crazing, mimicry
                playAnimalProperty();
                break;
            case "endMove":
                game.switchPlayerOnMove();
                game.getPlayer(move.getPlayer()).setDoEat(false);
            case "eatFat":
                eatFat();
                break;
        }
    }

    public void eatFat() throws GameException{

        Animal animal = game.getAnimal(move.getAnimalId());
        animal.eatFat();
    }

    public void playAnimalProperty() throws GameException{
        String property=move.getProperty();
        Animal animal = game.getAnimal(move.getAnimalId());
        switch (property){
            case "Hibernation":
                animal.hibernate(game.round);
                break;
            case "Piracy":
                pirate();
                break;
            case "Tail loss": //only if animal is attacked
                //??
                break;
            case "Grazing":
                if (game.getFood()<1) throw new GameException("There is no food left");
                game.deleteFood();
                break;
        }
    }

    public void pirate() throws GameException{
        Animal animal=game.getAnimal(move.getAnimalId());
        if (animal.isDoPiracy()) throw new GameException("This animal has already pirated!");
        if (!animal.isHungry()) throw new GameException("The animal can't pirate when it's fed");
        Animal victim=game.getAnimal(move.getSecondAnimalId());
        if (!victim.isHungry()) throw new GameException("You can't pirate from fed animal");
        if (victim.calculateHungry()==1) throw new GameException("There is nothing to pirate"); //if isHungry, but total hungry==1;
        if (!animal.checkSymbiosis(animal.getOwner())) throw new GameException("The animal can't processMove while it's symbiont is hungry");
        victim.deleteFood();
        animal.eatFish(1);
        animal.setDoPiracy(true);
    }

    public void attack() throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        if (player.isDoEat()) throw new GameException("You've already made attack ");
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
        player.setDoEat(true);
    }

    public void eatFood() throws GameException {
        if (game.getFood()==0) throw new GameException("There is no more food");
        Player player = game.getPlayer(move.getPlayer());
        if (player.isDoEat()) throw new GameException("You've already taken food");
        Animal animal = player.getAnimal(move.getAnimalId());
        if (animal==null) throw new GameException("Feeding stranger animal is danger!");
        animal.eatMeet(player, game);
        player.resetFedFlag();
        player.setDoEat(true);
    }
}
