package game.entities;

import java.io.Serializable;

public class ExtraMessage implements Serializable {
    String playerOnAttack;
    int predator;
    String playerUnderAttack;
    int victim;
    String type;

    public ExtraMessage(String name, int id, String name1, int id1, String type){
        playerOnAttack=name;
        predator=id;
        playerUnderAttack=name1;
        victim=id1;
        this.type=type;
    }

    public int getPredator(){return predator;}

    public String getPlayerOnAttack(){
        return playerOnAttack;
    }
    public String getType(){return type;
    }




}
