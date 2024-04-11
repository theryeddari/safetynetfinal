package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
@Getter
@AllArgsConstructor
@ToString
public class SubFireReplyReplyInfoPerson {
    public SubFireReplyReplyInfoPerson(){
        //constructor for jackson serialisation
    }
    String lastName;
    String phone;
    String birthdate;
    List<String> medications;
    List<String> allergies;

}
