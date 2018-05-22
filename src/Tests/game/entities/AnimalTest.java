package game.entities;

import game.constants.Constants;
import game.controller.Game;
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
        assert (predator.totalFatSupply==2);
        assert (predator.totalHungry==2);
    }

    @Test
    void eatMeet() throws GameException {
        //set up player with 5 connected animals
        Game game=new Game();
        game.setFood(Constants.MAX_FOOD.getValue());
        Player player=new Player("test");
        int index=1;
        Animal[] animals={new Animal(index++,player),new Animal(index++,player),new Animal(index++,player),new Animal(index++,player),new Animal(index++,player)};

        for (Animal an:animals
             ) {
            player.addAnimal(an);
        }
        player.connectAnimal(1,2,"Cooperation");
        player.connectAnimal(2,3,"Cooperation");
        player.connectAnimal(3,4, "Communication");
        player.connectAnimal(4,5,"Communication");

        //feed animals 1 first
        animals[0].eatMeet(player,game);
        assertThrows (GameException.class, ()->{animals[0].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[2].eatMeet(player,game);},"This animal is fed!");
        assert (animals[3].currentHungry==1);
        assert (animals[4].currentHungry==1);

        //feed animal 3 first
        player.resetFedFlag();
        player.resetFields();
        game.setFood(Constants.MAX_FOOD.getValue());
        animals[2].eatMeet(player,game);
        assertThrows (GameException.class, ()->{animals[0].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[2].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[3].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[4].eatMeet(player,game);},"This animal is fed!");
        assert (game.getFood()==5); //5= MAX.FOOD-3

        //feed animal 5 first
        player.resetFedFlag();
        player.resetFields();
        game.setFood(Constants.MAX_FOOD.getValue());
        animals[4].eatMeet(player,game);
        assertThrows (GameException.class, ()->{animals[0].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[2].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[3].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[4].eatMeet(player,game);},"This animal is fed!");
        assert (game.getFood()==5); //max 8-3

        //feed animal 5 first, game has less food
        player.resetFedFlag();
        player.resetFields();
        game.setFood(2);
        animals[4].eatMeet(player,game);
        assertThrows (GameException.class, ()->{animals[3].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[4].eatMeet(player,game);},"This animal is fed!");
        assert (game.getFood()==0);
        assert (animals[0].currentHungry==1);
        assert (animals[1].currentHungry==1);
        assert (animals[2].currentHungry==1);

        //feed animal 3 first, game has less food
        player.resetFedFlag();
        player.resetFields();
        game.setFood(2);
        animals[2].eatMeet(player,game);
        assertThrows (GameException.class, ()->{animals[0].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[2].eatMeet(player,game);},"This animal is fed!");
        assertThrows (GameException.class, ()->{animals[3].eatMeet(player,game);},"This animal is fed!");
        assert (animals[4].currentHungry==1);
        assert (game.getFood()==0);

        //feed big animal
        player.resetFedFlag();
        player.resetFields();
        game.setFood(Constants.MAX_FOOD.getValue());
        animals[0].addProperty("Big");
        animals[2].addProperty("Big");
        player.resetFields();
        animals[0].eatMeet(player,game);
        assertThrows (GameException.class, ()->{animals[1].eatMeet(player,game);},"This animal is fed!");
        assert (animals[0].currentHungry==1);
        assert (animals[1].currentHungry==0);
        assert (animals[2].currentHungry==1);

        //feed big animal again
        animals[0].eatMeet(player,game);
        assert (animals[0].currentHungry==0);
        assert (animals[1].currentHungry==0);
        assert (animals[2].currentHungry==1);
    }
}