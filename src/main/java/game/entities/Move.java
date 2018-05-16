package game.entities;

import com.google.gson.Gson;

public class Move {
    String player;
    String move;
    String property;
    int cardId;
    int animalId;
    int secondAnimalId;
    String log;

    public Move(String player,int cardId,int animalId,int secondAnimalId,String move,String property,String log){
        this.move = move;
        this.cardId = cardId;
        this.animalId=animalId;
        this.secondAnimalId=secondAnimalId;
        this.player = player;
        this.property=property;
        this.log=log;
    }

    public String getMove() {
        return move;
    }

    public String getPlayer() {
        return player;
    }

    public String getLog(){
        return log;
    }

    public int getCardId(){return cardId;}

    public int getAnimalId(){return animalId;}

    public int getSecondAnimalId(){return secondAnimalId;}

    public String getProperty(){return property;}


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
