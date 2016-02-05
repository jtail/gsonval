package com.github.jtail.sterren;

import com.github.jtail.testbeans.P;
import com.github.jtail.testbeans.D;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import static com.github.jtail.gson.BJson.arr;
import static com.github.jtail.gson.BJson.obj;
import static com.github.jtail.gson.Prop.val;
import static com.github.jtail.testutil.JsonMatchers.hasJsonPath;
import static org.hamcrest.Matchers.anything;

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
        String json = obj(val("value", "X")).toString();
        String s = assertFails(json, Int.class).toString();
        Assert.assertThat(s, hasJsonPath("value", anything()));
    }

    @Test
    public void testArray() throws Exception {

        TypeToken<Int[]> type = new TypeToken<Int[]>() {};
        String json = arr(
                obj(val("value", "1")),
                obj(val("va.lue", ".X")),
                obj(val("value", "Y"))
        ).toString();
        JsonElement errors = assertFails(json, type.getRawType());
        Assert.assertNotNull(errors.getAsJsonObject().getAsJsonObject("2").get("va.lue"));
        Assert.assertNotNull(errors.getAsJsonObject().getAsJsonObject("3").get("value"));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Int {
        @SerializedName(value = "value", alternate = "va.lue")
        int value;
    }

}
