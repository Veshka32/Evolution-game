package game.entities;

import game.controller.CC;
import game.controller.Graph;
import game.controller.GameException;

import java.util.*;

public class Player {
    private final String name;
    List<Card> cards=new ArrayList<>();
    private transient Graph animalGraph =new Graph(84);
    private Map<Integer,Animal> animals=new HashMap<>();

    public Player(String login){
        this.name=login;
    }

    public String getName(){
        return name;
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
        int id=animal.getId();
        animals.put(id,animal);
        buildComponents();
    }

    public void connectAnimal(int id1,int id2,String type) throws GameException {
        if (!(hasAnimal(id1) && hasAnimal(id2)))
            throw new GameException("It's not your animal(s)");

        CC cc=new CC(animalGraph);
        if (cc.connected(id1,id2)) throw new GameException("These animals are already helping each other!");

        animals.get(id1).addDoubleProperty(type, id2);
        animals.get(id2).addDoubleProperty(type, id1);
        union(id1,id2);
        buildComponents();
    }

    public void union(int a, int b){
        animalGraph.addEdge(a,b);
    }

    public void buildComponents(){
        CC cc=new CC(animalGraph);

        for (Animal animal:animals.values()) {
            animal.chain=cc.id(animal.getId());
        }
    }

    public Animal getAnimal(int id){
        return animals.get(id);
    }

    public boolean hasAnimal(int id){
       return animals.containsKey(id);
    }

    public int howManyAnimals(){
        return animals.size();
    }

   public boolean hasCards(){
        return !cards.isEmpty();
   }

   public boolean hasAnimals(){return !animals.isEmpty();}



//    public JsonArray getCards(){
//        Gson json=new Gson();
//        JsonElement element=json.toJsonTree(cards, new TypeToken<List<Card>>() {}.getType());
//        JsonArray jsonArray = element.getAsJsonArray();
//        return jsonArray;
//    }
}
