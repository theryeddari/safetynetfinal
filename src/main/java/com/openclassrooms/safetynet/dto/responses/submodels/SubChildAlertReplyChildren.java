package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a child in a child alert reply, including their name and birthdate.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubChildAlertReplyChildren {
    /**
     * Constructor for Jackson serialization.
     */
    public SubChildAlertReplyChildren() {
        // Constructor for Jackson serialization
    }

    private String firstName;
    private String lastName;
    private String birthdate;
}
