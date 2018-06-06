package game.controller;

import game.entities.Animal;
import game.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    @Spy
    Deck deck;

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
        first.setCardList(deck.getCards());
        second.setCardList(deck.getCards());
        assert (first.getCardList().containsAll(second.getCardList()));
    }

    @Test
    public void linkedMap(){
        Map<String,Player> map=new LinkedHashMap<>();
        map.put("test",new Player("test"));
        map.put("pop",new Player("pop"));
        map.forEach((k,v)->System.out.println(k));
        LinkedList<String> list=new LinkedList<>(map.keySet());
        list.forEach(v->System.out.println(v));
    }

}