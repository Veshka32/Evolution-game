package services;

import entities.Move;
import model.Game;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Decoder {

    public Move decode(String message) {
        JsonReader reader=Json.createReader(new StringReader(message));
        JsonObject json=reader.readObject();
        Move move=new Move(json.getString("player"),json.getString("opponent"),json.getString("move"));
        return move;
    }
}
