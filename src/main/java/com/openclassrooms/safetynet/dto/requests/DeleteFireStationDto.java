package com.openclassrooms.safetynet.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
/**
 * DTO to send the data of the FireStation to be deleted
 */
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
