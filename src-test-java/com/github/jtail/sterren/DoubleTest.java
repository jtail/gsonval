package com.github.jtail.sterren;

import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class DoubleTest extends AbstractGsonValTest {
    /** Unquoted number is a valid json primitive. */
    @Test
    public void unquotedNumber() throws Exception {
        String json = "1";
        Assert.assertEquals(1, subj.fromJson(json, double.class), 0);
    }

    @Test
    public void quotedNumber() throws Exception {
        String json = "\"1\"";
        Assert.assertEquals(1, subj.fromJson(json, double.class), 0);
    }

    @Test
    public void decimal() throws Exception {
        String json = "\"1.12\"";
        Assert.assertEquals(1.12, subj.fromJson(json, double.class), 0);
    }

    /**
     * Unquoted string is not a valid json primitive, so we should be getting structural exception instead.
     * However google gson gives NumberFormatException instead this in a way, that
     * TODO As we plan to include some special handling for primitives anyway, it will likely be a no-brainer to fix behaviour to be consistent.
     */
    @Test /* (expected = JsonSyntaxException.class) */
    public void unquotedString() throws Exception {
        String json = "X";
        JsonElement errors = assertFails(json, double.class);
        Assert.assertNotNull(errors.getAsJsonPrimitive().getAsString());
    }

    @Test
    public void quotedString() throws Exception {
        String json = "\"X\"";
        JsonElement errors = assertFails(json, double.class);
        Assert.assertNotNull(errors.getAsJsonPrimitive().getAsString());
    }
}
