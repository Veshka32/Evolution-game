package game.entities;

import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Graph;
import game.controller.GameException;

import java.util.*;

public class Player {
    private final String name;
    List<Card> cards=new ArrayList<>();
    private transient Graph  animals=new Graph(84);
    private transient Map<Integer,Animal> animalMap=new HashMap<>();
    Collection<List<Animal>> groupedAnimals;

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
        animalMap.put(id,animal);
    }

    public void connectAnimal(int id1,int id2,String type) throws GameException {
        if (!(hasAnimal(id1) && hasAnimal(id2)))
            throw new GameException("It's not your animal(s)");

        CC cc=new CC(animals);
        if (cc.connected(id1,id2)) throw new GameException("These animals are already helping each other!");

        animalMap.get(id1).addDoubleProperty(type, id2);
        animalMap.get(id2).addDoubleProperty(type,id1);
        union(id1,id2,type);
        buildComponents();
    }

    public void union(int a, int b,String type){
        animals.addEdge(a,b);
    }

    public void buildComponents(){
        CC cc=new CC(animals);

        Map<Integer,List<Animal>> components=new HashMap<>();

        for (int id:animalMap.keySet()){
            components.put(cc.id(id),new ArrayList<>());      //
        }

        for (int id:animalMap.keySet()){
            int componentId=cc.id(id);
            components.get(componentId).add(animalMap.get(id));
        }
        groupedAnimals=components.values();
    }

    public Animal getAnimal(int id){
        return animalMap.get(id);
    }

    public boolean hasAnimal(int id){
       return animalMap.containsKey(id);
    }

    public int howManyAnimals(){
        return animalMap.size();
    }

   public boolean hasCards(){
        return !cards.isEmpty();
   }

   public boolean hasAnimals(){return !animalMap.isEmpty();}

    public static void main(String[] args) {
        Player test=new Player("test");
        Animal an1=new Animal(1,"test");
        Animal an2=new Animal(2,"test");
        Animal an3=new Animal(3,"test");
        Animal an4=new Animal(4,"pop");
        test.addAnimal(an1);
    }



//    public JsonArray getCards(){
//        Gson json=new Gson();
//        JsonElement element=json.toJsonTree(cards, new TypeToken<List<Card>>() {}.getType());
//        JsonArray jsonArray = element.getAsJsonArray();
//        return jsonArray;
//    }
}
