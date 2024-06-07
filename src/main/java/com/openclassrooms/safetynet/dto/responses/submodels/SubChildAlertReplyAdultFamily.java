package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents an adult family member in a child alert reply.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubChildAlertReplyAdultFamily {
    /**
     * Constructor for Jackson serialization.
     */
    public SubChildAlertReplyAdultFamily() {
        // Constructor for Jackson serialization
    }

    private String firstName;
    private String lastName;
}
