package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdateFireStationDto;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FireStationService {

    ManageJsonData manageJsonData;

    public FireStationService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    public void addFireStation(AddOrUpdateFireStationDto newFireStation) throws IOException {
        List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
        if(listFireStationExisting.stream().noneMatch(fireStationExist -> fireStationExist.getStation().equals(newFireStation.getStation()) && fireStationExist.getAddress().equals(newFireStation.getAddress()))) {
            listFireStationExisting.add(new FireStationModel(newFireStation.getAddress(), newFireStation.getStation()));
        }
        manageJsonData.fireStationWriterJsonData(listFireStationExisting);
    }
}
