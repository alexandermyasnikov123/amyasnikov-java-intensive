package net.dunice.intensive.spring_boot.constants;

public interface GenericFieldErrors {
    String INVALID_FIELD_LENGTH = "Invalid title length. Must be at least {min} and at most {max} characters long.";

    String NULL_OR_BLANK_FIELD = "Null or blank field was provided.";
}
