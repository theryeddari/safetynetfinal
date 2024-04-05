package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SubFireStationModelReplyForCount {
    public SubFireStationModelReplyForCount(){
        //constructor for jackson serialisation
    }
    String adultCount;
    String minorCount;

}
