package com.github.jtail.sterren;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class IntegerTest extends AbstractGsonValTest {
    /** Unquoted number is a valid json primitive. */
    @Test
    public void unquotedNumber() throws Exception {
        String json = "1";
        Assert.assertEquals(1, (int) subj.fromJson(json, int.class));
    }

    @Test
    public void quotedNumber() throws Exception {
        String json = "\"1\"";
        Assert.assertEquals(1, (int) subj.fromJson(json, int.class));
    }

    @Test
    public void decimal() throws Exception {
        JsonElement errors = assertFails("1.12", int.class);
        System.out.println(PRETTY.toJson(errors));
    }

    /** Unquoted string is NOT a valid json primitive, so we should be getting structural exception instead. */
    @Test(expected = JsonSyntaxException.class)
    public void unquotedString() throws Exception {
        subj.fromJson("X", int.class);
    }

    @Test
    public void quotedString() throws Exception {
        String json = "\"X\"";
        JsonElement errors = assertFails(json, int.class);
        Assert.assertNotNull(errors.getAsJsonPrimitive().getAsString());
    }
}
