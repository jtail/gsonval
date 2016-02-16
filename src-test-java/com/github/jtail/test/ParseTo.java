package com.github.jtail.test;

import com.github.jtail.sterren.ObjectValidationException;
import com.github.jtail.testutil.FnAssert;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.lang.reflect.Type;
import java.util.function.Function;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class ParseTo<T> {
    private final static JsonParser parser = new JsonParser();

    private final Given given;
    private final Type type;

    protected ParseTo(Given given, Type type) {
        this.given = given;
        this.type = type;
    }

    protected ParseTo(Given given, TypeToken<T> typeToken) {
        this.given = given;
        this.type = typeToken.getType();
    }

    public void failWith(String result) {
        Assert.assertThat(failWith(ObjectValidationException.class).getFeedback(), Matchers.is(parser.parse(result)));
    }

    public  <X extends Exception> X failWith(Class<X> clazz) {
        return FnAssert.assertThrows(this::execute, clazz);
    }

    public <V> void returns(Function<T, V> transform, Matcher<V> matcher) {
        Assert.assertThat(execute(), FnAssert.has(transform, matcher));
    }

    public <V> void returns(Matcher<T> matcher) {
        Assert.assertThat(execute(), matcher);
    }

    private T execute() {
        return given.getSubj().fromJson(given.getJson(), type);
    }


}
