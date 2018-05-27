package game.entities;

import game.constants.Phase;

public class TailLossMessage {
    Phase phase = Phase.TAIL_LOSS;
    String player;
    String playerOnAttack;
    int predator;
    String playerUnderAttack;
    int victim;

    public TailLossMessage(String name, int id, String name1, int id1){
        playerOnAttack=name;
        predator=id;
        playerUnderAttack=name1;
        victim=id1;
    }

    public void setCurrentPlayer(String name){
        player=name;
    }

    public int getPredator(){return predator;}

    public String getPlayerOnAttack(){
        return playerOnAttack;
    }




}
