package game.controller;

import game.constants.Phase;
import game.entities.Animal;
import game.entities.Card;
import game.entities.Move;
import game.entities.Player;

public class EvolutionPhase {
    private Game game;
    private Move move;

    public EvolutionPhase(Game game,Move move){
        this.game=game; this.move=move;
    }

    void processMove() throws GameException {

        switch (move.getMove()) {
            case "MakeAnimal":
                game.makeAnimal(move);
                break;
            case "DeleteProperty":
                Player player=game.getPlayer(move.getPlayer());
                Animal animal=player.getAnimal(move.getAnimalId());
                animal.removeProperty(move.getProperty());
                player.deleteCard(move.getCardId());
                break;
            case "PlayProperty":
                if (Card.isDouble(move.getProperty()))
                    processDoubleProperty();

                else {
                    processSimpleProperty();
                }
        }
        if (game.getPhase().equals(Phase.EVOLUTION)) game.switchPlayerOnMove(); //if new phase, do not switch player, because playersTurn is update
    }

    private void processDoubleProperty() throws GameException {
        Player player = game.getPlayer(move.getPlayer());
        int id1=move.getAnimalId();
        int id2=move.getSecondAnimalId();
        if (id1==0 || id2==0) throw new GameException("You must pick two animals to play property "+move.getProperty());
        player.connectAnimal(id1,id2,move.getProperty());
        player.deleteCard(move.getCardId());

    }

    private void processSimpleProperty() throws GameException {
        if (move.getAnimalId()==0) throw new GameException("You forgot pick an animal");

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
