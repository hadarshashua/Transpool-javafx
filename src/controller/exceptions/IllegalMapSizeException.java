package controller.exceptions;

public class IllegalMapSizeException extends Exception{
    private final String errorMessage = "Map size is illegal, must be between 6 to 100";

    public IllegalMapSizeException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
