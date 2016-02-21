package tutorial.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

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
@Constraint(validatedBy = JediMaster.Validator.class)
public @interface JediMaster {

    String message() default "{tutorial.validators.JediMaster.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<JediMaster, String> {
        private final static java.util.List<String> MASTERS = Arrays.asList(
                "Bolla Ropal", "Ima-Gun Di", "Qui-Gon Jinn", "Obi-Wan Kenobi", "Agen Kolar", "Yarael Poof",
                "Tiplee", "Luminara Unduli", "Uvell", "Va Zhurro", "Yoda"
        );

        @Override
        public void initialize(JediMaster constraintAnnotation) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return MASTERS.contains(value);
        }
    }
}
