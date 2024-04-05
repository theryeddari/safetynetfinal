package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SubFireStationReplyPerson {
    public SubFireStationReplyPerson(){
        //constructor for jackson serialisation
    }
    String firstName;
    String lastName;
    String address;
    String city;
    int zip;
    String phone;
}
