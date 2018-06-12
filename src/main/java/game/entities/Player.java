package game.entities;

import com.google.gson.annotations.Expose;
import game.constants.Constants;
import game.controller.GameException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Player implements Serializable {

    transient private int cardNumber = Constants.START_NUMBER_OF_CARDS.getValue();
    transient private int points;
    transient private boolean leftGame=false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    private int order;
    private int usedCards;

    //include json
    @Expose
    private String name;
    //@Expose
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Card> cards = new ArrayList<>();
    @Expose
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "owner") //no player - no animals //orphanRemoval=true
    private Map<Integer, Animal> animals = new HashMap<>();
    private boolean doEat = false;

    public Player(){}

    public Player(String login,int order) {
        this.name = login;this.order=order;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public void setDoEat(boolean bool) {
        doEat = bool;
    }

    public void resetGrazing() {
        animals.forEach((k,v)->v.setDoGrazing(false));
    }

    public void leftGame(){
        leftGame=true;
    }

    public void backToGame(){
        leftGame=false;
    }

    public boolean isLeftGame(){
        return leftGame;
    }

    public boolean isDoEat() {
        return doEat;
    }

    public void addCard(Card card) {
        cards.add(card);
        cardNumber--;
    }

    public void deleteCard(int id) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getId() == id) {
                cards.remove(i);
                break;
            }
        }
    }

    public void setCardNumber() {
        if (!hasAnimals() && !hasCards())
            cardNumber = Constants.START_NUMBER_OF_CARDS.getValue();
        else cardNumber = animals.size() + Constants.NUMBER_OF_EXTRA_CARD.getValue();
    }

    public int getPoints() {
        animals.forEach((k,v)->points+=v.hungry); //how to calculate double cards?
        return points;
    }

    public String finalPoints(){
        return name+": "+points+" points, "+usedCards+" cards used.";
    }

    public void addAnimal(Animal animal) {
        animals.put(animal.getId(), animal);
    }

    //return true if player has any scavenger that can be fed; feed only one of them. Player should choose which one, actually
    public boolean feedScavenger() {
        for (Animal animal : animals.values()
                ) {
            if (animal.hasProperty("Scavenger") && animal.hungry > 0) {
                animal.eatFish(1);
                return true;
            }
        }
        return false;
    }


    public void connectAnimal(int id1, int id2, String property) throws GameException {

        if (animals.size() < 2) throw new GameException("You don't have enough animals");

        if (!(animals.containsKey(id1) && animals.containsKey(id2)))
            throw new GameException("It's not your animal(s)");

        if (id1 == 0 || id2 == 0)
            throw new GameException("You must pick two animals to play this card");

        else if (id1 == id2)
            throw new GameException("You must play this property on two different animals");

        Animal animal = animals.get(id1);
        Animal animal2 = animals.get(id2);

        switch (property) {
            case "Communication":
                if (animal.isCommunicate(id2) || animal.isCooperate(id2))
                    throw new GameException("These animals are already helping each other");
                else {
                    animal.setCommunicateTo(id2);
                    animal2.setCommunicateTo(id1);
                    animal.addProperty(property);
                    animal2.addProperty(property);
                }
                break;
            case "Cooperation":
                if (animal.isCommunicate(id2) || animal.isCooperate(id2))
                    throw new GameException("These animals are already helping each other");
                else {
                    animal.setCooperateTo(id2);
                    animal2.setCooperateTo(id1);
                    animal.addProperty(property);
                    animal2.addProperty(property);
                }
                break;
            case "Symbiosis":
                if (animal.isInSymbiosis(id2) || animal.isSymbiontFor(id2))
                    throw new GameException("Animal #" + id1 + " is already in symbiosisWith with animal #" + id2);
                else {
                    animal.setSymbiosysWith(id2);
                    animal2.setSymbiontFor(id1);
                    animal.addProperty(property);
                    animal2.addProperty(property);
                }
        }
    }

    public void animalsDie() {
        Collection<Animal> all = animals.values();
        Iterator<Animal> it = all.iterator(); //to remove animal safety;
        while (it.hasNext()) {
            Animal animal = it.next();
            if (animal.hungry > 0 || animal.isPoisoned) {
                animal.die();
                usedCards += animal.hungry;
                it.remove();
            }
        }
    }

    public void resetFedFlag() {
        animals.forEach((k,v)->v.fedFlag=false);
    }

    public void resetFields() {
        for (Animal an : animals.values()
                ) {
            an.setHungry();
            an.attackFlag = false;
            an.fedFlag = false;
            an.setDoPiracy(false);
            an.setDoGrazing(false);
            doEat = false;
        }
    }

    public List<Integer> canRedirect(Animal predator, int victim) {
        ArrayList<Integer> canAttack = new ArrayList<>();

        for (Animal an : animals.values()) {
            if (an.getId() == victim || an.hasProperty("Mimicry")) continue;
            try {
                predator.attack(an);
                canAttack.add(an.getId());
            } catch (GameException e) {
                //if catch ex, canAttack won't be increase
            }
        }
        return canAttack;
    }
    void increaseUsedCards(){
        usedCards++;
    }

    public Animal getAnimal(int id) {
        return animals.get(id);
    }

    public void deleteAnimal(int id) {
        animals.remove(id);
    }

    public int getUsedCards() {
        return usedCards;
    }

    private boolean hasCards() {
        return !cards.isEmpty();
    }

    private boolean hasAnimals() {
        return !animals.isEmpty();
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUsedCards(int usedCards) {
        this.usedCards = usedCards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Map<Integer, Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Map<Integer, Animal> animals) {
        this.animals = animals;
    }

    public int getId() {
        return id;
    }
}
