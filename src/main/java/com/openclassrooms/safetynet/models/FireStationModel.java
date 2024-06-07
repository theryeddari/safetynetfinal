package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a fire station.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class FireStationModel {
    /**
     * Default constructor for Jackson serialization.
     */
    public FireStationModel() {
        // Default constructor for Jackson serialization
    }

    /**
     * The address associated with the fire station.
     */
    private String address;

    /**
     * The station number.
     */
    private String station;
}
