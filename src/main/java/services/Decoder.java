package services;

import entities.Move;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class Decoder {

    public Move decode(String message) {
        JsonReader reader=Json.createReader(new StringReader(message));
        JsonObject json=reader.readObject();
        Move move=new Move(json.getString("player"),json.getString("opponent"),json.getString("move"));
        return move;
    }
}
