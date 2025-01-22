package net.dunice.intensive.spring_boot.validations.fields;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCharSequence;
import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public class FieldValidator implements ConstraintValidator<ValidField, String> {
    private final ConstraintValidator<Size, CharSequence> sizeValidator;

    private final ConstraintValidator<NotBlank, CharSequence> notBlankValidator;

    private MockSize mockSize;

    private MockNotBlank mockNotBlank;

    public FieldValidator() {
        this(new SizeValidatorForCharSequence(), new NotBlankValidator());
    }

    @Override
    public void initialize(ValidField constraintAnnotation) {
        mockSize = new MockSize(
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                constraintAnnotation.message(),
                constraintAnnotation.groups(),
                constraintAnnotation.payload(),
                Size.class
        );

        mockNotBlank = new MockNotBlank(
                constraintAnnotation.notBlankMessage(),
                constraintAnnotation.groups(),
                constraintAnnotation.payload(),
                NotBlank.class
        );

        sizeValidator.initialize(mockSize);
        notBlankValidator.initialize(mockNotBlank);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        boolean isValid = true;
        if (!notBlankValidator.isValid(value, context)) {
            context.buildConstraintViolationWithTemplate(mockNotBlank.message()).addConstraintViolation();
            isValid = false;
        }

        if (!sizeValidator.isValid(value, context)) {
            context.buildConstraintViolationWithTemplate(mockSize.message()).addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }

    private record MockSize(
            int min,
            int max,
            String message,
            Class<?>[] groups,
            Class<? extends Payload>[] payload,
            Class<? extends Annotation> annotationType
    ) implements Size {
    }

    private record MockNotBlank(
            String message,
            Class<?>[] groups,
            Class<? extends Payload>[] payload,
            Class<? extends Annotation> annotationType
    ) implements NotBlank {
    }
}
