package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PersonModel {
    String firstName;
    String lastName;
    String address;
    String city;
    int zip;
    String phone;
    String mail;
}
