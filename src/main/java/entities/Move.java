package entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Move {
    String player;
    String opponent;
    int move;

    public Move(String player,String opponent,int move){
        this.move=move;
        this.opponent=opponent;
        this.player=player;
    }

    public String toString(){
        ArrayList<String> fields=new ArrayList<>(3);
        for (Field f : Move.class.getDeclaredFields()) {

            try{fields.add(f.get(this).toString());} catch (IllegalAccessException ex){}
        }
        return fields.toString();

    }
}
