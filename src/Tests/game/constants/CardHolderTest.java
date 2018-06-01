package game.constants;

import game.entities.Card;
import org.junit.Test;

import java.util.List;


public class CardHolderTest {
    @Test
    public void cardHolder(){
        CardHolder holder=new CardHolder();
        List<Card> test=holder.getCards();
        assert (test.size()==84);
        List<Card> pop=holder.getCards();
        assert (pop.size()==84);
        assert (test.containsAll(pop));


    }

}