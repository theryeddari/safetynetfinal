package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents address information including city and zip code.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubStationsReplyInfoAddress {
    /**
     * Constructor for Jackson serialization.
     */
    public SubStationsReplyInfoAddress() {
        // Constructor for Jackson serialization
    }

    private String address;
    private String city;
    private String zip;
}
