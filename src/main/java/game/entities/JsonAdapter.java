package game.entities;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

class JsonAdapter implements JsonSerializer<List<?>> {

    @Override
    public JsonElement serialize(List<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null || src.isEmpty()) // exclusion is made here
            return null;

        JsonArray array = new JsonArray();

        for (Object child : src) {
            JsonElement element = context.serialize(child);
            array.add(element);
        }

        return array;
    }

//    public static void main(String[] args) throws GameException {
//        Animal animal=new Animal(1,new Player("test",1));
//        animal.setObserver(new Game());
//        animal.addProperty(Property.PREDATOR);
//        Gson gson=new GsonBuilder().registerTypeHierarchyAdapter(List.class, new E()).excludeFieldsWithoutExposeAnnotation().create();
//        System.out.println(gson.toJson(animal));
//
//
//    }
}

