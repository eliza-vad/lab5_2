package validation;

public class ValidationException extends RuntimeException { //проверяемое искл

    private final String errorCode;
    private final String userMessage;
    private final String fieldName;


    public ValidationException(String errorCode, String userMessage, String fieldName) {
        super(userMessage); //передает в родит класс
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.fieldName = fieldName;
    }
//они работа.т вместе. берем и передает
    public ValidationException(String message) {
        super(message);
        this.userMessage = message;
        this.errorCode = "GENERAL_ERROR";
        this.fieldName = "unknown";
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