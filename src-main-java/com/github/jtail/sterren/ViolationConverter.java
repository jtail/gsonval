package com.github.jtail.sterren;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.PackageHack;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts constraint violations list into a tree json structure.
 */
@Slf4j
public class ViolationConverter implements Function<Iterable<? extends ConstraintViolation<?>>, JsonObject> {
    public DecimalFormat INDEX_FORMAT = new DecimalFormat("[0]");

    @Override
    public JsonObject apply(Iterable<? extends ConstraintViolation<?>> violations) {
        return merge(violations, new JsonObject()).getAsJsonObject();
    }

    public JsonElement merge(Iterable<? extends ConstraintViolation<?>> violations, JsonElement structuralErrors) {
        if (structuralErrors.isJsonArray()) {
            return structuralErrors;
        } else {
            JsonObject object = structuralErrors.getAsJsonObject();
            JsonObject ret = PackageHack.deepCopy(object);
            for (ConstraintViolation<?> violation : violations) {
                fork(ret, violation.getPropertyPath().iterator(), violation.getMessage(), Optional.of(object));
            }
            return ret;
        }
    }

    private void fork(JsonObject parent, Iterator<Path.Node> it, String message, Optional<JsonObject> structuralParent) {
        Path.Node node = it.next();
        String name = node.getName();
        JsonElement child = parent.get(name);

        Optional<JsonElement> structural = structuralParent.map(x -> x.get(name));
        String path = node.toString();
        if (!Objects.equals(path, name)) {
            String index = extractIndex(name, path);
            JsonObject object = new JsonObject();
            parent.add(index, object);
        } else {
            fork(parent, it, message, name, child, structural);
        }
    }

    private void fork(JsonObject parent, Iterator<Path.Node> it, String message, String name, JsonElement child, Optional<JsonElement> structural) {
        if (structural.map(JsonElement::isJsonArray).orElse(false)) {
            parent.add(name, structural.get());
            log.debug("Ignoring javax.validation [{}] in favor of structural [{}]", message, structural.get());
        } else if (it.hasNext()) {
            branch(parent, it, message, name, child, toObject(structural));
        } else {
            leaf(parent, name, child).add(new JsonPrimitive(message));
        }
    }

    private void branch(JsonObject parent, Iterator<Path.Node> it, String message, String name, JsonElement child, Optional<JsonObject> struct) {
        if (child == null) {
            JsonObject object = new JsonObject();
            parent.add(name, object);
            fork(object, it, message, struct);
        } else if (child.isJsonObject()) {
            fork(child.getAsJsonObject(), it, message, struct);
        } else {
            throw new IllegalStateException(name);
        }
    }

    private JsonArray leaf(JsonObject parent, String name, JsonElement child) {
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

    private String extractIndex(String name, String path) {
        try {
            return String.valueOf(INDEX_FORMAT.parse(path.substring(name.length())).intValue());
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Optional<JsonObject> toObject(Optional<JsonElement> se) {
        return se.map(x -> x.isJsonObject() ? (JsonObject) x : null);
    }

}
