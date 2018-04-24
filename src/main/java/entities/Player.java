package entities;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Player {
    String login;
    //List<Animal> animals;
    ArrayList<Card> cards=new ArrayList<>();

    public Player(String login){
        this.login=login;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public String getCards(){
        StringBuilder builder=new StringBuilder();
        builder.append(cards.stream().map(x->x.convertToJsonString()).collect(Collectors.joining("/")));
        String result=builder.toString();
        return result;
    }

   public boolean hasCards(){
        return !cards.isEmpty();
   }
    //    public String getCards(){
//        return cards.stream().map(Object::toString).collect(Collectors.joining("/"));
//    }
}
