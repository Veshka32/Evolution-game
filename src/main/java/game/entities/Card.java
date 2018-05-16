package game.entities;

import game.constants.Properties;

import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public class Card {
    private String extraProperty;
    private String property;
    private Properties prop;
    private final int id;

    public Card(int id,String property,String extraProperty){
        this.id=id;
        this.property=property;
        this.extraProperty=extraProperty;
    }

    public Card(int id,String property){
        this.id=id;
        this.property=property;
    }

    public String convertToJsonString() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("id", id)
                .add("property", property);
        if (extraProperty != null)
            builder.add("extraProperty", extraProperty);

        return builder.build().toString();
    }

    public int getId(){
        return id;
    }
}

