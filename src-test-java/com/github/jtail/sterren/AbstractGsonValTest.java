package com.github.jtail.sterren;

import com.github.jtail.test.Given;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractGsonValTest {
    protected final static Gson PRETTY = new GsonBuilder().setPrettyPrinting().create();
    protected Gson subj = new GsonBuilder().registerTypeAdapterFactory(new ValidatingAdapterFactory()).create();

    protected Given given(String json) {
        return new Given(subj, json);
    }

}
