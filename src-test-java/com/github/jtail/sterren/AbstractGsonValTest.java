package com.github.jtail.sterren;

import com.github.jtail.testutil.FnAssert;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractGsonValTest {
    protected final static Gson PRETTY = new GsonBuilder().setPrettyPrinting().create();
    protected Gson subj = new GsonBuilder().registerTypeAdapterFactory(new ValidatingAdapterFactory()).create();

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    protected <T> JsonElement assertFails(String json, Class<T> clazz) {
            return FnAssert.assertThrows(() -> subj.fromJson(json, clazz), ObjectValidationException.class).getFeedback();
    }
}
