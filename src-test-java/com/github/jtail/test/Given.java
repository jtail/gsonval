package com.github.jtail.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Given {
    final private Gson subj;
    final private String json;

    public Given(Gson subj, String json) {
        this.subj = subj;
        this.json = json;
    }

    public <T> ParseTo<T> parseTo(Class<T> clazz) {
        return new ParseTo<>(this, clazz);
    }

    public <T> ParseTo<T> parseTo(TypeToken<T> type) {
        return new ParseTo<>(this, type);
    }

    public <T> T execute(Type type) {
        return subj.fromJson(json, type);
    }

}
