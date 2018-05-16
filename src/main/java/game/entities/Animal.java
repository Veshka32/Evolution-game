package game.entities;

import game.constants.Constants;
import game.controller.GameException;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import java.util.*;
import java.util.stream.Collectors;

public class Animal {
    List<String> propertyList = new ArrayList<>();
    int id;
    int totalHungry = 1;
    int currentHungry;
    String owner;
    int cooperateToAnimal; //default 0
    int communicateToAnimal;
    int symbiosToAnimal;

    int fat;
    int flagForSort;

    public Animal(int id, String player) {
        this.id = id;
        owner = player;
    }

    public void addProperty(String property) throws GameException {
        if (property.equals("Scavenger") && propertyList.contains("Predator"))
            throw new GameException("Predator cannot be a scavenger");

        if (property.equals("Predator") && propertyList.contains("Scavenger"))
            throw new GameException("Scavenger cannot be a predator");

        if (!(property.equals("Fat")) && propertyList.contains(property))
            throw new GameException("This animal already hasAnimal property: " + property);


        propertyList.add(property);
        if (property.equals("Fat")) fat++;
        if (property.equals("Predator") || property.equals("Big")) totalHungry++;
        if (property.equals("Parasite")) totalHungry += 2;
    }

    public void addDoubleProperty(String property, int id) throws GameException {

        if (property.equals("Cooperation")) {
            checkIfCooperate(id);
            cooperateToAnimal = id;
            flagForSort = Constants.FLAG_FOR_COOPERATION.getValue();
        }

        if (property.equals("Communication")) {
            checkIfCooperate(id);
            communicateToAnimal=id;
            flagForSort = Constants.FLAG_FOR_COMMUNICATION.getValue();
        }

        propertyList.add(property);
    }

    private void checkIfCooperate(int id) throws GameException {
        if  (cooperateToAnimal==id || communicateToAnimal==id)
            throw new GameException("These animals are already helping each other!");
    }

    public String getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public int getFlagForSort() {
        return flagForSort;
    }

    public boolean hasProperty(String property){
        return propertyList.contains(property);
    }



}
