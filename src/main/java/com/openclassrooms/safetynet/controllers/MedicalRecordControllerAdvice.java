package com.openclassrooms.safetynet.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;

/**
 * Provides advice for exceptions thrown by MedicalRecord controllers.
 */
@ControllerAdvice
public class MedicalRecordControllerAdvice {

    private static final Logger logger = LogManager.getLogger(MedicalRecordControllerAdvice.class);

    /**
     * Handles exceptions when adding a medical record fails.
     *
     * @param ex the exception that was thrown
     * @return a response entity indicating the error status and message
     */
    @ExceptionHandler(AddMedicalRecordException.class)
    public ResponseEntity<String> handleAddMedicalRecordException(AddMedicalRecordException ex) {
        String errorText = "Error occurred while adding a medical record";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof AlreadyExistMedicalRecordException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions when updating a medical record fails.
     *
     * @param ex the exception that was thrown
     * @return a response entity indicating the error status and message
     */
    @ExceptionHandler(UpdateMedicalRecordException.class)
    public ResponseEntity<String> handleUpdateMedicalRecordException(UpdateMedicalRecordException ex) {
        String errorText = "Error occurred while updating a medical record";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof NotFoundMedicalRecordException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions when deleting a medical record fails.
     *
     * @param ex the exception that was thrown
     * @return a response entity indicating the error status and message
     */
    @ExceptionHandler(DeleteMedicalRecordException.class)
    public ResponseEntity<String> handleDeleteMedicalRecordException(DeleteMedicalRecordException ex) {
        String errorText = "Error occurred while deleting a medical record";
        logger.error("{} : {}", errorText, ex.getMessage());
        if (ex.getCause() instanceof NotFoundMedicalRecordException) {
            return new ResponseEntity<>(errorText + " : " + ex.getCause().getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
