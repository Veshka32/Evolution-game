package game.entities;

import com.google.gson.Gson;

public class Move {
    String player;
    String move;
    int id;

    public Move(String player, int id, String move) {
        this.move = move;
        this.id = id;
        this.player = player;
    }

    public String getMove() {
        return move;
    }

    public String getPlayer() {
        return player;
    }

    public int getId(){return id;}


    public String toString() {
        Gson json = new Gson();
        String s = json.toJson(this);
        return s;
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
