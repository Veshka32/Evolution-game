package game.controller;

import game.constants.Property;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
class ExtraMessage implements Serializable {
    private String playerOnAttack;
    private int predator;
    private String playerUnderAttack;
    private int victim;
    private Property type;

    public void setPlayerOnAttack(String playerOnAttack) {
        this.playerOnAttack = playerOnAttack;
    }

    public void setPredator(int predator) {
        this.predator = predator;
    }

    public String getPlayerUnderAttack() {
        return playerUnderAttack;
    }

    public void setPlayerUnderAttack(String playerUnderAttack) {
        this.playerUnderAttack = playerUnderAttack;
    }

    public int getVictim() {
        return victim;
    }

    public void setVictim(int victim) {
        this.victim = victim;
    }

    public void setType(Property type) {
        this.type = type;
    }

    public ExtraMessage() {
    }

    ExtraMessage(String name, int id, String name1, int id1, Property type) {
        playerOnAttack = name;
        predator = id;
        playerUnderAttack = name1;
        victim = id1;
        this.type = type;
    }

    public int getPredator() {
        return predator;
    }

    public String getPlayerOnAttack() {
        return playerOnAttack;
    }

    public Property getType() {
        return type;
    }

}
