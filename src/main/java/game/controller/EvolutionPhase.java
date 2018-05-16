package game.controller;

import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

public class EvolutionPhase {

    public EvolutionPhase() {
    }

    public void playProperty(Game game, Move move) throws GameException {
        String property = move.getProperty();
        String name = move.getPlayer();
        Player player = game.getPlayer(move.getPlayer());
        int animalID = move.getAnimalId();
        int cardID = move.getCardId();

        switch (move.getProperty()) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;
            case "EndPhase":
                game.playerEndsPhase(name);
                if (game.isPhaseEnded()) {
                    game.switchStatus();
                    game.goToNextPhase();
                }

            case "PlayProperty":
                Animal animal = player.getAnimal(move.getAnimalId());
                if (animal == null) {
                    throw new GameException("this is not your animal");
                }
                player.deleteCard(move.getCardId());
                animal.addProperty(move.getProperty(),name);
        }
        game.switchPlayerOnMove();
    }
}
