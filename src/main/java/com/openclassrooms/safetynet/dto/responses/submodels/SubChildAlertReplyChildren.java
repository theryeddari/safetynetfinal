package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SubChildAlertReplyChildren {
    public SubChildAlertReplyChildren(){
        //constructor for jackson serialisation
    }
    String firstName;
    String lastName;
    String birthdate;
}
