package game.entities;

import com.google.gson.annotations.Expose;

import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Card implements Serializable {
    @Expose
    private String extraProperty;
    @Expose
    private String property;

    @Id
    @Expose
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

    public int getId(){
        return id;
    }

    public String getExtraProperty() {

        return extraProperty;
    }

}

