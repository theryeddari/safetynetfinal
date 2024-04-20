package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class SubStationsReplyInfoAddress {
    public SubStationsReplyInfoAddress(){
        //constructor for jackson serialisation
    }
    String address;
    String city;
    String zip;
}
