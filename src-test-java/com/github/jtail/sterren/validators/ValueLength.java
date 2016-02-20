package com.github.jtail.sterren.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A custom constraint that will verifys that value string representation has specified length.
 * Not expected to be useful in any manner, except being sample annotation for tests.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValueLength.Validator.class)
public @interface ValueLength {

    String message() default "{com.github.jtail.sterren.tutorial.validators.JediMaster.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 0;

    int max() default Integer.MAX_VALUE;


    class Validator implements ConstraintValidator<ValueLength, Object> {
        private ValueLength ann;

        @Override
        public void initialize(ValueLength constraintAnnotation) {
            ann = constraintAnnotation;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            int len = String.valueOf(value).length();
            return len >= ann.min() && len <= ann.max();
        }
    }
}
