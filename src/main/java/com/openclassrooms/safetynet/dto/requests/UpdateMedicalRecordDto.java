package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

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