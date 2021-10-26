package de.tech26.robotfactory.exception;

public class RoboFactoryException extends RuntimeException{

    public RoboFactoryException(String message) {
        super(message);
    }

    public RoboFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
