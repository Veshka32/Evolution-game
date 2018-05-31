package game.entities;
import com.google.gson.Gson;

import java.io.IOException;

public class Move {
    String player;
    String move;
    String property;
    int cardId;
    int animalId;
    int secondAnimalId;
    String log;


    public Move(String player, int cardId, int animalId, int secondAnimalId, String move, String property, String log){
        this.move = move;
        this.cardId = cardId;
        this.animalId=animalId;
        this.secondAnimalId=secondAnimalId;
        this.player = player;
        this.property=property;
        this.log=log;
    }

    public String getMove() {
        return move;
    }

    public String getPlayer() {
        return player;
    }

    public String getLog(){
        return log;
    }

    public int getCardId(){return cardId;}

    public int getAnimalId(){return animalId;}

    public int getSecondAnimalId(){return secondAnimalId;}

    public String getProperty(){return property;}

    public String toString() {
        return new Gson().toJson(this);
    }
}
