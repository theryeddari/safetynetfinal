package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
/**
 * DTO to send the data to be updated concerning a person.
 */
@Getter
@AllArgsConstructor
@ToString
public class UpdatePersonDto {
    public UpdatePersonDto(){
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
