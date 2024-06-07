package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
/**
 * DTO to send the data to be updated concerning a FireStation.
 */
@Getter
@AllArgsConstructor
@ToString
public class UpdateFireStationDto {
    public UpdateFireStationDto(){
        //constructor for jackson serialisation
    }
    @NonNull
    String address;
    @NonNull
    String station;
    @NonNull
    String newNumberStation;
}
