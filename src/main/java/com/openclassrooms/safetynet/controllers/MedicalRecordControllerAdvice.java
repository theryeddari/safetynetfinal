package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;

@ControllerAdvice
public class MedicalRecordControllerAdvice {

    private static final Logger logger = LogManager.getLogger(MedicalRecordControllerAdvice.class);

    @ExceptionHandler(AddMedicalRecordException.class)
    public ResponseEntity<String> handleAddMedicalRecordException(AddMedicalRecordException ex) {
        String errorText = "Error occurred while adding a medical record";
        logger.error("{} : {}", errorText, ex.getMessage());
        if(ex.getCause() instanceof MedicalRecordCustomException) {
            return new ResponseEntity<>(errorText, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateMedicalRecordException.class)
    public ResponseEntity<String> handleUpdateMedicalRecordException(UpdateMedicalRecordException ex) {
        String errorText = "Error occurred while updating a medical record";
        logger.error("{} : {}", errorText, ex.getMessage());
        if(ex.getCause() instanceof MedicalRecordCustomException) {
            return new ResponseEntity<>(errorText, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteMedicalRecordException.class)
    public ResponseEntity<String> handleDeleteMedicalRecordException(DeleteMedicalRecordException ex) {
        String errorText = "Error occurred while deleting a medical record";
        logger.error("{} : {}", errorText, ex.getMessage());
        if(ex.getCause() instanceof MedicalRecordCustomException) {
            return new ResponseEntity<>(errorText, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
