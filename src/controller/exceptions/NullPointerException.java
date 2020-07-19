package controller.exceptions;

public class NullPointerException extends Exception{
    private final String errorMessage = "To start the program you must read from a valid XML file first";

    public NullPointerException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
