package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents personal and contact information of an individual related to a fire station response.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubFireStationReplyPerson {
    /**
     * Constructor for Jackson serialization.
     */
    public SubFireStationReplyPerson() {
        // Constructor for Jackson serialization
    }

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
}
