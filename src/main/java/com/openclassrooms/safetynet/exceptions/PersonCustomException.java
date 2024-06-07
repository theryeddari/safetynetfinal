package com.openclassrooms.safetynet.exceptions;

/**
 * Custom exception class for managing JSON data exceptions.
 */
public class PersonCustomException extends Exception {

    /**
     * Constructor with message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public PersonCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message only.
     *
     * @param message the detail message.
     */
    public PersonCustomException(String message) {
        super(message);
    }

    /**
     * Exception for errors during the addition of a person.
     */
    public static class AddPersonException extends PersonCustomException {
        public AddPersonException(Throwable cause) {
            super("Error while adding a person, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during the update of a person.
     */
    public static class UpdatePersonException extends PersonCustomException {
        public UpdatePersonException(Throwable cause) {
            super("Error while updating a person, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during the deletion of a person.
     */
    public static class DeletePersonException extends PersonCustomException {
        public DeletePersonException(Throwable cause) {
            super("Error while deleting a person, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception when the person file already exists.
     */
    public static class AlreadyExistPersonException extends PersonCustomException {
        public AlreadyExistPersonException() {
            super("Error, The person file already exists, if you wish to modify it use the update procedure");
        }
    }

    /**
     * Exception when the person file is not found.
     */
    public static class NotFoundPersonException extends PersonCustomException {
        public NotFoundPersonException() {
            super("Error, The person file wanted is not found, if you wish to add it use the add procedure");
        }
    }

    /**
     * Exception for errors during fire station query execution.
     */
    public static class FireStationResponseException extends PersonCustomException {
        public FireStationResponseException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during child alert query execution.
     */
    public static class ChildAlertResponseException extends PersonCustomException {
        public ChildAlertResponseException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during phone alert query execution.
     */
    public static class PhoneAlertResponseException extends PersonCustomException {
        public PhoneAlertResponseException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during fire response query execution.
     */
    public static class FireResponseException extends PersonCustomException {
        public FireResponseException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during flood station query execution.
     */
    public static class FloodStationResponseException extends PersonCustomException {
        public FloodStationResponseException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during person info query execution.
     */
    public static class PersonInfoResponseException extends PersonCustomException {
        public PersonInfoResponseException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during community email query execution.
     */
    public static class CommunityEmailException extends PersonCustomException {
        public CommunityEmailException(Exception cause) {
            super("Error while executing query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during data assembly for query.
     */
    public static class FactoringConcatStreamMethodException extends PersonCustomException {
        public FactoringConcatStreamMethodException(Exception cause) {
            super("Error while assembly data for query, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }
}
