package com.github.jtail.sterren;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
@Slf4j
public class IntegerListTest extends AbstractGsonValTest {

    @Test
    public void unquotedNumber() throws Exception {
        given("['1', '2', '3']").parseTo(new TypeToken<List<Integer>>() {}).returns(
                Matchers.contains(1, 2, 3)
        );
    }

//    @Test
//    public void quotedNumber() throws Exception {
//        given("\"1\"").parseTo(int.class).returns(is(1));
//    }

    @Test
    public void decimal() throws Exception {
        given("['1', '2.1', '3']").parseTo(new TypeToken<List<Integer>>() {}).failWith("{'1':['Unable to parse `2.1` as [java.lang.Integer]']}");
    }
//
//    @Test
//    public void unquotedString() throws Exception {
//        given("X").parseTo(int.class).failWith(JsonSyntaxException.class);
//    }
//
//    @Test
//    public void quotedString() throws Exception {
//        given("\"X\"").parseTo(int.class).failWith("[\"Unable to parse `X` as [int]\"]");
//    }
}
