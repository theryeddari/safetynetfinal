package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Represents a medical record.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class MedicalRecordModel {
    /**
     * Default constructor for Jackson serialization.
     */
    public MedicalRecordModel() {
        // Default constructor for Jackson serialization
    }

    /**
     * The first name of the person.
     */
    private String firstName;

    /**
     * The last name of the person.
     */
    private String lastName;

    /**
     * The birthdate of the person.
     */
    private String birthdate;

    /**
     * The list of medications for the person.
     */
    private List<String> medications;

    /**
     * The list of allergies for the person.
     */
    private List<String> allergies;
}
