package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.dto.requests.AddFireStationDto;
import com.openclassrooms.safetynet.dto.requests.DeleteFireStationDto;
import com.openclassrooms.safetynet.dto.requests.UpdateFireStationDto;
import com.openclassrooms.safetynet.services.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.openclassrooms.safetynet.exceptions.FireStationCustomException.*;

/**
 * Controller for handling fire station related requests.
 */
@RestController
public class FireStationController {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    private final FireStationService fireStationService;

    /**
     * Constructs a new FireStationController with the specified FireStationService.
     *
     * @param fireStationService the fire station service to be used by this controller
     */
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    /**
     * Adds a new fire station.
     *
     * @param addFireStationDto the fire station to add
     * @throws AddFireStationException if an error occurs during the addition
     */
    @PostMapping("/firestation")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFireStation(@RequestBody AddFireStationDto addFireStationDto) throws AddFireStationException {
        logger.info("Attempting to save POST request with data: {}", addFireStationDto);
        fireStationService.addFireStation(addFireStationDto);
        logger.info("POST request successfully saved with the following data: {}", addFireStationDto);
    }

    /**
     * Updates an existing fire station.
     *
     * @param updateFireStationDto the fire station to update
     * @throws UpdateFireStationException if an error occurs during the update
     */
    @PutMapping("/firestation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFireStation(@RequestBody UpdateFireStationDto updateFireStationDto) throws UpdateFireStationException {
        logger.info("Attempting to save PUT request with data: {}", updateFireStationDto);
        fireStationService.updateFireStation(updateFireStationDto);
        logger.info("PUT request successfully saved with the following data: {}", updateFireStationDto);
    }

    /**
     * Deletes a fire station.
     *
     * @param deleteFireStationDto the fire station to delete
     * @throws DeleteFireStationException if an error occurs during the deletion
     */
    @DeleteMapping("/firestation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFireStation(@RequestBody DeleteFireStationDto deleteFireStationDto) throws DeleteFireStationException {
        logger.info("Attempting to save DELETE request with data: {}", deleteFireStationDto);
        fireStationService.deleteFireStation(deleteFireStationDto);
        logger.info("DELETE request successfully saved with the following data: {}", deleteFireStationDto);
    }
}
