package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class SubPersonInfoReplyPerson {
    public SubPersonInfoReplyPerson(){
        //constructor for jackson serialisation
    }
    String lastName;
    String address;
    String city;
    String zip;
    String mail;
    String birthdate;
    List<String> medication;
    List<String> allergies;
}
