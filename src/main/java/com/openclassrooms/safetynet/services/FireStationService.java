package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddFireStationDto;
import com.openclassrooms.safetynet.dto.requests.DeleteFireStationDto;
import com.openclassrooms.safetynet.dto.requests.UpdateFireStationDto;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static com.openclassrooms.safetynet.exceptions.FireStationCustomException.*;

@Service
public class FireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);
    ManageJsonData manageJsonData;

    // Constructor to inject ManageJsonData dependency
    public FireStationService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    // Method to add a new fire station
    public void addFireStation(AddFireStationDto newFireStation) throws AddFireStationException {
        logger.info("Adding new fire station at address: {}", newFireStation.getAddress());
        try {
            List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
            logger.debug("Existing fire stations: {}", listFireStationExisting);

            // Check if the fire station already exists
            if (listFireStationExisting.stream().noneMatch(fireStationExist ->
                    fireStationExist.getStation().equals(newFireStation.getStation()) &&
                            fireStationExist.getAddress().equals(newFireStation.getAddress()))) {

                // Add the new fire station if it does not exist
                listFireStationExisting.add(new FireStationModel(newFireStation.getAddress(), newFireStation.getStation()));
                logger.info("New fire station added successfully.");
            } else {
                throw new AlreadyExistFireStationException();
            }

            // Write the updated list of fire station back to the JSON file
            manageJsonData.fireStationWriterJsonData(listFireStationExisting);
        } catch (Exception e) {
            logger.error("Error adding fire station: ", e);
            throw new AddFireStationException(e);
        }
    }

    // Method to update an existing fire station
    public void updateFireStation(UpdateFireStationDto updateFireStation) throws UpdateFireStationException {
        logger.info("Updating fire station at address: {}", updateFireStation.getAddress());
        try {
            List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
            logger.debug("Existing fire stations: {}", listFireStationExisting);

            // Get the reference to the filtered fire station object
            Optional<FireStationModel> wantedFireStationUpdate = listFireStationExisting.stream()
                    .filter(fireStation -> fireStation.getStation().equals(updateFireStation.getStation()) &&
                            fireStation.getAddress().equals(updateFireStation.getAddress()))
                    .findFirst();

            if (wantedFireStationUpdate.isEmpty()) {
                throw new NotFoundFireStationException();
            }

            // If there is a reference, access the object and modify its properties
            wantedFireStationUpdate.ifPresent(fireStation -> {
                fireStation.setAddress(updateFireStation.getAddress());
                fireStation.setStation(updateFireStation.getNewNumberStation());
            });

            // Write the updated list of fire station back to the JSON file
            manageJsonData.fireStationWriterJsonData(listFireStationExisting);
            logger.info("Fire station updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating fire station: ", e);
            throw new UpdateFireStationException(e);
        }
    }

    // Method to delete an existing fire station
    public void deleteFireStation(DeleteFireStationDto deleteFireStation) throws DeleteFireStationException {
        logger.info("Deleting fire station with address: {} and station: {}", deleteFireStation.getAddress(), deleteFireStation.getStation());
        try {
            List<FireStationModel> listFireStationExisting = manageJsonData.fireStationReaderJsonData();
            logger.debug("Existing fire stations: {}", listFireStationExisting);

            if ((deleteFireStation.getStation() == null || deleteFireStation.getStation().isBlank()) &&
                    (deleteFireStation.getAddress() == null || deleteFireStation.getAddress().isBlank())) {
                throw new MissingFireStationArgument();
            } else {
                // Remove all station fire with this address
                if ((deleteFireStation.getStation() == null || deleteFireStation.getStation().isBlank()) &&
                    listFireStationExisting.removeIf(fireStation -> fireStation.getAddress().equals(deleteFireStation.getAddress()))) {
                    logger.info("Fire stations with address {} deleted successfully.", deleteFireStation.getAddress());
                }
                // Remove all station fire with this number station
                else if ((deleteFireStation.getAddress() == null || deleteFireStation.getAddress().isBlank()) &&
                         listFireStationExisting.removeIf(fireStation -> fireStation.getStation().equals(deleteFireStation.getStation()))) {
                    logger.info("Fire stations with station number {} deleted successfully.", deleteFireStation.getStation());
                }
                // Remove only fire station with this number station and address
                else if (listFireStationExisting.removeIf(fireStation ->
                    fireStation.getAddress().equals(deleteFireStation.getAddress()) &&
                    fireStation.getStation().equals(deleteFireStation.getStation()))) {
                    logger.info("Fire station with address {} and station number {} deleted successfully.", deleteFireStation.getAddress(), deleteFireStation.getStation());
                } else {
                    throw new NotFoundFireStationException();
                }
            }
            // Write the updated list of fire station back to the JSON file
            manageJsonData.fireStationWriterJsonData(listFireStationExisting);
        } catch (Exception e) {
            logger.error("Error deleting fire station: ", e);
            throw new DeleteFireStationException(e);
        }
    }
}
