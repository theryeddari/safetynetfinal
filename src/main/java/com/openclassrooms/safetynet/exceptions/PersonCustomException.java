package com.openclassrooms.safetynet.exceptions;

// Custom exception class for managing JSON data exceptions
public class PersonCustomException extends Exception {

    // Constructor with message and cause
    public PersonCustomException(String message, Throwable cause) {super(message, cause);}

    // Nested static class for add person method to encapsulate throwable general Exception in specific Exception
    public static class AddPersonException extends PersonCustomException {
        public AddPersonException(String message, Throwable cause) {
            super("Error while adding a person, additional information : " + message, cause);
        }
    }

    // Nested static class for update person method to encapsulate throwable general Exception in specific Exception
    public static class UpdatePersonException extends PersonCustomException {
        public UpdatePersonException(String message, Throwable cause) {
            super("Error while updating a person, additional information : " + message, cause);
        }
    }

    // Nested static class for update person method to encapsulate throwable general Exception in specific Exception
    public static class DeletePersonException extends PersonCustomException {
        public DeletePersonException(String message, Throwable cause) {
            super("Error while deleting a person, additional information : " + message, cause);
        }
    }

}
