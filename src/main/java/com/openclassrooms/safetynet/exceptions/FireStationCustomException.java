package com.openclassrooms.safetynet.exceptions;

/**
 * Custom exception class for fire station operations.
 */
public class FireStationCustomException extends Exception {

    /**
     * Constructor with message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public FireStationCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message only.
     *
     * @param message the detail message.
     */
    public FireStationCustomException(String message) {
        super(message);
    }

    /**
     * Exception for errors during the addition of a fire station.
     */
    public static class AddFireStationException extends FireStationCustomException {
        public AddFireStationException(Throwable cause) {
            super("Error while adding a fire station, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during the update of a fire station.
     */
    public static class UpdateFireStationException extends FireStationCustomException {
        public UpdateFireStationException(Throwable cause) {
            super("Error while updating a fire station, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors during the deletion of a fire station.
     */
    public static class DeleteFireStationException extends FireStationCustomException {
        public DeleteFireStationException(Throwable cause) {
            super("Error while deleting a fire station, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for attempting to add an already existing fire station.
     */
    public static class AlreadyExistFireStationException extends FireStationCustomException {
        public AlreadyExistFireStationException() {
            super("Error, The fire station already exists, if you wish to modify it use the update procedure");
        }
    }

    /**
     * Exception for when a fire station is not found.
     */
    public static class NotFoundFireStationException extends FireStationCustomException {
        public NotFoundFireStationException() {
            super("Error, The fire station wanted is not found, if you wish to add it use the add procedure");
        }
    }

}
