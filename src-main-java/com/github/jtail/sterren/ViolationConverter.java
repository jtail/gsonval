package com.github.jtail.sterren;

import com.github.jtail.gson.BJson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.PackageHack;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts constraint violations list into a tree json structure.
 */
@Slf4j
public class ViolationConverter implements Function<Iterable<? extends ConstraintViolation<?>>, JsonObject> {

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
        if (structural.map(JsonElement::isJsonArray).orElse(false)) {
            parent.add(name, structural.get());
            log.debug("Ignoring javax.validation [{}] in favor of structural [{}]", message, structural.get());
        } else if (it.hasNext()) {
            if (child == null) {
                JsonObject object = new JsonObject();
                parent.add(name, object);
                fork(object, it, message, toObject(structural));
            } else if (child.isJsonObject()) {
                fork(child.getAsJsonObject(), it, message, toObject(structural));
            } else {
                throw new IllegalStateException(name);
            }
        } else {
            if (child == null) {
                parent.add(name, BJson.arr(message));
            } else if (child.isJsonArray()) {
                child.getAsJsonArray().add(message);
            } else {
                throw new IllegalStateException(name);
            }
        }
    }

    private Optional<JsonObject> toObject(Optional<JsonElement> se) {
        return se.map(x -> x.isJsonObject() ? (JsonObject) x : null);
    }

}
