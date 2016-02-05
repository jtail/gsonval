package com.github.jtail.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Utility class to construct JsonElements
 */
public class BJson {
    /**
     * Creates JsonArray from nested elements.
     * @param contents elements to be placed inside the array
     */
    public static JsonArray arr(JsonElement... contents) {
        return arr(a -> Stream.of(contents).forEach(a::add));
    }

    /**
     * Creates JsonArray with the content provided by callback.
     * @param consumer callback to insert elements
     */
    public static JsonArray arr(Consumer<JsonArray> consumer) {
        JsonArray object = new JsonArray();
        consumer.accept(object);
        return object;
    }

    /**
     * Shortcut for creating a single nested object.
     * @param name name of the nested object
     * @param nested nested object
     */
    public static JsonObject obj(String name, JsonElement nested) {
        return obj(Prop.val(name, nested));
    }

    /**
     * Shortcut for creating 2 nested objects
     * @param name1 name of the first nested object
     * @param nested1 first nested object
     * @param name2 name of the second nested object
     * @param nested2 second nested object
     */
    public static JsonObject obj(String name1, JsonElement nested1, String name2, JsonElement nested2) {
        return obj(Prop.val(name1, nested1), Prop.val(name2, nested2));
    }

    /**
     * Creates JsonObject with given properties.
     * @param properties name-value pair to be used as properties in the object being created.
     */
    public static JsonObject obj(Prop... properties) {
        return obj(o -> Stream.of(properties).forEach(p -> o.add(p.getName(), p.getValue())));
    }

    /**
     * Creates JsonObject with the content provided by callback.
     * @param consumer callback to fill object contents
     */
    public static JsonObject obj(Consumer<JsonObject> consumer) {
        JsonObject object = new JsonObject();
        consumer.accept(object);
        return object;
    }

}
