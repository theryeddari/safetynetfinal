package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
/**
 * DTO to send the data of the person to be deleted
 */
@Getter
@AllArgsConstructor
@ToString
public class DeletePersonDto {
    public DeletePersonDto(){
        //constructor for jackson serialisation
    }
    @NonNull
    String firstName;
    @NonNull
    String lastName;
}
