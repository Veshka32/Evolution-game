package game.constants;

import game.controller.Deck;
import game.entities.Card;
import org.junit.Test;

import java.util.List;


public class DeckTest {
    @Test
    public void cardHolder(){
        Deck holder=new Deck();
        List<Card> test=holder.getCards();
        assert (test.size()==84);
        List<Card> pop=holder.getCards();
        assert (pop.size()==84);
        assert (test.containsAll(pop));
    }

}