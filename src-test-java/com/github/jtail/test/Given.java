package com.github.jtail.test;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 *
 */
public abstract class Given {
    protected final String json;

    public Given(String json) {
        this.json = json;
    }

    public <T> ParseTo<T> parseTo(Class<T> clazz) {
        return new ParseTo<>(this, clazz);
    }

    public <T> ParseTo<T> parseTo(TypeToken<T> type) {
        return new ParseTo<>(this, type);
    }

    public abstract <T> T execute(Type type);
}
