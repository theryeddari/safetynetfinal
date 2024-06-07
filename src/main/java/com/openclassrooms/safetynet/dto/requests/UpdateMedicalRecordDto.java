package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
/**
 * DTO to send the data to be updated concerning a Medical record.
 */
@Getter
@AllArgsConstructor
@ToString
public class UpdateMedicalRecordDto {
    public UpdateMedicalRecordDto() {
        //constructor for jackson serialisation
    }
    String firstName;
    String lastName;
    String birthdate;
    List<String> medications;
    List<String> allergies;
}