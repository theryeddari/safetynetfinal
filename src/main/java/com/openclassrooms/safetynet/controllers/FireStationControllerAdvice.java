package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.exceptions.FireStationCustomException.AddFireStationException;
import com.openclassrooms.safetynet.exceptions.FireStationCustomException.UpdateFireStationException;
import com.openclassrooms.safetynet.exceptions.FireStationCustomException.DeleteFireStationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.openclassrooms.safetynet.exceptions.FireStationCustomException.*;

/**
 * Provides advice for exceptions thrown by FireStation controllers.
 */
@ControllerAdvice
public class FireStationControllerAdvice {

    private static final Logger logger = LogManager.getLogger(FireStationControllerAdvice.class);

    /**
     * Handles exceptions when adding a fire station fails.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity with an error message and appropriate HTTP status.
     */
    @ExceptionHandler(AddFireStationException.class)
    public ResponseEntity<String> handleAddFireStationException(AddFireStationException ex) {
        String errorText = "Error occurred while adding a fire station";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof AlreadyExistFireStationException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions when updating a fire station fails.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity with an error message and appropriate HTTP status.
     */
    @ExceptionHandler(UpdateFireStationException.class)
    public ResponseEntity<String> handleUpdateFireStationException(UpdateFireStationException ex) {
        String errorText = "Error occurred while updating a fire station";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof NotFoundFireStationException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage() , HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions when deleting a fire station fails.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity with an error message and appropriate HTTP status.
     */
    @ExceptionHandler(DeleteFireStationException.class)
    public ResponseEntity<String> handleDeleteFireStationException(DeleteFireStationException ex) {
        String errorText = "Error occurred while deleting a fire station";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof NotFoundFireStationException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
