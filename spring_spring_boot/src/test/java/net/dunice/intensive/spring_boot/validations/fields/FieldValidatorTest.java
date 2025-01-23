package net.dunice.intensive.spring_boot.validations.fields;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCharSequence;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FieldValidatorTest {
    private static final String EXPECTED_NOT_BLANK_MESSAGE = "Null or blank field was provided.";

    private static final String EXPECTED_LENGTH_MESSAGE = "Invalid field length. Must be at least 2 and at most 4 characters long.";

    @Test
    public void initializeActuallySetsAccordingFieldsToProvidedInCtor() {
        final var expectedMin = 111;
        final var expectedMax = 333;

        final var annotation = mock(ValidField.class);
        when(annotation.min()).thenReturn(expectedMin);
        when(annotation.max()).thenReturn(expectedMax);

        final var sizeValidator = new SizeValidatorForCharSequence();
        final var fieldValidator = new FieldValidator(sizeValidator, new NotBlankValidator());
        fieldValidator.initialize(annotation);

        final var actualMin = ReflectionTestUtils.getField(sizeValidator, "min");
        final var actualMax = ReflectionTestUtils.getField(sizeValidator, "max");

        assertEquals(expectedMin, actualMin);
        assertEquals(expectedMax, actualMax);
    }

    @Test
    public void isValidDelegatesCallToTheSizeValidatorImpl() {
        final var annotation = mock(ValidField.class);
        when(annotation.min()).thenReturn(1);
        when(annotation.max()).thenReturn(3);

        final var expectedString = "will be checked for the validity";

        final var mockContext = mock(ConstraintValidatorContext.class);
        final var mockContextBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockContextBuilder);
        when(mockContextBuilder.addConstraintViolation()).thenReturn(mockContext);

        final var sizeValidator = mock(SizeValidatorForCharSequence.class);
        final var fieldValidator = new FieldValidator(sizeValidator, new NotBlankValidator());

        fieldValidator.initialize(annotation);
        fieldValidator.isValid(expectedString, mockContext);

        verify(mockContext).disableDefaultConstraintViolation();
        verify(sizeValidator).isValid(expectedString, mockContext);
    }

    @Test
    public void checkValidatorProvidesRightMessageWhenStringIsBlankOrNull() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            final var expectedMessages = List.of(EXPECTED_NOT_BLANK_MESSAGE, EXPECTED_LENGTH_MESSAGE);
            final var validator = validatorFactory.getValidator();

            final var mockContext = mock(ConstraintValidatorContext.class);
            final var mockContextBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

            when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockContextBuilder);

            Stream.of("", " ", "\t", "\n")
                    .map(string -> validator.validate(new FieldRecord(string)))
                    .forEach(violations -> {
                        final var expectedSize = expectedMessages.size();
                        final var violationMessages = violations.stream().map(ConstraintViolation::getMessage).toList();

                        assertEquals(expectedSize, violations.size());
                        assertTrue(violationMessages.containsAll(expectedMessages));
                    });
        }
    }

    @Test
    public void checkValidatorProvidesRightMessageWhenStringSizeIsNotValid() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            final var validator = validatorFactory.getValidator();

            final var validationResults = Stream.of("asdasd", "123213123", "x")
                    .map(string -> validator.validate(new FieldRecord(string)))
                    .flatMap(violations -> violations.stream().map(ConstraintViolation::getMessage))
                    .toList();

            assertFalse(validationResults.isEmpty());
            validationResults.forEach(message -> assertEquals(EXPECTED_LENGTH_MESSAGE, message));
        }
    }

    private record FieldRecord(
            @ValidField(min = 2, max = 4) String string
    ) {
    }
}