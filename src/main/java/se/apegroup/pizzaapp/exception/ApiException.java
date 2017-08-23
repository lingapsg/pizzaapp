package se.apegroup.pizzaapp.exception;

public class ApiException extends RuntimeException {

    public final int errorCode;
    public final String errorMessage;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
