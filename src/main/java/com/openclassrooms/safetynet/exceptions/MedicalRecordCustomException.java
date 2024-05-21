package com.openclassrooms.safetynet.exceptions;

public class MedicalRecordCustomException extends Exception{
    // Constructor with message and cause
    public MedicalRecordCustomException(String message, Throwable cause) {super(message, cause);}

    // Nested static class for add medical record method to encapsulate throwable general Exception in specific Exception
    public static class AddMedicalRecordException extends MedicalRecordCustomException {
        public AddMedicalRecordException(String message, Throwable cause) {
            super("Error while adding a medical record, additional information : " + message, cause);
        }
    }

    // Nested static class for update medical record method to encapsulate throwable general Exception in specific Exception
    public static class UpdateMedicalRecordException extends MedicalRecordCustomException {
        public UpdateMedicalRecordException(String message, Throwable cause) {
            super("Error while updating a medical record, additional information : " + message, cause);
        }
    }

    // Nested static class for update medical record method to encapsulate throwable general Exception in specific Exception
    public static class DeleteMedicalRecordException extends MedicalRecordCustomException {
        public DeleteMedicalRecordException(String message, Throwable cause) {
            super("Error while deleting a medical record, additional information : " + message, cause);
        }
    }
    
}
