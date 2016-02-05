package com.github.jtail.sterren;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Converts constraint violations list into a tree json structure.
 */
public class ViolationConverter implements Function<Iterable<? extends ConstraintViolation<?>>, JsonObject> {

    @Override
    public JsonObject apply(Iterable<? extends ConstraintViolation<?>> violations) {
        JsonObject ret = new JsonObject();
        for (ConstraintViolation<?> violation : violations) {
            getBranch(ret, violation.getPropertyPath().iterator()).add(violation.getMessage());
        }
        return ret;
    }

    private JsonArray getBranch(JsonObject parent, Iterator<Path.Node> it) {
        Path.Node node = it.next();
        String name = node.getName();
        JsonElement child = parent.get(name);
        if (it.hasNext()) {
            if (child == null) {
                JsonObject object = new JsonObject();
                parent.add(name, object);
                return getBranch(object, it);
            } else if (child.isJsonObject()) {
                return getBranch(child.getAsJsonObject(), it);
            } else {
                throw new IllegalStateException(name);
            }
        } else {
            if (child == null) {
                JsonArray array = new JsonArray();
                parent.add(name, array);
                return array;
            } else if (child.isJsonArray()) {
                return child.getAsJsonArray();
            } else {
                throw new IllegalStateException(name);
            }
        }
    }
}
