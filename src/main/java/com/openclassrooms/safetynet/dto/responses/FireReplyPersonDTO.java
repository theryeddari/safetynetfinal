package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class FireReplyPersonDTO {
    String lastName;
    String phone;
    LocalDate birthdate;
    List<String> medication;
    List<String> allergies;
}
