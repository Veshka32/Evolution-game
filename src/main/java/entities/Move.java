package entities;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Move {
    String player;
    String move;
    String opponent;

    public Move(String player, String opponent,String move) {
        this.move = move;
        this.opponent=opponent;
        this.player = player;
    }

    public String getMove() {
        return move;
    }

    public String getPlayer() {
        return player;
    }


    public String toString() {
        ArrayList<String> fields = new ArrayList<>(3);
        for (Field f : Move.class.getDeclaredFields()) {
            try {
                fields.add(f.get(this).toString());
            } catch (IllegalAccessException ex) {
            }
        }
        return fields.toString();
    }

//    public static void main(String[] args) throws JsonProcessingException,IOException {
//        Move test=new Move("pl","move","i55");
//        Gson gson=new Gson();
//        String json=gson.toJson(test);
//        System.out.println(json);
//
//        test=gson.fromJson(json,Move.class);
//        System.out.println(test.getMove());
//        System.out.println(test.getPlayer());
//
////        ObjectMapper mapper=new ObjectMapper();
////        String jsonToString=mapper.writeValueAsString(test);
////        System.out.println(jsonToString);
////
////        ObjectMapper mapper1=new ObjectMapper();
////        Move test1=mapper1.readValue(jsonToString,Move.class);
////        System.out.println(test1.move);
//
//    }
}
