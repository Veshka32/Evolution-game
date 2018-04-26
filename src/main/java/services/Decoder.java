package services;

import com.google.gson.Gson;
import entities.Move;

public class Decoder {

    public Move decode(String message) {
        Gson json=new Gson();
        Move move=json.fromJson(message,Move.class);
        return move;
    }
}
