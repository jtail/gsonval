package com.github.jtail.gson;

import com.google.gson.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import static com.github.jtail.gson.BJson.arr;
import static com.github.jtail.gson.BJson.obj;
import static com.github.jtail.gson.Prop.val;

@Slf4j
public class BJsonTest {
    @Test
    public void array() {
        JsonArray arr = arr(
                obj(o -> {
                    o.addProperty("x", 1);
                    o.addProperty("y", 2);
                }),
                obj(o -> {
                    o.addProperty("x", 5);
                })
        );
        Assert.assertEquals("[{\"x\":1,\"y\":2},{\"x\":5}]", arr.toString());
    }

    @Test
    public void arrayNestedObjects() {
        JsonArray arr = arr(
                obj(
                        val("x", 1),
                        val("y", 2),
                        val("nested", obj(val("value", "232")))
                ),
                obj(val("x", 5))
        );
        Assert.assertEquals("[{\"x\":1,\"y\":2,\"nested\":{\"value\":\"232\"}},{\"x\":5}]", arr.toString());
    }

}