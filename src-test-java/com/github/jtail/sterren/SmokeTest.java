package com.github.jtail.sterren;

import com.github.jtail.testbeans.D;
import com.github.jtail.testbeans.P;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.github.jtail.gson.BJson.arr;
import static com.github.jtail.gson.BJson.obj;
import static com.github.jtail.gson.Prop.val;

/**
 * Test for some a few specific features, like arrays and generics that are known to be tricky.
 */
@Slf4j
public class SmokeTest extends AbstractGsonValTest {
    private final static TypeToken<D<D<P>>> TT = new TypeToken<D<D<P>>>() {};

    @Test
    public void test() throws Exception {
        String json = obj(
                "x", obj(
                        "x", obj(val("x", 2), val("y", 3)),
                        "y", obj(val("x", 4), val("y", 4))
                ),
                "y", obj()
        ).toString();

        log.info(json);
        D<D<P>> o = subj.fromJson(json, TT.getType());

        log.info("D = [{}]", o);
    }

    @Test
    public void test2() throws Exception {
        given("{'value':'X'}").parseTo(Int.class).failWith("{'value':['Unable to parse `X` as [int]']}");
    }

    @Test
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void testArray() throws Exception {
        given(
                arr(
                        obj(val("value", "1")),
                        obj(val("va.lue", ".X")),
                        obj(val("value", "Y"))
                ).toString()
        ).parseTo(
                new TypeToken<Int[]>() {}
        ).failWith(
                "{'1':{'va.lue':['Unable to parse `.X` as [int]']}, '2':{'value':['Unable to parse `Y` as [int]']}}"
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Int {
        @SerializedName(value = "value", alternate = "va.lue")
        int value;
    }

}
