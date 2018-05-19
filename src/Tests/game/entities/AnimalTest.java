package game.entities;

import game.controller.GameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    String owner1="test";
    String owner2="pop";
    int index=1;
    Animal scavenger=new Animal(index++,owner1);
    Animal predator=new Animal(index++,owner1);
    Animal an3=new Animal(index++,owner2);

    @Test
    void addProperty() throws GameException {
        scavenger.addProperty("Scavenger");

        assertThrows(GameException.class,()->{
            scavenger.addProperty("Predator");
        },"Scavenger cannot be a predator");

        predator.addProperty("Predator");

        assertThrows(GameException.class,()->{
            predator.addProperty("Scavenger");
        },"Predator cannot be a scavenger");

        assertThrows(GameException.class,()->{
            predator.addProperty("Predator");
        },"This animal already has property: predator");

        predator.addProperty("Fat");
        predator.addProperty("Fat");
        assert (predator.fatSupply==2);
        assert (predator.totalHungry==2);
    }

    @Test
    void addDoubleProperty() {
    }
}