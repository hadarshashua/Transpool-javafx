package controller.exceptions;

public class DayStartNotValidException extends Exception{
    private final String errorMessage = "day start not valid, must be greater than 0";

    public DayStartNotValidException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
