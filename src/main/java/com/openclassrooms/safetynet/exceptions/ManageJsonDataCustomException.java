package com.openclassrooms.safetynet.exceptions;

// Custom exception class for managing JSON data exceptions
public class ManageJsonDataCustomException extends Exception {

    // Constructor with message and cause
    public ManageJsonDataCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    // Nested static class for person writer method to encapsulate throwable general Exception in specific Exception
    public static class PersonWriterException extends ManageJsonDataCustomException {

        // Constructor with message and cause
        public PersonWriterException(String message, Throwable cause) {
            super("Error while writing information about a person, additional information : " + message, cause);
        }
    }

    // Nested static class for fire station writer method to encapsulate throwable general Exception in specific Exception
    public static class FireStationWriterException extends ManageJsonDataCustomException {

        // Constructor with message and cause
        public FireStationWriterException(String message, Throwable cause) {
            super("Error while writing information about a fire station, additional information : " + message, cause);
        }
    }

    // Nested static class for medical record writer method to encapsulate throwable general Exception in specific Exception
    public static class MedicalRecordWriterException extends ManageJsonDataCustomException {

        // Constructor with message and cause
        public MedicalRecordWriterException(String message, Throwable cause) {
            super("Error while writing information about a medical record, additional information : " + message, cause);
        }
    }

    // Nested static class for init method to encapsulate throwable general Exception in specific Exception
    public static class InitException extends ManageJsonDataCustomException {

        // Constructor with message and cause
        public InitException(String message, Throwable cause) {
            super("error while initialization of ManageJsonData, additional information : " + message, cause);
        }
    }

}