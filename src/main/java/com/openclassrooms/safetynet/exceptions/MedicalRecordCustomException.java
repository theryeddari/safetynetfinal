package com.openclassrooms.safetynet.exceptions;

/**
 * Custom exception class for handling medical record related exceptions.
 */
public class MedicalRecordCustomException extends Exception {

    /**
     * Constructor with message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public MedicalRecordCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message.
     *
     * @param message the detail message.
     */
    public MedicalRecordCustomException(String message) {
        super(message);
    }

    /**
     * Exception for errors when adding a medical record.
     */
    public static class AddMedicalRecordException extends MedicalRecordCustomException {
        public AddMedicalRecordException(Throwable cause) {
            super("Error while adding a medical record, additional information: " + cause.getClass().getName() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors when updating a medical record.
     */
    public static class UpdateMedicalRecordException extends MedicalRecordCustomException {
        public UpdateMedicalRecordException(Throwable cause) {
            super("Error while updating a medical record, additional information: " + cause.getClass().getName() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for errors when deleting a medical record.
     */
    public static class DeleteMedicalRecordException extends MedicalRecordCustomException {
        public DeleteMedicalRecordException(Throwable cause) {
            super("Error while deleting a medical record, additional information: " + cause.getClass().getName() + " " + cause.getMessage(), cause);
        }
    }

    /**
     * Exception for when a medical record already exists.
     */
    public static class AlreadyExistMedicalRecordException extends MedicalRecordCustomException {
        public AlreadyExistMedicalRecordException() {
            super("Error, The Medical file already exists, if you wish to modify it use the update procedure");
        }
    }

    /**
     * Exception for when a medical record is not found.
     */
    public static class NotFoundMedicalRecordException extends MedicalRecordCustomException {
        public NotFoundMedicalRecordException() {
            super("Error, The Medical file wanted is not found, if you wish to add it use the add procedure");
        }
    }
}
