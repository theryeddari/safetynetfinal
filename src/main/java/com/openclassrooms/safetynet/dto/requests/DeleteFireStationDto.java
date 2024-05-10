package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class DeleteFireStationDto {
    public DeleteFireStationDto(){
        //constructor for jackson serialisation
    }
    String address;
    String station;
}
