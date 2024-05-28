package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AddPersonDto {
    public AddPersonDto(){
        //constructor for jackson serialisation
    }
    @NonNull
    String firstName;
    @NonNull
    String lastName;
    String address;
    String city;
    String zip;
    String phone;
    String email;
}