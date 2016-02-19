package com.github.jtail.sterren;

import com.github.jtail.testbeans.P;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
public class ValidatorTest {
    private ValidatorFactory avf = Validation.buildDefaultValidatorFactory();
    private Validator validator = avf.getValidator();

    @Test
    public void validator() throws Exception {
        Q q = new Q(Collections.singletonList(new P()));
        Set<ConstraintViolation<Q>> violations = validator.validate(q);
        Assert.assertTrue(violations.size() == 2);
    }

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class Q {
        @Valid
        @Size(min = 3) private List<P> list;
    }

}
