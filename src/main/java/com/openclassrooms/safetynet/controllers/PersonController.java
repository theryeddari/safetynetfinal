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
/**
 * Controller for handling person related requests.
 */
@RestController
public class PersonController {

    private final PersonService personService;
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Retrieves residents served by the specified fire station.
     *
     * @param stationNumber The station number to query.
     * @return A DTO containing information about the residents.
     * @throws FireStationResponseException If an error occurs during the process.
     */
    @GetMapping("/firestation")
    public FireStationReplyPersonDTO getResidentsByFireStation(@RequestParam("stationNumber") String stationNumber) throws FireStationResponseException {
        logger.info("Start request GET /firestation with stationNumber: {}", stationNumber);
        FireStationReplyPersonDTO result = personService.fireStationReply(stationNumber);
        logger.info("End request GET /firestation with result: {}", result);
        return result;
    }

    /**
     * Retrieves children living at the specified address.
     *
     * @param address The address to query.
     * @return A DTO containing information about the children and other household members.
     * @throws ChildAlertResponseException If an error occurs during the process.
     */
    @GetMapping("/childAlert")
    public ChildAlertReplyPersonDTO getChildAlertByAddress(@RequestParam("address") String address) throws ChildAlertResponseException {
        logger.info("Start request GET /childAlert with address: {}", address);
        ChildAlertReplyPersonDTO result = personService.childAlertReply(address);
        logger.info("End request GET /childAlert with result: {}", result);
        return result;
    }

    /**
     * Retrieves phone numbers of residents served by the specified fire station.
     *
     * @param fireStationNumber The fire station number to query.
     * @return A DTO containing the phone numbers.
     * @throws PhoneAlertResponseException If an error occurs during the process.
     */
    @GetMapping("/phoneAlert")
    public PhoneAlertReplyPersonDTO getPhoneAlertByFireStation(@RequestParam("firestation") String fireStationNumber) throws PhoneAlertResponseException {
        logger.info("Start request GET /phoneAlert with firestation: {}", fireStationNumber);
        PhoneAlertReplyPersonDTO result = personService.phoneAlert(fireStationNumber);
        logger.info("End request GET /phoneAlert with result: {}", result);
        return result;
    }

    /**
     * Retrieves information about residents and fire stations serving the specified address.
     *
     * @param address The address to query.
     * @return A DTO containing information about the residents and fire stations.
     * @throws FireResponseException If an error occurs during the process.
     */
    @GetMapping("/fire")
    public FireReplyPersonDTO getFireByAddress(@RequestParam("address") String address) throws FireResponseException {
        logger.info("Start request GET /fire with address: {}", address);
        FireReplyPersonDTO result = personService.fire(address);
        logger.info("End request GET /fire with result: {}", result);
        return result;
    }

    /**
     * Retrieves information on households served by the specified fire stations.
     *
     * @param stationNumbers The list of fire station numbers to query.
     * @return A DTO containing information about the households.
     * @throws FloodStationResponseException If an error occurs during the process.
     */
    @GetMapping("/flood/stations")
    public StationsReplyPersonDTO getFloodStationsByStations(@RequestParam("stations") List<String> stationNumbers) throws FloodStationResponseException {
        logger.info("Start request GET /flood/stations with stations: {}", stationNumbers);
        StationsReplyPersonDTO result = personService.floodStation(stationNumbers);
        logger.info("End request GET /flood/stations with result: {}", result);
        return result;
    }

    /**
     * Retrieves detailed personal information by last name.
     *
     * @param lastName The last name to query.
     * @return A DTO containing detailed personal information.
     * @throws PersonInfoResponseException If an error occurs during the process.
     */
    @GetMapping("/personInfo")
    public PersonInfoReplyPersonDTO getPersonInfoByNames(@RequestParam("lastName") String lastName) throws PersonInfoResponseException {
        logger.info("Start request GET /personInfo with lastName: {}", lastName);
        PersonInfoReplyPersonDTO result = personService.personInfo(lastName);
        logger.info("End request GET /personInfo with result: {}", result);
        return result;
    }

    /**
     * Retrieves email addresses of all residents in the specified city.
     *
     * @param city The city to query.
     * @return A DTO containing the email addresses.
     * @throws CommunityEmailException If an error occurs during the process.
     */
    @GetMapping("/communityEmail")
    public CommunityEmailReplyPersonDTO getCommunityEmailByCity(@RequestParam("city") String city) throws CommunityEmailException {
        logger.info("Start request GET /communityEmail with city: {}", city);
        CommunityEmailReplyPersonDTO result = personService.communityEmail(city);
        logger.info("End request GET /communityEmail with result: {}", result);
        return result;
    }

    /**
     * Adds a new person.
     *
     * @param addPersonDto The DTO containing the person's information to add.
     * @throws AddPersonException If an error occurs during the process.
     */
    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody AddPersonDto addPersonDto) throws AddPersonException {
        logger.info("Start request POST /person with data: {}", addPersonDto);
        personService.addPerson(addPersonDto);
        logger.info("End request POST /person");
    }

    /**
     * Updates an existing person's information.
     *
     * @param updatePersonDto The DTO containing the updated information.
     * @throws UpdatePersonException If an error occurs during the process.
     */
    @PutMapping("/person")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePerson(@RequestBody UpdatePersonDto updatePersonDto) throws UpdatePersonException {
        logger.info("Start request PUT /person with data: {}", updatePersonDto);
        personService.updatePerson(updatePersonDto);
        logger.info("End request PUT /person");
    }

    /**
     * Deletes a person.
     *
     * @param deletePersonDto The DTO containing the person's information to delete.
     * @throws DeletePersonException If an error occurs during the process.
     */
    @DeleteMapping("/person")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@RequestBody DeletePersonDto deletePersonDto) throws DeletePersonException {
        logger.info("Start request DELETE /person with data: {}", deletePersonDto);
        personService.deletePerson(deletePersonDto);
        logger.info("End request DELETE /person");
    }
}
