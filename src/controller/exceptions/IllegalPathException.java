package controller.exceptions;

public class IllegalPathException extends Exception{
    private final String errorMessage = "There is one or more paths which contain invalid stations";

    public IllegalPathException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
