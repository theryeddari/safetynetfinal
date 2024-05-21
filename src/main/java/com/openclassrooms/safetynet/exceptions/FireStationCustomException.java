package com.openclassrooms.safetynet.exceptions;

public class FireStationCustomException extends Exception{
    // Constructor with message and cause
    public FireStationCustomException(String message, Throwable cause) {super(message,cause);}

    // Nested static class for add fire station method to encapsulate throwable general Exception in specific Exception
    public static class AddFireStationException extends FireStationCustomException {
        public AddFireStationException(String message, Throwable cause) {
            super("Error while adding a fire station, additional information : " + message, cause);
        }
    }

    // Nested static class for update fire station method to encapsulate throwable general Exception in specific Exception
    public static class UpdateFireStationException extends FireStationCustomException {
        public UpdateFireStationException(String message, Throwable cause) {
            super("Error while updating a fire station, additional information : " + message, cause);
        }
    }

    // Nested static class for update fire station method to encapsulate throwable general Exception in specific Exception
    public static class DeleteFireStationException extends FireStationCustomException {
        public DeleteFireStationException(String message, Throwable cause) {
            super("Error while deleting a fire station, additional information : " + message, cause);
        }
    }
}
