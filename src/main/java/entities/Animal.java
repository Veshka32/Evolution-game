package entities;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Animal {
    List<String> propertyList=new ArrayList<>();
    int id;
    int totalHungry;
    int currentHungry;
    String owner;

    public Animal(int id,String player){
        this.id=id;
        owner=player;
    }

    public void addProperty(String property){
        propertyList.add(property);
    }

    public String getProperties(){
        return propertyList.stream().collect(Collectors.joining(","));
    }

    public String convertToJsonString(){
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("id",id);
        if (!propertyList.isEmpty())
            builder.add("properties", getProperties());
        JsonObject json =builder.build();
        return json.toString();
    }
}
