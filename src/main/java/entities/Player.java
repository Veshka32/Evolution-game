package entities;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Player {
    String login;
    List<Animal> animals;
    ArrayList<Integer> cards;

    public Player(String login){
        this.login=login;
        cards=new ArrayList<>(6);
        Random r=new Random();
        for (int i=0;i<6;i++){
            cards.add(r.nextInt(10));
        }
    }

    public String getCards(){
        return cards.stream().map(Object::toString).collect(Collectors.joining("/"));
    }
}
