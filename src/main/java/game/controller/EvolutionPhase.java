package game.controller;

import game.constants.Phase;
import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

class EvolutionPhase {
    private Game game;

    public EvolutionPhase(Game game){
        this.game=game;
    }

    void playProperty(Move move) throws GameException {

        switch (move.getMove()) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;

            case "TailLoss":
                tailLoss(move);
                break;

            case "EndPhase":
                game.playerEndsPhase(move.getPlayer());
                break;

            case "PlayProperty":
                if (isDouble(move.getProperty()))
                    processDoubleProperty(move);

                else {
                    processSimpleProperty(move);
                }
        }
        if (game.phase.equals(Phase.EVOLUTION)) game.switchPlayerOnMove(); //if new phase, do not switch player, because playersTurn is update
    }

    private void tailLoss(Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        Animal animal=player.getAnimal(move.getAnimalId());
        if (animal==null) throw new GameException("It's not your animal");
        int id1=move.getAnimalId();
        int id2=move.getSecondAnimalId();
        if (id1!=id2) throw new GameException("You should remove property from the same animal");
        String property=move.getProperty();
        animal.removeProperty(property,move.getSecondAnimalId());
    }

    private boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication") || property.equals("Symbiosis");
    }

    private void processDoubleProperty(Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        int id1=move.getAnimalId();
        int id2=move.getSecondAnimalId();
        player.connectAnimal(id1,id2,move.getProperty());
        player.deleteCard(move.getCardId());

    }

    private void processSimpleProperty(Move move) throws GameException {
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
