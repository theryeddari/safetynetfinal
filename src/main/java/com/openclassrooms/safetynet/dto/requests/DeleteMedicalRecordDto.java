package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class DeleteMedicalRecordDto {
    public DeleteMedicalRecordDto(){
        //constructor for jackson serialisation
    }
    @NonNull
    String firstName;
    @NonNull
    String lastName;
}
