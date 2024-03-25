package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class ChildAlertReplyPersonDTO {
    String firstName;
    String lastName;
    LocalDate birthdate;
    List<Map<String,String>> identityFamily;
}

