package Exceptions;

public class InvalidWGS84CoordinateException extends Exception {
    public InvalidWGS84CoordinateException() {
        super("The coordinate is outside of the WGS84 bounds.");
    }
}
