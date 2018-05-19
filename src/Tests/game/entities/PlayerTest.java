package game.entities;

import game.controller.GameException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player=new Player("test");
    Animal an1=new Animal(1,"test");
    Animal an2=new Animal(2,"test");
    Animal an3=new Animal(3,"test");
    Animal an4=new Animal(4,"pop");

    @Test
    public void connectAnimal() throws GameException {
        player.addAnimal(an1);
        player.addAnimal(an2);
        player.connectAnimal(1,2,"Cooperation");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(1,2,"Cooperation");
        }, "These animals are already helping each other!");

        assertThrows(GameException.class,()->{
            player.connectAnimal(1,3,"Communication"); //not throw any
        },"It's not your animal(s)");

        player.addAnimal(an3);
        player.connectAnimal(1,3,"Communication");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(1,3,"Communication");
        }, "These animals are already helping each other!");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(2,3,"Communication");
        }, "These animals are already helping each other!");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(2,3,"Cooperation");
        }, "These animals are already helping each other!");

        assertThrows(GameException.class, ()->{
            player.connectAnimal(1,4,"Communication");
        }, "It's not your animal(s)");

        player.addAnimal(an4);
        player.connectAnimal(1,4,"Communication");

        int[] list=new int[4];
        int[] rightOrder={1,2,3,4};
        List<Animal> sorted=player.animals.get(0);
        for (int i=0;i<list.length;i++) {
            list[i]=sorted.get(i).getId();
        }
        assert(Arrays.equals(list,rightOrder));







    }

}