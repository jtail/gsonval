package com.github.jtail.test;

import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Some syntax sugar to have tests a little more expressive
 */
public class Admire {
    public static <T> Actor<T> given(T given) {
        return new Actor<>(given);
    }

    public static <T> Actor<T> given(Callable<T> supplier) throws Exception {
        // TODO run this in assume
        return new Actor<>(supplier.call());
    }

    @AllArgsConstructor
    public static class Actor<T> {
        final T given;

        // TODO use XFunction to allow exceptions
        public <R> Asserter<R> execute(Function<T, R> action) {
            return new Asserter<>(action.apply(given));
        }
    }

    @AllArgsConstructor
    public static class Asserter<V> {
        final V value;

        @SuppressWarnings("unchecked")
        public void expect(Matcher<V>... matchers) {
            Assert.assertThat(value, Matchers.allOf(matchers));
        }
    }

}
