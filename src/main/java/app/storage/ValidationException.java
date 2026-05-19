package app.storage;

public class ValidationException extends Exception {
    private final String errorCode;
    private final String userMessage;
    private final String fieldName;

    public ValidationException(String userMessage) {
        this("GENERAL_ERROR", userMessage, null, null);
    }

    public ValidationException(String userMessage, Throwable cause) {
        this("GENERAL_ERROR", userMessage, null, cause);
    }

    public ValidationException(String errorCode, String userMessage, String fieldName) {
        this(errorCode, userMessage, fieldName, null);
    }

    public ValidationException(String errorCode, String userMessage, String fieldName, Throwable cause) {
        super(userMessage, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.fieldName = fieldName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getFieldName() {
        return fieldName;
    }
}