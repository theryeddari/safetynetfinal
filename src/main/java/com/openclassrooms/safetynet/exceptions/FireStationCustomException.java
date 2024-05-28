package com.openclassrooms.safetynet.exceptions;

public class FireStationCustomException extends Exception{
    // Constructor with message and cause
    public FireStationCustomException(String message, Throwable cause) {super(message,cause);}
    public FireStationCustomException(String message) {super(message);}

    // Nested static class for add fire station method to encapsulate throwable general Exception in specific Exception
    public static class AddFireStationException extends FireStationCustomException {
        public AddFireStationException(Throwable cause) {
            super("Error while adding a fire station, additional information : " +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for update fire station method to encapsulate throwable general Exception in specific Exception
    public static class UpdateFireStationException extends FireStationCustomException {
        public UpdateFireStationException(Throwable cause) {
            super("Error while updating a fire station, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for delete fire station method to encapsulate throwable general Exception in specific Exception
    public static class DeleteFireStationException extends FireStationCustomException {
        public DeleteFireStationException(Throwable cause) {
            super("Error while deleting a fire station, additional information : "  +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for add fire station method for exception when fireStation already exist
    public static class AlreadyExistFireStationException extends FireStationCustomException {
        public AlreadyExistFireStationException() {
            super("Error, The Station fire file already exists, if you wish to modify it use the update procedure");
        }
    }

    public static class NotFoundFireStationException extends FireStationCustomException {
        public NotFoundFireStationException() {
            super("Error, The Station fire file wanted its not found, if you wish to add it use the add procedure");
        }
    }

    public static class MissingFireStationArgument extends FireStationCustomException{

        public MissingFireStationArgument() {
            super("Error, address and station number not provided try again");
        }

    }
}
