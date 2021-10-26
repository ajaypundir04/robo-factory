package de.tech26.robotfactory.rest;

import de.tech26.robotfactory.exception.ConfigurationException;
import de.tech26.robotfactory.exception.RoboOrderException;
import de.tech26.robotfactory.exception.UnParsableConfigurationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Ajay Singh Pundir
 * It handles all the exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RoboOrderException.class, ConfigurationException.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnParsableConfigurationException.class)
    public ResponseEntity<String> handleUnParsableException(UnParsableConfigurationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
