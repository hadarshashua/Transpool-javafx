package controller.exceptions;

public class DayStartNotExistsException extends Exception {
    private final String errorMessage = "day start not exists";

    public DayStartNotExistsException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
