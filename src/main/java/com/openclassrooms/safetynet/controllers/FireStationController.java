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

@RestController
public class FireStationController {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @PostMapping("/firestation")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFireStation(@RequestBody AddFireStationDto addFireStationDto) throws AddFireStationException {
        fireStationService.addFireStation(addFireStationDto);
        logger.info("POST request successfully saved with the following data: {}",addFireStationDto);
    }
    @PutMapping("/firestation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFireStation(@RequestBody UpdateFireStationDto updateFireStationDto ) throws UpdateFireStationException {
        fireStationService.updateFireStation(updateFireStationDto);
        logger.info("PUT request successfully saved with the following data: {}", updateFireStationDto);
    }
    @DeleteMapping("/firestation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFireStation(@RequestBody DeleteFireStationDto deleteFireStationDto) throws DeleteFireStationException {
        fireStationService.deleteFireStation(deleteFireStationDto);
        logger.info("DELETE request successfully saved with the following data: {}",deleteFireStationDto);
    }
}