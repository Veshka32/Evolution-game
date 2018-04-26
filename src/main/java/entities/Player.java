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
    private final String login;
    List<Animal> animals;
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

    public void deleteCard(int id){
        for (int i=0;i<cards.size();i++){
            if (cards.get(i).getId()==id){
                cards.remove(i);
                break;
            }
        }
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
    }

   public boolean hasCards(){
        return !cards.isEmpty();
   }
}
