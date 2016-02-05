package com.github.jtail.testutil;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;

import java.util.concurrent.Callable;
import java.util.function.Function;

@Slf4j
public class FnAssert {
    public static <T, V> Matcher<T> has(Function<T, V> transform, Matcher<V> matcher) {
        return new TypeSafeMatcher<T>() {
            @Override
            protected boolean matchesSafely(T item) {
                return matcher.matches(transform.apply(item));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Assert on: ").appendValue(transform);
                description.appendText(" that is ").appendDescriptionOf(matcher);
            }
        };
    }

    @SuppressWarnings("unchecked") // Casts are guarded by assertions
    public static <T, X extends Throwable> X assertThrows(Callable<T> action, Class<X> clazz) {
        Throwable t = expectThrows(action);
        try {
            Assert.assertThat(t, Matchers.instanceOf(clazz));
            return (X) t;
        } catch (AssertionError e) {
            log.error("Unexpected exception: ", t);
            throw e;
        }
    }

    public static <T> Throwable expectThrows(Callable<T> action) {
        try {
            action.call();
            throw new AssertionError("Expected exception");
        } catch (Throwable t) {
            return t;
        }
    }
}
