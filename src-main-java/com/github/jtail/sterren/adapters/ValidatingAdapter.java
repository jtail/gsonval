package com.github.jtail.sterren.adapters;

import com.github.jtail.gson.BJson;
import com.github.jtail.sterren.ObjectValidationException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Deque;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ValidatingAdapter<T> extends TypeAdapter<T> {
    private final TypeAdapter<T> delegate;
    private final BiConsumer<T, Optional<JsonElement>> validationCallback;
    private TypeToken<T> type;

    public ValidatingAdapter(TypeAdapter<T> delegate, BiConsumer<T, Optional<JsonElement>> validationCallback, TypeToken<T> type) {
        this.delegate = delegate;
        this.validationCallback = validationCallback;
        this.type = type;
    }

    @Override
    public T read(JsonReader in) throws IOException {
        if (in instanceof ErrorTrackingReader) {
            return readTracked((ErrorTrackingReader) in);
        } else {
            // This is top-level object
            ErrorTrackingReader reader = new ErrorTrackingReader(in);
            T object = readTracked(reader);
            Optional<JsonElement> structural = Optional.ofNullable(reader.getErrors().peek()).map(Pair::getValue);

            if (validationCallback != null) {
                validationCallback.accept(object, structural);
            }
            structural.ifPresent(error -> {
                throw new ObjectValidationException("Validation failed", error);
            });
            return object;
        }
    }

    private T readTracked(ErrorTrackingReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case BEGIN_OBJECT:
                return readNested(in, key -> key.substring(1));
            case BEGIN_ARRAY:
                return readNested(in, key -> key.substring(1, key.length() - 1));
            default:
                return delegate(in);
        }
    }

    /**
     * Reads nested structure (object or array)
     *
     * @param in         reader to read
     * @param translator function that converts name from path provided by JsonReader to the desired
     * @return read object.
     * @throws IOException if reader throws one
     */
    private T readNested(ErrorTrackingReader in, Function<String, String> translator) throws IOException {
        Deque<Pair<String, JsonElement>> errors = in.getErrors();
        int before = errors.size();
        T o = delegate(in);
        if (errors.size() > before) {
            String path = in.getPath();
            JsonObject group = new JsonObject();
            do {
                Pair<String, JsonElement> err = errors.pop();
                group.add(translator.apply(err.getKey().substring(path.length())), err.getValue());
            } while (errors.size() > before);
            errors.push(Pair.of(path, group));
        }
        return o;
    }

    private T delegate(ErrorTrackingReader in) throws IOException {
        try {
            return delegate.read(in);
        } catch (NumberFormatException e) {
            processError(in, e.getMessage());
        } catch (JsonSyntaxException e) {
            Throwable t = e.getCause();
            processError(in, (t instanceof NumberFormatException) ? t.getMessage() : e.getMessage());
        }
        // Continue parsing without setting this field
        return null;
    }

    private void processError(ErrorTrackingReader reader, String message) throws IOException {
        String value = reader.nextString();
        String errorText = getErrorMessage(message, value, delegate.getClass());
        reader.pushError(reader.getPath(), BJson.arr(new JsonPrimitive(errorText)));
    }

    private String getErrorMessage(String message, String value, Class<? extends TypeAdapter> clazz) {
        if (clazz.isAnonymousClass() && clazz.getName().startsWith(Gson.class.getPackage().getName())) {
            // Anonymous classes from Gson are pretty useless for error messages (and provide useless error messages as well)
            return MessageFormat.format("Unable to parse `{0}` as [{1}]", value, type.getType(), clazz.getName(), message);
        } else {
            return MessageFormat.format("Unable to parse `{0}` as [{1}] using {2}: {3}", value, type.getType(), clazz.getName(), message);
        }
    }

    /**
     * TODO Strictly speaking, we never should end up writing using this, consider throwing UOE here.
     */
    @Override
    public void write(JsonWriter out, T value) throws IOException {
        delegate.write(out, value);
    }
}
