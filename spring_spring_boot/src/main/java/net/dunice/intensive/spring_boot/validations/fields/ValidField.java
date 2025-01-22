package net.dunice.intensive.spring_boot.validations.fields;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Constraint(validatedBy = FieldValidator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface ValidField {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "{validation.field.invalid-length}";

    String notBlankMessage() default "{validation.field.null-or-blank-field}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
