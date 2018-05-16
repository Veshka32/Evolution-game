package game.controller;

import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

public class EvolutionPhase {

    public EvolutionPhase() {
    }

    public void playProperty(Game game, Move move) throws GameException {
        String property = move.getProperty();
        String moveType=move.getMove();
        String name = move.getPlayer();
        Player player = game.getPlayer(move.getPlayer());
        int animalID = move.getAnimalId();
        int cardID = move.getCardId();

        switch (moveType) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;
            case "EndPhase":
                game.playerEndsPhase(name);
                if (game.isPhaseEnded()) {
                    game.goToNextPhase();
                }
                break;

            case "PlayProperty":
                Animal animal = player.getAnimal(animalID);
                if (animal == null) {
                    throw new GameException("this is not your animal");
                }
                animal.addProperty(property,name);
                player.deleteCard(cardID);
        }
        game.switchPlayerOnMove();
    }
}
