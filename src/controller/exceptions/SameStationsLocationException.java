package controller.exceptions;

public class SameStationsLocationException extends Exception{
    private final String errorMessage = "There are two or more stations with the same coordinates";

    public SameStationsLocationException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
