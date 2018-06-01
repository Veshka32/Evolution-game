package game.controller;

import game.constants.CardHolder;
import game.entities.Animal;
import game.entities.Player;
import org.junit.Test;


class GameTest {
    @Test
    void gsonExpose(){
        Game game=new Game();
        game.addPlayer("test");
        Player pl=game.getPlayer("test");
        pl.addAnimal(new Animal(1,pl));
        pl.addAnimal(new Animal(2,pl));
        String gamejson=game.convertToJsonString("test");
    }

    @Test
    void testCardHolder(){
        Game first=new Game();
        first.setCardList(new CardHolder().getCards());
        Game second=new Game();
        second.setCardList(new CardHolder().getCards());
        assert (first.getCardList().containsAll(second.getCardList()));
    }

}