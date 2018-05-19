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
    transient String owner;
    private transient int cooperateToAnimal; //default 0
    private transient int communicateToAnimal;
    transient int sortIndex; //comm=2, coop=1
    int symbiosToAnimal;

    int fat;


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
            throw new GameException("This animal already has property: " + property);

        propertyList.add(property);
        if (property.equals("Fat")) fat++;
        if (property.equals("Predator") || property.equals("Big")) totalHungry++;
        if (property.equals("Parasite")) totalHungry += 2;
    }

    public void addDoubleProperty(String property, int id) throws GameException {

        if (property.equals("Cooperation")) {
            cooperateToAnimal = id;
            sortIndex=1; //update anyway
        }

        if (property.equals("Communication")) {
            communicateToAnimal=id;
            if (sortIndex!=1) sortIndex=2; //update only if not coop
        }

        propertyList.add(property);
    }

    public String getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public int getSortIndex(){return sortIndex;}

    public boolean hasProperty(String property){
        return propertyList.contains(property);
    }



}
