package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents the count of adults and minors for a fire station coverage area.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubFireStationModelReplyForCount {
    /**
     * Constructor for Jackson serialization.
     */
    public SubFireStationModelReplyForCount() {
        // Constructor for Jackson serialization
    }

    private String adultCount;
    private String minorCount;
}
