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
    ArrayList<Card> cards;

    public Player(String login){
        this.login=login;
        cards=new ArrayList<>(6);
        Random r=new Random();
        for (int i=0;i<6;i++){
            cards.add(new Card(r.nextInt(3)));
        }
    }

    public String getCards(){
        StringBuilder builder=new StringBuilder();
        builder.append(cards.stream().map(x->x.convertToJsonString()).collect(Collectors.joining("/")));
        String result=builder.toString();
        return result;
    }

//    public String getCards(){
//        return cards.stream().map(Object::toString).collect(Collectors.joining("/"));
//    }
}
