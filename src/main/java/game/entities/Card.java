package game.entities;

import com.google.gson.annotations.Expose;
import game.controller.Game;

import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Card implements Serializable {
    @Expose
    @Column(updatable = false,nullable = false)
    private String property;

    @Expose
    @Column(updatable = false)
    private String extraProperty; //use in Game.json

    @Id
    @Expose
    @Column(updatable = false)
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
}

