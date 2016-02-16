package com.github.jtail.sterren;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.Matchers.closeTo;

@Slf4j
public class DoubleTest extends AbstractGsonValTest {
    @Test
    public void unquotedNumber() throws Exception {
        given("1").parseTo(double.class).returns(closeTo(1.0D, 0));
    }

    @Test
    public void quotedNumber() throws Exception {
        given("\"1\"").parseTo(double.class).returns(closeTo(1.0D, 0));
    }

    @Test
    public void decimal() throws Exception {
        given("\"1.12\"").parseTo(double.class).returns(closeTo(1.12D, 0));
    }

    @Test
    public void unquotedString() throws Exception {
        given("X").parseTo(double.class).failWith("\"Unable to parse `X` as [double]\"");
    }

    @Test
    public void quotedString() throws Exception {
        given("\"X\"").parseTo(double.class).failWith("\"Unable to parse `X` as [double]\"");
    }
}
