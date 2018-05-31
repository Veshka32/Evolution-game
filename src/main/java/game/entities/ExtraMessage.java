package game.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ExtraMessage implements Serializable {
    String playerOnAttack;
    int predator;
    String playerUnderAttack;
    int victim;
    String type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public void setType(String type) {
        this.type = type;
    }

    public ExtraMessage() {
    }

    public ExtraMessage(String name, int id, String name1, int id1, String type) {
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

    public String getType() {
        return type;
    }

}
