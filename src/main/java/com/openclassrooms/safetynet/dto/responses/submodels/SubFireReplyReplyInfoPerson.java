package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Represents personal information of an individual in a fire alert reply,
 * including contact details, birthdate, medications, and allergies.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubFireReplyReplyInfoPerson {
    /**
     * Constructor for Jackson serialization.
     */
    public SubFireReplyReplyInfoPerson() {
        // Constructor for Jackson serialization
    }

    private String lastName;
    private String phone;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
