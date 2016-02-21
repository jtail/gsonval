package com.github.jtail.sterren;

import com.github.jtail.sterren.validators.ValueLength;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.validation.Valid;
import java.util.List;

@Slf4j
public class IntegerListTest extends AbstractGsonValTest {

    @Test
    public void unquotedNumber() throws Exception {
        given("[1, 2, 3]").parseTo(new TypeToken<List<Integer>>() {}).returns(
                Matchers.contains(1, 2, 3)
        );
    }

    @Test
    public void quotedNumber() throws Exception {
        given("['1', '2', '3']").parseTo(new TypeToken<List<Integer>>() {}).returns(
                Matchers.contains(1, 2, 3)
        );
    }

    @Test
    public void decimal() throws Exception {
        given("['1', '2.1', '3']").parseTo(new TypeToken<List<Integer>>() {}).failWith("{'1':['Unable to parse `2.1` as [java.lang.Integer]']}");
    }

    @Test
    public void constraints() throws Exception {
        given("{'value': ['1', '222', '3']}").parseTo(Dummy.class).failWith(
                "{'value': {'1':['value length out of range']}}"
        );
    }

//    @Test
//    public void quotedString() throws Exception {
//        given("\"X\"").parseTo(int.class).failWith("[\"Unable to parse `X` as [int]\"]");
//    }

    @AllArgsConstructor
    private static class Dummy {
        @Valid List<@ValueLength(max = 2) Integer> value;
    }

}
