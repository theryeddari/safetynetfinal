package com.openclassrooms.safetynet.exceptions;

public class MedicalRecordCustomException extends Exception{
    // Constructor with message and cause
    public MedicalRecordCustomException(String message, Throwable cause) {super(message, cause);}

    // Constructor with message
    public MedicalRecordCustomException(String message) {super(message);}

    // Nested static class for add medical record method to encapsulate throwable general Exception in specific Exception
    public static class AddMedicalRecordException extends MedicalRecordCustomException {
        public AddMedicalRecordException(Throwable cause) {
            super("Error while adding a medical record, additional information : " +cause.getClass().getName() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for update medical record method to encapsulate throwable general Exception in specific Exception
    public static class UpdateMedicalRecordException extends MedicalRecordCustomException {
        public UpdateMedicalRecordException(Throwable cause) {
            super("Error while updating a medical record, additional information : " +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for update medical record method to encapsulate throwable general Exception in specific Exception
    public static class DeleteMedicalRecordException extends MedicalRecordCustomException {
        public DeleteMedicalRecordException(Throwable cause) {
            super("Error while deleting a medical record, additional information : " +cause.getClass() +" "+ cause.getMessage(), cause);
        }
    }

    // Nested static class for add medical record method for exception when medical record already exist
    public static class AlreadyExistMedicalRecordException extends MedicalRecordCustomException {

        public AlreadyExistMedicalRecordException() {
            super("Error, The Medical file already exists, if you wish to modify it use the update procedure");
        }
    }

    // Nested static class for update medical record method for exception when medical record not found
    public static class NotFoundMedicalRecordException extends MedicalRecordCustomException {

        public NotFoundMedicalRecordException() {
            super("Error, The Medical file wanted its not found, if you wish to add it use the add procedure");
        }
    }
}
