package game.entities;

import game.controller.GameException;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Animal {
    List<String> propertyList = new ArrayList<>();
    int id;
    int totalHungry;
    int currentHungry;
    String owner;

    public Animal(int id, String player) {
        this.id = id;
        owner = player;
    }

    public void addProperty(String property, String actor) throws GameException {
        if (property.equals("Scavenger")&&propertyList.contains("Predator")) throw new GameException("Predator cannot be a scavenger");
        if (property.equals("Predator")&&propertyList.contains("Scavenger")) throw new GameException("Scavenger cannot be a predator");
        if ( !(property.equals("Fat")&&propertyList.contains(property)) ) throw new GameException("This animal already has property: "+property);
        if (property.equals("Parasite") && actor.equals(owner)) throw new GameException("You can't play Parasite on your own animal");
        propertyList.add(property);
    }

    public String getOwner(){
        return owner;
    }

    public int getId(){return id;}


}
