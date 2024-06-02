package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.dto.requests.AddPersonDto;
import com.openclassrooms.safetynet.dto.requests.DeletePersonDto;
import com.openclassrooms.safetynet.dto.requests.UpdatePersonDto;
import com.openclassrooms.safetynet.dto.responses.*;
import com.openclassrooms.safetynet.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.openclassrooms.safetynet.exceptions.PersonCustomException.*;

@RestController
public class PersonController {

    private final PersonService personService;

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/firestation")
    public FireStationReplyPersonDTO getResidentsByFireStation(@RequestParam("stationNumber") String stationNumber) throws FireStationResponseException {
        FireStationReplyPersonDTO result = personService.fireStationReply(stationNumber);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", stationNumber, result);
        return result;
    }

    @GetMapping("/childAlert")
    public ChildAlertReplyPersonDTO getChildAlertByAddress(@RequestParam("address") String address) throws ChildAlertResponseException {
        ChildAlertReplyPersonDTO result = personService.childAlertReply(address);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", address, result);
        return result;
    }

    @GetMapping("/phoneAlert")
    public PhoneAlertReplyPersonDTO getPhoneAlertByFireStation(@RequestParam("firestation") String fireStationNumber) throws PhoneAlertResponseException {
        PhoneAlertReplyPersonDTO result = personService.phoneAlert(fireStationNumber);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", fireStationNumber, result);
        return result;
    }

    @GetMapping("/fire")
    public FireReplyPersonDTO getFireByAddress(@RequestParam("address") String address) throws FireResponseException {
        FireReplyPersonDTO result = personService.fire(address);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", address, result);
        return result;
    }

    @GetMapping("/flood/stations")
    public StationsReplyPersonDTO getFloodStationsByStations(@RequestParam("stations") List<String> stationNumbers) throws FloodStationResponseException {
        StationsReplyPersonDTO result = personService.floodStation(stationNumbers);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", stationNumbers, result);
        return result;
    }

    @GetMapping("/personInfo")
    public PersonInfoReplyPersonDTO getPersonInfoByNames(@RequestParam("lastName") String lastName) throws PersonInfoResponseException {
        PersonInfoReplyPersonDTO result = personService.personInfo(lastName);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", lastName, result);
        return result;
    }

    @GetMapping("/communityEmail")
    public CommunityEmailReplyPersonDTO getCommunityEmailByCity(@RequestParam("city") String city) throws CommunityEmailException {
        CommunityEmailReplyPersonDTO result = personService.communityEmail(city);
        logger.info("Requête GET recue avec la donnée : {} \n Réponse du GET avec les données : {}", city, result);
        return result;
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody AddPersonDto addPersonDto) throws AddPersonException {
        personService.addPerson(addPersonDto);
        logger.info("Requête POST bien enregistré avec les données suivante : {}", addPersonDto);
    }

    @PutMapping("/person")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePerson(@RequestBody UpdatePersonDto updatePersonDTO) throws UpdatePersonException {
        personService.updatePerson(updatePersonDTO);
        logger.info("Requête PATCH bien enregistré avec les données suivante : {}", updatePersonDTO);
    }

    @DeleteMapping("/person")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(DeletePersonDto deletePersonDto) throws DeletePersonException {
        personService.deletePerson(deletePersonDto);
        logger.info("Requête DELETE bien excécuté avec les données suivante : {}", deletePersonDto);
    }
}