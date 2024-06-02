package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.exceptions.FireStationCustomException.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FireStationControllerAdviceTest {

    private final FireStationControllerAdvice advice = new FireStationControllerAdvice();

    @Test
    void handleAddFireStationException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleAddFireStationException(new AddFireStationException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleAddFireStationSubsumedAlreadyExistException() {
        AlreadyExistFireStationException alreadyExistFireStationException = new AlreadyExistFireStationException();
        ResponseEntity<String> responseEntity = advice.handleAddFireStationException(new AddFireStationException(alreadyExistFireStationException));
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
    @Test
    void handleUpdateFireStationException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleUpdateFireStationException(new UpdateFireStationException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    void handleUpdateFireStationSubsumedNotFoundException() {
        NotFoundFireStationException notFoundFireStationException = new NotFoundFireStationException();
        ResponseEntity<String> responseEntity = advice.handleUpdateFireStationException(new UpdateFireStationException(notFoundFireStationException));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleDeleteFireStationException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleDeleteFireStationException(new DeleteFireStationException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    void handleDeleteFireStationSubsumedNotFoundException() {
        NotFoundFireStationException notFoundFireStationException = new NotFoundFireStationException();
        ResponseEntity<String> responseEntity = advice.handleDeleteFireStationException(new DeleteFireStationException(notFoundFireStationException));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}