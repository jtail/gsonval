package com.github.jtail.sterren;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
                fork(ret, violation.getPropertyPath().iterator(), Optional.of(object)).ifPresent(
                        a -> a.add(violation.getMessage())
                );
            }
            return ret;
        }
    }

    private Optional<JsonArray> fork(JsonObject parent, Iterator<Path.Node> it, Optional<JsonObject> struct) {
        Path.Node node = it.next();
        String name = node.getName();

        Optional<JsonElement> struct1 = struct.map(x -> x.get(name));
        String path = node.toString();
        if (!Objects.equals(path, name)) {
            String index = extractIndex(name, path);
            return fork(branch(parent, name), it, index, toObject(struct1).map(x -> x.get(name)));
        } else {
            return fork(parent, it, name, struct1);
        }
    }

    private Optional<JsonArray> fork(JsonObject parent, Iterator<Path.Node> it, String name, Optional<JsonElement> struct) {
        if (struct.map(JsonElement::isJsonArray).orElse(false)) {
            parent.add(name, struct.get());
            log.debug("Ignoring javax.validation in favor of structural [{}]", struct.get());
            return Optional.empty();
        } else if (it.hasNext()) {
            return fork(branch(parent, name), it, toObject(struct));
        } else {
            return Optional.of(leaf(parent, name));
        }
    }

    private JsonObject branch(JsonObject parent, String name) {
        JsonElement child = parent.get(name);
        if (child == null) {
            JsonObject object;
            object = new JsonObject();
            parent.add(name, object);
            return object;
        } else if (child.isJsonObject()) {
            return child.getAsJsonObject();
        } else {
            throw new IllegalStateException(name);
        }
    }

    private JsonArray leaf(JsonObject parent, String name) {
        JsonElement child = parent.get(name);
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
