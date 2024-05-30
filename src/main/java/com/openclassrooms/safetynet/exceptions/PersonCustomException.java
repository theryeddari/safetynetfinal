package com.openclassrooms.safetynet.exceptions;

// Custom exception class for managing JSON data exceptions
public class PersonCustomException extends Exception {

    // Constructor with message and cause
    public PersonCustomException(String message, Throwable cause) {super(message, cause);}
    public PersonCustomException(String message) {super(message);}

    // Nested static class for add person method to encapsulate throwable general Exception in specific Exception
    public static class AddPersonException extends PersonCustomException {
        public AddPersonException(Throwable cause) {
            super("Error while adding a person, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for update person method to encapsulate throwable general Exception in specific Exception
    public static class UpdatePersonException extends PersonCustomException {
        public UpdatePersonException(Throwable cause) {
            super("Error while updating a person, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for delete person method to encapsulate throwable general Exception in specific Exception
    public static class DeletePersonException extends PersonCustomException {
        public DeletePersonException(Throwable cause) {
            super("Error while deleting a person, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for add person method for exception when medical record already exist
    public static class AlreadyExistPersonException extends PersonCustomException {

        public AlreadyExistPersonException() {
            super("Error, The person file already exists, if you wish to modify it use the update procedure");
        }
    }

    // Nested static class for update person method for exception when medical record not found
    public static class NotFoundPersonException extends PersonCustomException {

        public NotFoundPersonException() {
            super("Error, The person file wanted its not found, if you wish to add it use the add procedure");
        }
    }

    public static class NoMatchesFoundException extends PersonCustomException {
        public NoMatchesFoundException() {
            super("No match found in the database, please check your entry or create it there");
        }
    }

    public static class FireStationResponseException extends PersonCustomException {
        public FireStationResponseException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    public static class ChildAlertResponseException extends PersonCustomException {
        public ChildAlertResponseException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    public static class PhoneAlertResponseException extends PersonCustomException {
        public PhoneAlertResponseException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    public static class FireResponseException extends PersonCustomException {
        public FireResponseException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    public static class FloodStationResponseException extends PersonCustomException {
        public FloodStationResponseException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    public static class PersonInfoResponseException extends PersonCustomException {
        public PersonInfoResponseException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    public static class CommunityEmailException extends PersonCustomException {
        public CommunityEmailException(Exception cause) {
            super("Error while executing query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);

        }
    }

    public static class FactoringConcatStreamMethodException extends PersonCustomException {
        public FactoringConcatStreamMethodException(Exception cause) {
            super("Error while assembly data for query, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }
}
