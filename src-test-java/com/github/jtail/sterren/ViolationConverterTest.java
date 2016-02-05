package com.github.jtail.sterren;

import com.github.jtail.testbeans.D;
import com.github.jtail.testbeans.P;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Slf4j
public class ViolationConverterTest {
    private ViolationConverter converter = new ViolationConverter();
    private ValidatorFactory avf = Validation.buildDefaultValidatorFactory();
    private Validator validator = avf.getValidator();

    @Test
    public void validator() throws Exception {
        Set<ConstraintViolation<B>> violations = validator.validate(new B());
        Assert.assertTrue(violations.size() > 0);
    }

    @Test
    public void converter() throws Exception {
        D<D<P>> object = new D<>(new D<>(new P(), new P()), new D<>());
        Set<ConstraintViolation<D<D<P>>>> violations = validator.validate(object);

        Assert.assertEquals(2, violations.size());

        JsonObject json = converter.apply(violations);
        log.info("Errors JSON: {}", new Gson().toJson(json));

        Assert.assertNotNull(json.getAsJsonObject("x").getAsJsonObject("y").getAsJsonArray("y").get(0).getAsString());
        Assert.assertNotNull(json.getAsJsonObject("x").getAsJsonObject("x").getAsJsonArray("y").get(0).getAsString());
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class B {
        @NotNull
        private P required;
        private P optional;
    }
}
