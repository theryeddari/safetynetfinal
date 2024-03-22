package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class MedicalRecordModel {
    String firstName;
    String lastName;
    LocalDate birthDate;
    List<String> medication;
    List<String> allergies;
}

