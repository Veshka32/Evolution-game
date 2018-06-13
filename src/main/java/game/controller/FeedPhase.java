package game.controller;

import game.constants.Property;
import game.entities.Animal;
import game.entities.Player;

import java.util.List;
import java.util.Random;

class FeedPhase {
    private Game game;
    private Move move;

    FeedPhase(Game game, Move move) {
        this.game = game;
        this.move = move;
    }

    void processMove() throws GameException {
        switch (move.getMove()) {
            case "eatFood":
                eatFood();
                break;
            case "attack":
                attack();
                break;
            case "playAnimalProperty":
                playAnimalProperty();
                break;
            case "endMove":
                game.switchPlayerOnMove();
                game.getPlayer(move.getPlayer()).setDoEat(false);
                game.getPlayer(move.getPlayer()).resetGrazing();
                break;
            case "eatFat":
                eatFat();
                break;
            case "DeleteProperty":
                processTailLoss();
                break;
            case "playMimicry":
                processMimicry();
                break;
        }
    }

    private void processMimicry() {
        Animal victim = game.getAnimal(move.getAnimalId());
        Animal predator = game.getAnimal(game.getExtraMessage().getPredator());
        if (victim.hasProperty(Property.TAIL_LOSS)) {
            game.playTailLoss(predator, victim);
            return;
        }

        afterSuccessfulAttack(victim, predator, predator.getOwner());
        game.afterMimicry();
    }

    private void processTailLoss() {
        Animal victim = game.getAnimal(move.getAnimalId());
        Property property = move.getProperty();
        victim.removeProperty(property);
        Animal predator = game.getAnimal(game.getExtraMessage().getPredator());
        if (canMimicry(victim, predator)) return;
        predator.setAttackFlag(true);
        predator.getOwner().resetFedFlag();
        if (game.getPlayersOrder().size() > 1) predator.getOwner().setDoEat(true);
        predator.eatFish(1);
        game.afterTailLoss();
    }


    private void attack() throws GameException {

        Player player = game.getPlayer(move.getPlayer());
        Animal predator = player.getAnimal(move.getAnimalId());
        if (predator == null) throw new GameException("It's not your animal");

        if (move.getAnimalId() == 0 || move.getSecondAnimalId() == 0)
            throw new GameException("You must pick two animals to play this card");

        else if (move.getAnimalId() == move.getSecondAnimalId())
            throw new GameException("You must play this property on two different animals");

        Animal victim = game.getAnimal(move.getSecondAnimalId());
        predator.attack(victim);

        //probability 50/50
        if (victim.hasProperty(Property.RUNNING)) {
            boolean isSuccessful = new Random().nextBoolean();
            if (!isSuccessful) {
                game.addLogMessage("Animal #",String.valueOf(victim.getId())," run away from predator");
                predator.setAttackFlag(true);
                if (game.getPlayersOrder().size() > 1) player.setDoEat(true);
                return;
            } else
                game.addLogMessage("Predator #",String.valueOf(predator.getId())," run up animal #",String.valueOf(victim.getId()));
        }

        if (victim.hasProperty(Property.TAIL_LOSS)) {
            game.playTailLoss(predator, victim);
            return;
        }
        if (canMimicry(victim, predator)) return;
        afterSuccessfulAttack(victim, predator, player);
    }


    private void afterSuccessfulAttack(Animal victim, Animal predator, Player player) {
        if (victim.hasProperty(Property.POISONOUS)) predator.poison();
        predator.setAttackFlag(true);
        player.resetFedFlag();
        if (game.getPlayersOrder().size() > 1) player.setDoEat(true);
        predator.eatFish(2);
        victim.die();
        victim.getOwner().deleteAnimal(victim.getId());
        game.feedScavenger(move.getPlayer());
    }

    private boolean canMimicry(Animal victim, Animal predator) {
        if (victim.hasProperty(Property.MIMICRY)) {
            List<Integer> animalsToRedirect = victim.getOwner().canRedirect(predator, victim.getId());
            if (!animalsToRedirect.isEmpty()) {
                game.playMimicry(predator, victim, animalsToRedirect);
                return true;
            }
        }
        return false;
    }

    private void eatFat() throws GameException {
        Animal animal = game.getAnimal(move.getAnimalId());
        animal.eatFat();
    }

    private void playAnimalProperty() throws GameException {
        Property property = move.getProperty();
        Animal animal = game.getAnimal(move.getAnimalId());
        switch (property) {
            case HIBERNATION:
                animal.hibernate(game.getRound());
                break;
            case PIRACY:
                pirate();
                break;
            case GRAZING:
                graze();
                break;
        }
    }

    private void eatFood() throws GameException {
        if (game.getFood() == 0) throw new GameException("There is no more food");
        Player player = game.getPlayer(move.getPlayer());
        if (player.isDoEat()) throw new GameException("You've already taken food");
        Animal animal = player.getAnimal(move.getAnimalId());
        if (animal == null) throw new GameException("Feeding stranger animal is danger!");
        animal.eatMeet(player, game);
        player.resetFedFlag();
        if (game.getPlayersOrder().size() > 1) player.setDoEat(true);
    }

    private void graze() throws GameException {
        Animal animal = game.getAnimal(move.getAnimalId());
        if (animal.isDoGrazing()) throw new GameException("This animal is already grazed");
        if (game.getFood() < 1) throw new GameException("There is no food left");
        game.deleteFood();
        animal.setDoGrazing(true);
    }

    private void pirate() throws GameException {
        Animal animal = game.getAnimal(move.getAnimalId());
        if (animal.isDoPiracy()) throw new GameException("This animal has already pirated!");
        if (animal.notHungry()) throw new GameException("The animal can't pirate when it's fed");
        Animal victim = game.getAnimal(move.getSecondAnimalId());
        if (victim==null) throw new GameException("Pick the animal to pirate from");
        if (victim.notHungry()) throw new GameException("You can't pirate from fed animal");
        if (victim.calculateHungry() == 1)
            throw new GameException("There is nothing to pirate"); //if hungry, but total hungry==1;
        if (!animal.checkSymbiosis(animal.getOwner()))
            throw new GameException("The animal can't processMove while it's symbiont is hungry");
        victim.deleteFood();
        animal.eatFish(1);
        animal.setDoPiracy(true);
    }
}
