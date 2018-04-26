package entities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public class Card {
    private String extraProperty;
    private String property;
    private final int id;

    public Card(int type, int id) {
        switch (type) {
            case 0:
                property = "Swimming";
                break;
            case 1:
                property = "Big";
                break;
            case 2:
                property = "Parasite";
                extraProperty = "Fat";
        }
        this.id = id;
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

