package entities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

public class Card {
    String extraProperty="null";
    String property;
    String color="green";

    public Card(Integer id){
        switch (id) {
            case 0: property="Swimming";color="blue";break;
            case 1: property="Big"; break;
            case 2: property="Parasite"; extraProperty="fat";
        }
    }

    public String convertToJsonString(){
        JsonProvider provider = JsonProvider.provider();
        JsonObject json = provider.createObjectBuilder()
                .add("id","card")
                .add("property", property)
                .add("color",color)
                .add("extraProperty",extraProperty)
                .build();
        String result=json.toString();
        return result;
    }

//    public Card(Property x, Property y){
//        mainProperty=x;
//        property=y;
//    }
//
//    public String showProperties() {
//        return mainProperty.getName()+"/"+property;
//    }
//
//    public void activate(int property){
//        if (property==1) mainProperty=this.property;
//    }
//
//    public Property play(){
//        return mainProperty;
//    }
}

