package services;

import com.google.gson.Gson;
import entities.Move;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class Decoder {

    public Move decode(String message) {
        Gson json=new Gson();
        Move move=json.fromJson(message,Move.class);
        return move;
    }
}
