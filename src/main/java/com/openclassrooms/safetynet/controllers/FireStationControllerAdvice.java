package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.exceptions.FireStationCustomException;
import com.openclassrooms.safetynet.exceptions.FireStationCustomException.AddFireStationException;
import com.openclassrooms.safetynet.exceptions.FireStationCustomException.UpdateFireStationException;
import com.openclassrooms.safetynet.exceptions.FireStationCustomException.DeleteFireStationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FireStationControllerAdvice {

    private static final Logger logger = LogManager.getLogger(FireStationControllerAdvice.class);

    @ExceptionHandler(AddFireStationException.class)
    public ResponseEntity<String> handleAddFireStationException(AddFireStationException ex) {
        String errorText = "Error occurred while adding a fire station";
        logger.error("{} : {}", errorText, ex.getMessage());
        if(ex.getCause() instanceof FireStationCustomException) {
            return new ResponseEntity<>(errorText, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateFireStationException.class)
    public ResponseEntity<String> handleUpdateFireStationException(UpdateFireStationException ex) {
        String errorText = "Error occurred while updating a fire station";
        logger.error("{} : {}", errorText, ex.getMessage());
        if(ex.getCause() instanceof FireStationCustomException) {
            return new ResponseEntity<>(errorText, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteFireStationException.class)
    public ResponseEntity<String> handleDeleteFireStationException(DeleteFireStationException ex) {
        String errorText = "Error occurred while deleting a fire station";
        logger.error("{} : {}", errorText, ex.getMessage());
        if(ex.getCause() instanceof FireStationCustomException) {
            return new ResponseEntity<>(errorText, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}