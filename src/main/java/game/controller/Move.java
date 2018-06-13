package game.controller;
import com.google.gson.Gson;
import game.constants.MoveType;
import game.constants.Property;

public class Move {
    private String player;
    private MoveType move;
    private Property property;
    private int cardId;
    private int animalId;
    private int secondAnimalId;
    private String log;


    public Move(String player, int cardId, int animalId, int secondAnimalId, String move, String property, String log){
        this.move = MoveType.valueOf(move);
        this.cardId = cardId;
        this.animalId=animalId;
        this.secondAnimalId=secondAnimalId;
        this.player = player;
        this.property=Property.valueOf(property);
        this.log=log;
    }

    public MoveType getMove() {
        return move;
    }

    public String getPlayer() {
        return player;
    }

    String getLog(){
        return log;
    }

    int getCardId(){return cardId;}

    int getAnimalId(){return animalId;}

    int getSecondAnimalId(){return secondAnimalId;}

    public Property getProperty(){return property;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
