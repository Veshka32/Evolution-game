package game.entities;

import game.controller.GameException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player=new Player("test");
    Animal an1=new Animal(1,player);
    Animal an2=new Animal(2,player);
    Animal an3=new Animal(3,player);

    @Test
    public void connectAnimal() throws GameException {
        player.addAnimal(an1);
        player.addAnimal(an2);
        player.connectAnimal(1,2,"Cooperation");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(1,2,"Cooperation");
        }, "These animals are already cooperating");

        assertThrows(GameException.class,()->{
            player.connectAnimal(1,3,"Communication"); //not throw any
        },"It's not your animal(s)");

        player.addAnimal(an3);
        player.connectAnimal(1,3,"Communication");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(1,3,"Communication");
        }, "These animals are already communicating");

        player.animalDie();

    }



}