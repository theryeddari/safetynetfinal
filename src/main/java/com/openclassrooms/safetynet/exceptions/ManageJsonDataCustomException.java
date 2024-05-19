package com.openclassrooms.safetynet.exceptions;

// Custom exception class for managing JSON data exceptions
public class ManageJsonDataCustomException extends Exception {

    // Constructor with message and cause
    public ManageJsonDataCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    // Nested static class for person writer specific exceptions
    public static class ManageJsonDataPersonWriterException extends ManageJsonDataCustomException {

        // Constructor with message and cause
        public ManageJsonDataPersonWriterException(String message, Throwable cause) {

            super("error while writing information about a person, additional information:" + message, cause);
        }
    }
}