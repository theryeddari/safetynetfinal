package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class PersonModel {
    public PersonModel(){
        //constructor for jackson serialisation
    }
    String firstName;
    String lastName;
    String address;
    String city;
    String zip;
    String phone;
    String email;
}
