package game.entities;

import game.controller.GameException;

import java.util.*;

public class Animal {
    List<String> propertyList = new ArrayList<>();
    int id;
    int totalHungry = 1;
    int cooperateTo; //default 0
    int communicateTo;
    transient int symbiosToAnimal;
    int currentHungry;
    int chain;
    int fat;

    transient String owner;



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
            cooperateTo = id;
        } else if (property.equals("Communication")) {
            communicateTo=id;
        }

        propertyList.add(property);
    }

    public String getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public boolean hasProperty(String property){
        return propertyList.contains(property);
    }



}
