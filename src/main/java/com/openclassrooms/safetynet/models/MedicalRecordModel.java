package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class MedicalRecordModel {
    public MedicalRecordModel(){
        //constructor for jackson serialisation
    }
    String firstName;
    String lastName;
    String birthdate;
    List<String> medications;
    List<String> allergies;
}

