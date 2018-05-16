package game.controller;

import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

public class EvolutionPhase {

    public EvolutionPhase() {
    }

    public void playProperty(Game game, Move move) throws GameException {

        switch (move.getMove()) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;
            case "EndPhase":
                game.playerEndsPhase(move.getPlayer());
                if (game.isPhaseEnded()) {
                    game.goToNextPhase();
                }
                break;

            case "PlayProperty":
                if (isDouble(move.getProperty()))
                    processDoubleProperty(game, move);
                else {
                    processSimpleProperty(game, move);
                }
        }
        game.sort();
        game.switchPlayerOnMove();
    }

    private boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication");
    }

    private void processDoubleProperty(Game game, Move move) throws GameException {
        Player player=game.getPlayer(move.getPlayer());
        if (player.howManyAnimals()<2) throw new GameException("You don't have enough animals");
        if (move.getAnimalId()==move.getSecondAnimalId()) throw new GameException("You must play this property on two different animals");
        if (!(player.hasAnimal(move.getAnimalId()) && player.hasAnimal(move.getSecondAnimalId()))) throw new GameException("It's not your animal(s)");

        Animal animal=player.getAnimal(move.getAnimalId());
        animal.addDoubleProperty(move.getProperty(),move.getSecondAnimalId());

        Animal animal2=player.getAnimal(move.getSecondAnimalId());
        animal2.addDoubleProperty(move.getProperty(),move.getAnimalId());
    }

    private void processSimpleProperty(Game game, Move move) throws GameException {
        Player player=game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
//        if (animal == null) {
//            throw new GameException("It's not your animal");
//        }
        animal.addProperty(move.getProperty(), move.getPlayer());
        player.deleteCard(move.getCardId());

    }
}
