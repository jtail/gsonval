package com.github.jtail.testutil;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonMatchers {
    private static JsonParser parser = new JsonParser();

    public static Matcher<String> isJson(String json) {
        JsonElement expected = parser.parse(json);
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String json) {
                try {
                    assertThat(expected, is(parser.parse(json)));
                    return true;
                } catch (AssertionError ae) {
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" JSON object ").appendValue(expected);
            }
        };

    }

    public static <T> Matcher<String> hasJsonPath(String jsonPath, Matcher<T> matches) {

        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String json) {
                try {
                    with(json).assertThat(jsonPath, matches);
                    return true;
                } catch (AssertionError ae) {
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" JSON object with a value at node ").appendValue(jsonPath);
                description.appendText(" that is ").appendDescriptionOf(matches);
            }
        };
    }
}
