package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class StationsReplyPersonDTO {
    String address;
    String city;
    int zip;
    List<FireReplyPersonDTO> personsAtAddress;
}
