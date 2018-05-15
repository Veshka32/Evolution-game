package game.entities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    List<Animal> animals=new ArrayList<>();
    ArrayList<Card> cards=new ArrayList<>();

    public Player(String login){
        this.name=login;
    }

    public String getName(){
        return name;
    }



    public JsonArray getCards(){
        Gson json=new Gson();
        JsonElement element=json.toJsonTree(cards, new TypeToken<List<Card>>() {}.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public void addCard(Card card){
        cards.add(card);
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

    public Animal getAnimal(int id){
        for (int i=0;i<animals.size();i++){
            if (animals.get(i).getId()==id){
                return animals.get(i);
            }
        }
        return null;
    }

   public boolean hasCards(){
        return !cards.isEmpty();
   }
}
