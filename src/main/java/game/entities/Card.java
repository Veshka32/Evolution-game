package game.entities;

import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Card implements Serializable {
    private String extraProperty;
    private String property;
    private int id;


    public Card(){}
    public Card(int id,String property,String extraProperty){
        this.id=id;
        this.property=property;
        this.extraProperty=extraProperty;
    }

    public Card(int id,String property){
        this.id=id;
        this.property=property;
    }

//    public String convertToJsonString() {
//        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
//        builder.add("id", id)
//                .add("property", property);
//        if (extraProperty != null)
//            builder.add("extraProperty", extraProperty);
//
//        return builder.build().toString();
//    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getExtraProperty() {

        return extraProperty;
    }

    public void setExtraProperty(String extraProperty) {
        this.extraProperty = extraProperty;
    }

    public static boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication") || property.equals("Symbiosis");
    }

}

