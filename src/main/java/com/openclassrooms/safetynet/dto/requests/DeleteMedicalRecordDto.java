package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
/**
 * DTO to send the data of the medical record to be deleted
 */
@Getter
@AllArgsConstructor
@ToString
public class DeleteMedicalRecordDto {
    public DeleteMedicalRecordDto(){
        //constructor for jackson serialisation
    }
    @NonNull
    String firstName;
    @NonNull
    String lastName;
}
