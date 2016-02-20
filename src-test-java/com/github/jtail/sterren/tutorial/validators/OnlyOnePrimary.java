package com.github.jtail.sterren.tutorial.validators;

import com.github.jtail.sterren.tutorial.endpoints.UserResource.Email;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = OnlyOnePrimary.Validator.class)
public @interface OnlyOnePrimary {

    String message() default "{com.github.jtail.sterren.tutorial.validators.OnlyOnePrimary.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<OnlyOnePrimary, List<Email>> {
        @Override
        public void initialize(OnlyOnePrimary constraintAnnotation) {
        }

        @Override
        public boolean isValid(List<Email> value, ConstraintValidatorContext context) {
            return value.stream().filter(Email::isPrimary).count() == 1;
        }

    }
}
