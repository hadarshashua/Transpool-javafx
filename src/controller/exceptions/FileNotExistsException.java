package controller.exceptions;

public class FileNotExistsException extends Exception {
    private final String errorMessage = "File not exists!";

    public FileNotExistsException() { }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
