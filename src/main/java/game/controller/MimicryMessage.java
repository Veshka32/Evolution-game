package game.controller;

import game.constants.Property;

import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
class MimicryMessage extends ExtraMessage {
    List <Integer> victims;

    public MimicryMessage(){}

    MimicryMessage(String name, int id, String name1, int id1, Property type, List<Integer> victims){
        super(name,id,name1,id1,type);
        this.victims=victims;
    }
}
