package com.openclassrooms.safetynet.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MedicalRecordControllerAdviceTest {

    private final MedicalRecordControllerAdvice advice = new MedicalRecordControllerAdvice();

    @Test
    void handleAddMedicalRecordException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleAddMedicalRecordException(new AddMedicalRecordException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleAddMedicalRecordSubsumedAlreadyExistException() {
        AlreadyExistMedicalRecordException alreadyExistMedicalRecordException = new AlreadyExistMedicalRecordException();
            ResponseEntity<String> responseEntity = advice.handleAddMedicalRecordException(new AddMedicalRecordException(alreadyExistMedicalRecordException));
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
    @Test
    void handleUpdateMedicalRecordException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleUpdateMedicalRecordException(new UpdateMedicalRecordException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    void handleUpdateMedicalRecordSubsumedNotFoundException() {
        NotFoundMedicalRecordException notFoundMedicalRecordException = new NotFoundMedicalRecordException();
        ResponseEntity<String> responseEntity = advice.handleUpdateMedicalRecordException(new UpdateMedicalRecordException(notFoundMedicalRecordException));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleDeleteMedicalRecordException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleDeleteMedicalRecordException(new DeleteMedicalRecordException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    void handleDeleteMedicalRecordSubsumedNotFoundException() {
        NotFoundMedicalRecordException notFoundMedicalRecordException = new NotFoundMedicalRecordException();
        ResponseEntity<String> responseEntity = advice.handleDeleteMedicalRecordException(new DeleteMedicalRecordException(notFoundMedicalRecordException));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}