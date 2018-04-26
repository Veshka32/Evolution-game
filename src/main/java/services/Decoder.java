package services;

import com.google.gson.Gson;
import entities.Move;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;

public class Decoder implements javax.websocket.Decoder.Text<Move> {

    @Override
    public Move decode(String s) throws DecodeException {
        Gson json=new Gson();
        Move move=json.fromJson(s,Move.class);
        return move;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        System.out.println("decoder init");

    }

    @Override
    public void destroy() {

    }
}
