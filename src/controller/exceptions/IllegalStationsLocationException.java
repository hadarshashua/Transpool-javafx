package controller.exceptions;

public class IllegalStationsLocationException extends Exception{
    private final String errorMessage = "At least one of the stations is not in map boundries";

    public IllegalStationsLocationException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
