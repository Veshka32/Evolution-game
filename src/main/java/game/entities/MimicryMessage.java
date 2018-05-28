package game.entities;

import java.util.List;

public class MimicryMessage extends ExtraMessage {
    List <Integer> victims;

    public MimicryMessage(String name, int id, String name1, int id1,String type,List<Integer> victims){
        super(name,id,name1,id1,type);
        this.victims=victims;
    }
}
