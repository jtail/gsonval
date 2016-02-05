package com.github.jtail.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;

/**
 * Holds name-value pair for JSON property
 */
@Getter
public class Prop {
    private String name;
    private JsonElement value;

    private Prop(String name, JsonElement value) {
        this.name = name;
        this.value = value;
    }

    public static Prop val(String name, Boolean value) {
        return val(name, new JsonPrimitive(value));
    }
    public static Prop val(String name, Character value) {
        return val(name, new JsonPrimitive(value));
    }

    public static Prop val(String name, Number value) {
        return val(name, new JsonPrimitive(value));
    }

    public static Prop val(String name, String value) {
        return val(name, new JsonPrimitive(value));
    }

    public static Prop val(String name, JsonElement value) {
        return new Prop(name, value);
    }
}
