package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a person.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class PersonModel {
    /**
     * Default constructor for Jackson serialization.
     */
    public PersonModel() {
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
     * The address of the person.
     */
    private String address;

    /**
     * The city where the person lives.
     */
    private String city;

    /**
     * The ZIP code of the person's city.
     */
    private String zip;

    /**
     * The phone number of the person.
     */
    private String phone;

    /**
     * The email address of the person.
     */
    private String email;
}
