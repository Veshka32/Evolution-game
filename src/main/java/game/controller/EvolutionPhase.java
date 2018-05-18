package game.controller;

import game.entities.Animal;
import game.entities.Move;
import game.entities.Player;

public class EvolutionPhase {

    public void playProperty(Game game, Move move) throws GameException {

        switch (move.getMove()) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;
            case "EndPhase":
                game.playerEndsPhase(move.getPlayer());
                return; //no need in switchPlayerOnMove

            case "PlayProperty":
                if (isDouble(move.getProperty()))
                    processDoubleProperty(game, move);
                else {
                    processSimpleProperty(game, move);
                }
        }
        game.switchPlayerOnMove();
    }

    private boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication");
    }

    private void processDoubleProperty(Game game, Move move) throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        int id1=move.getAnimalId();
        int id2=move.getSecondAnimalId();

        if (player.howManyAnimals() < 2) throw new GameException("You don't have enough animals");

        if (id1 == id2)
            throw new GameException("You must play this property on two different animals");

        if (id1 == 0 ||id2 == 0)
            throw new GameException("You must pick two animals to play this card");

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
