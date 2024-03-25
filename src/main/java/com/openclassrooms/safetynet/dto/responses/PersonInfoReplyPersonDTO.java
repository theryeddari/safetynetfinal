package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.models.MedicalRecordModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class PersonInfoReplyPersonDTO {
    String lastName;
    String address;
    String city;
    int zip;
    LocalDate birthdate;
    String mail;
    List<String> medication;
    List<String> allergies;
}
