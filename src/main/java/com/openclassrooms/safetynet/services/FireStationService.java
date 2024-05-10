package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddFireStationDto;
import com.openclassrooms.safetynet.dto.requests.UpdateFireStationDto;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FireStationService {

    ManageJsonData manageJsonData;

    public FireStationService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    public void addFireStation(AddFireStationDto newFireStation) throws IOException {
        List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
        if(listFireStationExisting.stream().noneMatch(fireStationExist -> fireStationExist.getStation().equals(newFireStation.getStation()) && fireStationExist.getAddress().equals(newFireStation.getAddress()))) {
            listFireStationExisting.add(new FireStationModel(newFireStation.getAddress(), newFireStation.getStation()));
        }
        manageJsonData.fireStationWriterJsonData(listFireStationExisting);
    }

    public void updateFireStation(UpdateFireStationDto updateFireStation) throws IOException {
        List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
        //get the reference to the filtered person object
        Optional<FireStationModel> wantedFireStationUpdate = listFireStationExisting.stream().filter(fireStation ->
                fireStation.getStation().equals(updateFireStation.getStation())
                        && fireStation.getAddress().equals(updateFireStation.getAddress())).findFirst();
        wantedFireStationUpdate.ifPresent(fireStation -> {
            fireStation.setAddress(updateFireStation.getAddress());
            fireStation.setStation(updateFireStation.getNewNumberStation());
        });
        manageJsonData.fireStationWriterJsonData(listFireStationExisting);
    }
}
