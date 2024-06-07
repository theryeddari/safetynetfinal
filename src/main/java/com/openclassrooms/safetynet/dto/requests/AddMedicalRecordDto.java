package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * DTO to send the data of the medical record to be added
 */
@Getter
@AllArgsConstructor
@ToString
public class AddMedicalRecordDto {
    public AddMedicalRecordDto() {
        //constructor for jackson serialisation
    }
    String firstName;
    String lastName;
    String birthdate;
    List<String> medications;
    List<String> allergies;
}