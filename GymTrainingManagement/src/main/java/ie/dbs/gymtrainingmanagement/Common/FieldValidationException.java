package ie.dbs.gymtrainingmanagement.Common;

/**
 * Thrown by a service when a rule is broken that Bean Validation cannot check
 * (duplicate email, selected row does not exist, ...).
 * The controller turns it into an error on the given form field.
 */
public class FieldValidationException extends RuntimeException {
    private final String field;

    public FieldValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
