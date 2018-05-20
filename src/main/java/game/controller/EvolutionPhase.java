package game.controller;

import game.constants.Phase;
import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

class EvolutionPhase {

    void playProperty(Game game, Move move) throws GameException {

        switch (move.getMove()) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;

            case "EndPhase":
                game.playerEndsPhase(move.getPlayer());
                break;

            case "PlayProperty":
                if (isDouble(move.getProperty()))
                    processDoubleProperty(game, move);

                else {
                    processSimpleProperty(game, move);
                }
        }
        if (game.phase.equals(Phase.EVOLUTION)) game.switchPlayerOnMove(); //if new phase, do not switch player, because playersTurn is update

    }

    private boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication") || property.equals("Symbiosis");
    }

    private void processDoubleProperty(Game game, Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        int id1=move.getAnimalId();
        int id2=move.getSecondAnimalId();

        player.connectAnimal(id1,id2,move.getProperty());

    }

    private void processSimpleProperty(Game game, Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        Animal animal = player.getAnimal(move.getAnimalId());
        String property=move.getProperty();

        if (property.equals("Parasite")) {
            if (animal!=null) throw new GameException("You can't play Parasite on your own animal");
            Animal attackedAnimal=game.getAnimal(move.getAnimalId());
            attackedAnimal.addProperty(property);

        } else {
            if (animal==null) throw new GameException("It's not your animal");
            animal.addProperty(property);
        }
        player.deleteCard(move.getCardId());
    }


}
