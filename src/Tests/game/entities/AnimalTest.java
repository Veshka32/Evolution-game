package game.entities;

import game.controller.GameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {


    @Test
    void addProperty() throws GameException {
        Player owner1=new Player("test");
        int index=1;
        Animal scavenger=new Animal(index++,owner1);
        Animal predator=new Animal(index++,owner1);
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
    void eatMeet() throws GameException {
        Player player=new Player("test");
        int index=1;
        Animal[] animals={new Animal(index++,player),new Animal(index++,player),new Animal(index++,player),new Animal(index++,player)};

        for (Animal an:animals
             ) {
            player.addAnimal(an);
        }
        player.connectAnimal(1,2,"Cooperation");
        player.connectAnimal(2,3,"Cooperation");

        //feed one animal
        animals[0].eatMeet(player);
        assertThrows (GameException.class, ()->{animals[0].eatMeet(player);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[2].eatMeet(player);},"This animal is fed!");
        assert (animals[0].currentHungry==0);
        assert (animals[1].currentHungry==0);
        assert (animals[2].currentHungry==0);
        assert (animals[3].currentHungry==1);
        player.resetFedFlag();
        player.reserCurrentHungry();

        //feed big animal
        animals[0].addProperty("Big");
        animals[2].addProperty("Big");
        player.reserCurrentHungry();
        animals[0].eatMeet(player);
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player);},"This animal is fed!");
        assert (animals[0].currentHungry==1);
        assert (animals[1].currentHungry==0);
        assert (animals[2].currentHungry==1);

        //feed big animal again
        animals[0].eatMeet(player);
        assert (animals[0].currentHungry==0);
        assert (animals[1].currentHungry==0);
        assert (animals[2].currentHungry==1);


    }
}