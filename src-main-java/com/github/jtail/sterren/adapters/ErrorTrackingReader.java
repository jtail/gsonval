package com.github.jtail.sterren.adapters;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Deque;
import java.util.LinkedList;

public class ErrorTrackingReader extends AbstractJsonReaderDelegate {
    @Getter
    private Deque<Pair<String, JsonElement>> errors = new LinkedList<>();

    public ErrorTrackingReader(JsonReader delegate) {
        super(delegate);
    }

    public void pushError(String key, JsonElement value) {
        errors.push(Pair.of(key, value));
    }
}
