package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Represents detailed personal and medical information of an individual.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubPersonInfoReplyPerson {
    /**
     * Constructor for Jackson serialization.
     */
    public SubPersonInfoReplyPerson() {
        // Constructor for Jackson serialization
    }

    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String mail;
    private String birthdate;
    private List<String> medication;
    private List<String> allergies;
}
