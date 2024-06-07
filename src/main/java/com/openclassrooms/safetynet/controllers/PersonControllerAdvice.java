package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.exceptions.PersonCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.openclassrooms.safetynet.exceptions.PersonCustomException.*;
/**
 * Provides advice for exceptions thrown by Person controllers.
 */
@ControllerAdvice
public class PersonControllerAdvice {

    private static final Logger logger = LogManager.getLogger(PersonControllerAdvice.class);

    /**
     * Handles exceptions thrown when adding a person fails.
     *
     * @param ex the exception that was thrown
     * @return a response entity with an appropriate HTTP status and error message
     */
    @ExceptionHandler(AddPersonException.class)
    public ResponseEntity<String> handleAddPersonException(AddPersonException ex) {
        String errorText = "Error occurred while adding a person";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof AlreadyExistPersonException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions thrown when updating a person fails.
     *
     * @param ex the exception that was thrown
     * @return a response entity with an appropriate HTTP status and error message
     */
    @ExceptionHandler(UpdatePersonException.class)
    public ResponseEntity<String> handleUpdatePersonException(UpdatePersonException ex) {
        String errorText = "Error occurred while updating a person";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof NotFoundPersonException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions thrown when deleting a person fails.
     *
     * @param ex the exception that was thrown
     * @return a response entity with an appropriate HTTP status and error message
     */
    @ExceptionHandler(DeletePersonException.class)
    public ResponseEntity<String> handleDeletePersonException(DeletePersonException ex) {
        String errorText = "Error occurred while deleting a person";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof NotFoundPersonException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles all other exceptions not covered in PersonService (Get) because Advice preferred to choose a child exception (more precise than the parent).
     *
     * @param ex the exception that was thrown
     * @return a response entity with an appropriate HTTP status and error message
     */
    @ExceptionHandler(PersonCustomException.class)
    public ResponseEntity<String> handleAnythingGetMethodException(PersonCustomException ex) {
        String errorText = "Error occurred while during processing of a person";
        logger.error("{} : {}", errorText, ex.getMessage());
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
