package com.github.jtail.sterren;

import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
@Slf4j
public class IntegerTest extends AbstractGsonValTest {
    @Test
    public void unquotedNumber() throws Exception {
        given("1").parseTo(int.class).returns(is(1));
    }

    @Test
    public void quotedNumber() throws Exception {
        given("\"1\"").parseTo(int.class).returns(is(1));
    }

    @Test
    public void decimal() throws Exception {
        given("1.12").parseTo(int.class).failWith("[\"Unable to parse `1.12` as [int]\"]");
    }

    @Test
    public void unquotedString() throws Exception {
        given("X").parseTo(int.class).failWith(JsonSyntaxException.class);
    }

    @Test
    public void quotedString() throws Exception {
        given("\"X\"").parseTo(int.class).failWith("[\"Unable to parse `X` as [int]\"]");
    }
}
