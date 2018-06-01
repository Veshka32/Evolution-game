package game.controller;

import game.constants.CardHolder;
import game.entities.Animal;
import game.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    @Spy
    CardHolder cardHolder;

    @InjectMocks
    Game first,second;

    @Test
    public void gsonExpose(){
        Game game=new Game();
        game.addPlayer("test");
        Player pl=game.getPlayer("test");
        pl.addAnimal(new Animal(1,pl));
        pl.addAnimal(new Animal(2,pl));
        String gamejson=game.convertToJsonString("test");
    }

    @Test
    public void testCardHolder(){
        first.setCardList(cardHolder.getCards());
        second.setCardList(cardHolder.getCards());
        assert (first.getCardList().containsAll(second.getCardList()));
    }

}