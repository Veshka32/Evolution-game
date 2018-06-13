package game.controller;

import game.entities.Animal;
import game.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

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
        String gamejson=game.getFullJson("test");
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
        map.put("test",new Player("test",1));
        map.put("pop",new Player("pop",2));
        map.forEach((k,v)->System.out.println(k));
        LinkedList<String> list=new LinkedList<>(map.keySet());
        list.forEach(v->System.out.println(v));
    }

    @Test
    public void winners(){
        Map players=new HashMap();
        Player one=new Player("one",1);
        one.setPoints(10);
        one.setUsedCards(20);
        Player two=new Player("two",2);
        two.setPoints(10);
        two.setUsedCards(22);
        Player three=new Player("three",3);
        three.setPoints(12);
        three.setUsedCards(25);
        players.put("one",one);
        players.put("two",two);
        players.put("three",three);
        List<Player> sorted=new ArrayList<>(players.values());
        sorted.sort(Comparator.comparing(Player::getPoints).thenComparing(Player::getUsedCards).reversed());
        String winners=sorted.stream().map(x->x.finalPoints()).collect(Collectors.joining("\n"));
        assert (sorted.get(0)==three);

    }

}