package com.openclassrooms.safetynet.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.openclassrooms.safetynet.exceptions.PersonCustomException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonControllerAdviceTest {

    private final PersonControllerAdvice advice = new PersonControllerAdvice();

    @Test
    void handleAddPersonException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleAddPersonException(new AddPersonException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleAddPersonSubsumedAlreadyExistException() {
        AlreadyExistPersonException alreadyExistPersonException = new AlreadyExistPersonException();
            ResponseEntity<String> responseEntity = advice.handleAddPersonException(new AddPersonException(alreadyExistPersonException));
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
    @Test
    void handleUpdatePersonException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleUpdatePersonException(new UpdatePersonException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    void handleUpdatePersonSubsumedNotFoundException() {
        NotFoundPersonException notFoundPersonException = new NotFoundPersonException();
        ResponseEntity<String> responseEntity = advice.handleUpdatePersonException(new UpdatePersonException(notFoundPersonException));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleDeletePersonException() {
        IOException ioException = new IOException();
        ResponseEntity<String> responseEntity = advice.handleDeletePersonException(new DeletePersonException(ioException));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    void handleDeletePersonSubsumedNotFoundException() {
        NotFoundPersonException notFoundPersonException = new NotFoundPersonException();
        ResponseEntity<String> responseEntity = advice.handleDeletePersonException(new DeletePersonException(notFoundPersonException));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}