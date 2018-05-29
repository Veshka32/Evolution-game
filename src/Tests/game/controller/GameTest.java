package game.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void convertToJsonString() {
        Gson gson=new Gson();
        JsonElement element=new JsonObject();
        element.getAsJsonObject().addProperty("error","errortext");
        String result=gson.toJson(element);
        System.out.println(result);
    }
}