package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class FireStationModel {
    public FireStationModel(){        //constructor for jackson serialisation
        //constructor for jackson serialisation
    }
    String address;
    String station;
}
