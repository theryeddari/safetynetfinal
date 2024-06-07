package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO to send the data of the fire station to be added
 */
@Getter
@AllArgsConstructor
@ToString
public class AddFireStationDto {
    public AddFireStationDto(){
        //constructor for jackson serialisation
    }
    @NonNull
    String address;
    @NonNull
    String station;
}
