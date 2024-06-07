package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Represents personal and medical information of an individual, including contact details.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubStationsReplyInfoPerson {
    /**
     * Constructor for Jackson serialization.
     */
    public SubStationsReplyInfoPerson() {
        // Constructor for Jackson serialization
    }

    private String lastName;
    private String phone;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
