package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddFireStationDto;
import com.openclassrooms.safetynet.dto.requests.DeleteFireStationDto;
import com.openclassrooms.safetynet.dto.requests.UpdateFireStationDto;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FireStationService {

    ManageJsonData manageJsonData;

    // Constructor to inject ManageJsonData dependency
    public FireStationService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    // Method to add a new fire station
    public void addFireStation(AddFireStationDto newFireStation) throws IOException {
        List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
        // Check if the fire station already exists
        if (listFireStationExisting.stream().noneMatch(fireStationExist -> fireStationExist.getStation().equals(newFireStation.getStation()) && fireStationExist.getAddress().equals(newFireStation.getAddress()))) {
            // Add the new fire station if it does not exist
            listFireStationExisting.add(new FireStationModel(newFireStation.getAddress(), newFireStation.getStation()));
        }
        // Write the updated list of fire station back to the JSON file
        manageJsonData.fireStationWriterJsonData(listFireStationExisting);
    }

    // Method to update an existing fire station
    public void updateFireStation(UpdateFireStationDto updateFireStation) throws IOException {
        List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
        //get the reference to the filtered fire station object
        Optional<FireStationModel> wantedFireStationUpdate = listFireStationExisting.stream().filter(fireStation ->
                fireStation.getStation().equals(updateFireStation.getStation())
                        && fireStation.getAddress().equals(updateFireStation.getAddress())).findFirst();
        // If there is a reference, access the object and modify its properties
        wantedFireStationUpdate.ifPresent(fireStation -> {
            fireStation.setAddress(updateFireStation.getAddress());
            fireStation.setStation(updateFireStation.getNewNumberStation());
        });
        // Write the updated list of fire station back to the JSON file
        manageJsonData.fireStationWriterJsonData(listFireStationExisting);
    }

    // Method to delete an existing fire station
    public void deleteFireStation(DeleteFireStationDto deleteFireStation) throws IOException {
        List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
        if (deleteFireStation.getStation() == null && deleteFireStation.getAddress() == null) {
            //TODO: exception

            // Remove the fire station if it matches
        } else {
            // Remove all station fire with this address
            if (deleteFireStation.getStation() == null || deleteFireStation.getStation().isBlank()) {
                if(!listFireStationExisting.removeIf(fireStation -> fireStation.getAddress().equals(deleteFireStation.getAddress()))){
                    //TODO : exception
                }
            // Remove all station fire with this number station
            } else if (deleteFireStation.getAddress() == null || deleteFireStation.getAddress().isBlank()) {
                if(!listFireStationExisting.removeIf(fireStation -> fireStation.getStation().equals(deleteFireStation.getStation()))){
                    //TODO : exception
                }
            // Remove only fire station with this number station and address
            } else {
                if(listFireStationExisting.removeIf(fireStation -> fireStation.getAddress().equals(deleteFireStation.getAddress()) && fireStation.getStation().equals(deleteFireStation.getStation()))){
                    //TODO : exception
                }
            }
        }
        // Write the updated list of fire station back to the JSON file
        manageJsonData.fireStationWriterJsonData(listFireStationExisting);
    }
}
