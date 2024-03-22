package com.openclassrooms.safetynet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class FireStationModel {
    String address;
    List<Integer> station;
}
