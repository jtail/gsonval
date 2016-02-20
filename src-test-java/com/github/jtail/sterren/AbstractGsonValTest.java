package com.github.jtail.sterren;

import com.github.jtail.test.Given;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

@Slf4j
public class AbstractGsonValTest {
    protected Gson subj = new GsonBuilder().registerTypeAdapterFactory(new ValidatingAdapterFactory()).create();

    protected Given given(String json) {
        return new Given(json) {
            @Override
            public <T> T execute(Type type) {
                return subj.fromJson(json, type);
            }
        };
    }

}
