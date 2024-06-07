package com.openclassrooms.safetynet.exceptions;

/**
 * Custom exception class for managing JSON data exceptions.
 */
public class ManageJsonDataCustomException extends Exception {

    /**
     * Constructor with message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public ManageJsonDataCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Nested static class for encapsulating general Exception into a specific Exception
     * related to person writer method errors.
     */
    public static class PersonWriterException extends ManageJsonDataCustomException {

        /**
         * Constructor with cause.
         *
         * @param cause the cause of the exception.
         */
        public PersonWriterException(Throwable cause) {
            super("Error while writing information about a person, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Nested static class for encapsulating general Exception into a specific Exception
     * related to fire station writer method errors.
     */
    public static class FireStationWriterException extends ManageJsonDataCustomException {

        /**
         * Constructor with cause.
         *
         * @param cause the cause of the exception.
         */
        public FireStationWriterException(Throwable cause) {
            super("Error while writing information about a fire station, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Nested static class for encapsulating general Exception into a specific Exception
     * related to medical record writer method errors.
     */
    public static class MedicalRecordWriterException extends ManageJsonDataCustomException {

        /**
         * Constructor with cause.
         *
         * @param cause the cause of the exception.
         */
        public MedicalRecordWriterException(Throwable cause) {
            super("Error while writing information about a medical record, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Nested static class for encapsulating general Exception into a specific Exception
     * related to initialization errors of ManageJsonData.
     */
    public static class InitException extends ManageJsonDataCustomException {

        /**
         * Constructor with cause.
         *
         * @param cause the cause of the exception.
         */
        public InitException(Throwable cause) {
            super("Error while initialization of ManageJsonData, additional information: " + cause.getClass() + " " + cause.getMessage(), cause);
        }
    }

}
