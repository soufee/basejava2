package ru.shoma.webapp.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public class JsonSectionAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) object.get(CLASSNAME);
        String className = prim.getAsString();
        try {
            Class clazz = Class.forName(className);
            return context.deserialize(object.get(INSTANCE), clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext context) {
        JsonObject value = new JsonObject();
        value.addProperty(CLASSNAME, t.getClass().getName());
        JsonElement element = context.serialize(t);
        value.add(INSTANCE, element);
        return value;
    }
}
