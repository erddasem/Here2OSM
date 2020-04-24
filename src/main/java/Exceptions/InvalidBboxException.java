package Exceptions;

public class InvalidBboxException extends Exception {

    public InvalidBboxException() {
        super("Invalid bounding box input.");
    }
}
