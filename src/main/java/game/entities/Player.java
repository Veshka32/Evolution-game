package game.entities;

import game.constants.Constants;
import game.controller.CC;
import game.controller.Graph;
import game.controller.GameException;

import java.util.*;

public class Player {
    private final String name;
    List<Card> cards = new ArrayList<>();
    Map<Integer, Animal> animals = new HashMap<>();
    private transient int cardNumber=Constants.START_NUMBER_OF_CARDS.getValue();
    private transient int points;

    public Player(String login) {
        this.name = login;
    }

    public String getName() {
        return name;
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

    public void setCardNumber(){
        if (!hasAnimals() && !hasCards())
            cardNumber=Constants.START_NUMBER_OF_CARDS.getValue();
        else cardNumber=animals.size()+1;
    }

    public boolean needCards(){
        return cardNumber>0;
    }

    public int getPoints(){
        for (Animal animal:animals.values()){
            points+=animal.totalHungry; //how to calculate double cards?
        }
        return points;
    }

    public void addAnimal(Animal animal) {
        animals.put(animal.getId(), animal);
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
                if (animal.isInSymbiosis(id2))
                    throw new GameException("Animal #" + id1 + " is already in symbiosis with animal #" + id2);
                else {
                    animal.setSymbiosysWith(id2);
                    animal2.setSymbiontFor(id1);
                    animal.addProperty(property);
                    animal2.addProperty(property);
                }
        }
    }

    public void animalDie() {
        Collection<Integer> all = animals.keySet(); //for safe removing from map while iterating
        for (int id : all
                ) {
            Animal animal = animals.get(id);
            if (animal.currentHungry > 0) animal.die();
            if (animal.isPoisoned) animal.die();
        }
    }

    public void resetFedFlag() {
        for (Animal an : animals.values()
                ) {
            an.fedFlag = false;
        }
    }

    public void resetFields() {
        for (Animal an : animals.values()
                ) {
            an.currentHungry = an.totalHungry;
            an.attackFlag = false;
        }
    }

    public Animal getAnimal(int id) {
        return animals.get(id);
    }

    public boolean hasAnimal(int id) {
        return animals.containsKey(id);
    }

    public int howManyAnimals() {
        return animals.size();
    }

    public boolean hasCards() {
        return !cards.isEmpty();
    }

    public boolean hasAnimals() {
        return !animals.isEmpty();
    }
}
