package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddPersonDto;
import com.openclassrooms.safetynet.dto.requests.DeletePersonDto;
import com.openclassrooms.safetynet.dto.requests.UpdatePersonDto;
import com.openclassrooms.safetynet.dto.responses.ChildAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireStationReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.PhoneAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.StationsReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.PersonInfoReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.CommunityEmailReplyPersonDTO;

import com.openclassrooms.safetynet.dto.responses.submodels.*;
import com.openclassrooms.safetynet.exceptions.PersonCustomException;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.openclassrooms.safetynet.exceptions.PersonCustomException.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class PersonService {
    private static final Logger logger = LogManager.getLogger(PersonService.class);
    ManageJsonData manageJsonData;
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Constructor to inject ManageJsonData dependency.
     *
     * @param manageJsonData the manageJsonData to be injected
     */
    public PersonService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    /**
     * Method to get persons and count of minor and major based on station number.
     *
     * @param stationNumber the station number to query
     * @return FireStationReplyPersonDTO containing the information
     * @throws FireStationResponseException if an error occurs during the process
     */
    public FireStationReplyPersonDTO fireStationReply(String stationNumber) throws FireStationResponseException {
        logger.info("Fetching fire station reply for station number: {}", stationNumber);
        try {
            List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();
            logger.debug("Existing fire stations: {}", fireStationModelList);

            List<Map<Boolean, SubFireStationReplyPerson>> necessaryData = Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                    .flatMap(List::stream).collect(Collectors.groupingBy(model -> {
                                if (model instanceof PersonModel personModel) {
                                    return personModel.getFirstName() + personModel.getLastName();
                                } else {
                                    MedicalRecordModel medicalRecordModel = ((MedicalRecordModel) model);
                                    return medicalRecordModel.getFirstName() + medicalRecordModel.getLastName();
                                }
                            }
                    ))
                    .values()
                    .stream().filter(grouped -> grouped.stream().anyMatch(aloneModel -> aloneModel instanceof MedicalRecordModel))
                    .filter(grouped ->
                            grouped.stream().anyMatch(model -> model instanceof PersonModel personModel && fireStationModelList.stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList().toString().contains(personModel.getAddress()))
                    )
                    .map(subDto -> {
                        PersonModel personModel = subDto.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();
                        Boolean countMinorAdult = subDto.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).anyMatch(age -> LocalDate.parse(age.getBirthdate(), dateFormatter).until(currentDate).getYears() < 18);
                        SubFireStationReplyPerson subFireStationReplyPerson = new SubFireStationReplyPerson(personModel.getFirstName(), personModel.getLastName(), personModel.getAddress(), personModel.getCity(), personModel.getZip(), personModel.getPhone());
                        Map<Boolean, SubFireStationReplyPerson> booleanMap = new HashMap<>();
                        booleanMap.put(countMinorAdult, subFireStationReplyPerson);
                        logger.debug("Mapped person: {}, is minor: {}", subFireStationReplyPerson, countMinorAdult);
                        return booleanMap;
                    }).toList();

            List<SubFireStationReplyPerson> reply = necessaryData.stream().flatMap(model -> model.values().stream()).toList();
            long minor = necessaryData.stream().filter(booleanMinor -> booleanMinor.containsKey(true)).count();
            long adult = necessaryData.stream().filter(booleanAdult -> booleanAdult.containsKey(false)).count();

            SubFireStationModelReplyForCount subFireStationModelReplyForCount = new SubFireStationModelReplyForCount(String.valueOf(adult), String.valueOf(minor));
            logger.debug("Minor count: {}, Adult count: {}", minor, adult);
            logger.info("Fire station reply fetched successfully.");
            return new FireStationReplyPersonDTO(reply, subFireStationModelReplyForCount);
        } catch (Exception e) {
            logger.error("Error fetching fire station reply: ", e);
            throw new FireStationResponseException(e);
        }
    }

    /**
     * Method to get details of children and their tutors' full names based on address.
     *
     * @param address the address to query
     * @return ChildAlertReplyPersonDTO containing the information
     * @throws ChildAlertResponseException if an error occurs during the process
     */
    public ChildAlertReplyPersonDTO childAlertReply(String address) throws ChildAlertResponseException {
        logger.info("Fetching child alert reply for address: {}", address);
        try {
            List<Object> necessaryData = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                    .filter(listconcat ->
                            listconcat.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).anyMatch(person -> person.getAddress().equals(address))
                    )
                    .flatMap(List::stream)
                    .filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast)
                    .map(subDto -> {
                                SubChildAlertReplyChildren replyChildren = LocalDate.parse(subDto.getBirthdate(), dateFormatter).until(currentDate).getYears() < 18
                                        ? new SubChildAlertReplyChildren(subDto.getFirstName(), subDto.getLastName(), subDto.getBirthdate()) : null;
                                SubChildAlertReplyAdultFamily replyAdultFamily = LocalDate.parse(subDto.getBirthdate(), dateFormatter).until(currentDate).getYears() > 18
                                        ? new SubChildAlertReplyAdultFamily(subDto.getFirstName(), subDto.getLastName()) : null;
                                logger.debug("Processed medical record: {}, is child: {}", subDto, replyChildren != null);
                                if (replyChildren != null) {
                                    return replyChildren;
                                } else
                                    return replyAdultFamily;
                            }
                    ).toList();

            List<SubChildAlertReplyChildren> listChild = necessaryData.stream().filter(SubChildAlertReplyChildren.class::isInstance).map(SubChildAlertReplyChildren.class::cast).toList();
            List<SubChildAlertReplyAdultFamily> listAdult = necessaryData.stream().filter(SubChildAlertReplyAdultFamily.class::isInstance).map(SubChildAlertReplyAdultFamily.class::cast).toList();

            logger.debug("Children list: {}, Adults list: {}", listChild, listAdult);
            logger.info("Child alert reply fetched successfully.");
            return new ChildAlertReplyPersonDTO(listChild, listAdult);
        } catch (Exception e) {
            logger.error("Error fetching child alert reply: ", e);
            throw new PersonCustomException.ChildAlertResponseException(e);
        }
    }

    /**
     * Method to get phone numbers based on station number.
     *
     * @param stationNumber the station number to query
     * @return PhoneAlertReplyPersonDTO containing the phone numbers
     * @throws PhoneAlertResponseException if an error occurs during the process
     */
    public PhoneAlertReplyPersonDTO phoneAlert(String stationNumber) throws PhoneAlertResponseException {
        logger.info("Fetching phone alert reply for station number: {}", stationNumber);
        try {
            List<String> listPhone = Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.fireStationReaderJsonData()))
                    .flatMap(List::stream).collect(Collectors.groupingBy(model -> {
                                if (model instanceof PersonModel personModel) {
                                    return personModel.getAddress();
                                } else {
                                    FireStationModel fireStationModel = ((FireStationModel) model);
                                    return fireStationModel.getAddress();
                                }
                            }
                    ))
                    .values().stream()
                    .filter(listConcat -> listConcat.stream().anyMatch(fireStation -> fireStation instanceof FireStationModel fireStationModel && fireStationModel.getStation().equals(stationNumber)))
                    .flatMap(phone -> phone.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).map(PersonModel::getPhone))
                    .toList();

            logger.debug("Phone list: {}", listPhone);
            logger.info("Phone alert reply fetched successfully.");
            return new PhoneAlertReplyPersonDTO(listPhone);
        } catch (Exception e) {
            logger.error("Error fetching phone alert reply: ", e);
            throw new PersonCustomException.PhoneAlertResponseException(e);
        }
    }

    /**
     * Method to get details of persons and the fire station linked to their address.
     *
     * @param address the address to query
     * @return FireReplyPersonDTO containing the information
     * @throws FireResponseException if an error occurs during the process
     */
    public FireReplyPersonDTO fire(String address) throws FireResponseException {
        logger.info("Fetching fire reply for address: {}", address);
        try {
            List<SubFireReplyReplyInfoPerson> subFireReplyReplyInfoPersonList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                    .filter(listConcat ->
                            listConcat.stream().anyMatch(model -> model instanceof PersonModel personModel
                                    && personModel.getAddress().equals(address)))
                    .map(subDto -> {
                        PersonModel personModel = subDto.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).findAny().orElseThrow();
                        MedicalRecordModel medicalRecordModel = subDto.stream().filter(model -> model instanceof MedicalRecordModel).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                        logger.debug("Processed person: {}, medical record: {}", personModel, medicalRecordModel);
                        return new SubFireReplyReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                    })
                    .toList();

            String stationNumber = manageJsonData.fireStationReaderJsonData().stream()
                    .filter(model -> model.getAddress().equals(address)).map(FireStationModel::getStation).findAny().orElse(null);

            logger.debug("Station number for address {}: {}", address, stationNumber);
            logger.info("Fire reply fetched successfully.");
            return new FireReplyPersonDTO(subFireReplyReplyInfoPersonList, stationNumber);
        } catch (Exception e) {
            logger.error("Error fetching fire reply: ", e);
            throw new PersonCustomException.FireResponseException(e);
        }
    }

    /**
     * Method to get details of persons grouped by address, based on station numbers.
     *
     * @param listStationNumber the list of station numbers to query
     * @return StationsReplyPersonDTO containing the information
     * @throws FloodStationResponseException if an error occurs during the process
     */
    public StationsReplyPersonDTO floodStation(List<String> listStationNumber) throws FloodStationResponseException {
        logger.info("Fetching flood station reply for station numbers: {}", listStationNumber);
        try {
            List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();
            logger.debug("Existing fire stations: {}", fireStationModelList);

            List<SubStationsReplyInfoPersonByAddress> subStationsReplyInfoPersonByAddressList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                    .collect(Collectors.groupingBy(
                            group -> {
                                for (Object model : group) {
                                    if (model instanceof PersonModel personModel) {
                                        return personModel.getAddress();
                                    }
                                }
                                return "UnknownAddress";
                            }
                    ))
                    .values().stream()
                    .filter(groupOfGroups -> groupOfGroups.stream()
                            .anyMatch(group -> group.stream()
                                    .anyMatch(model -> model instanceof PersonModel personModel
                                            && listStationNumber.stream().anyMatch(stationNumber -> fireStationModelList.stream().anyMatch(stationAddress -> stationAddress.getStation().equals(stationNumber)
                                            && personModel.getAddress().equals(stationAddress.getAddress())
                                    )))
                            ))
                    .map(groupOfGroups -> {
                        List<SubStationsReplyInfoPerson> liste = new ArrayList<>();
                        SubStationsReplyInfoAddress infoAddress = null;

                        for (List<?> groupOfSameProfil : groupOfGroups) {
                            PersonModel personModel = groupOfSameProfil.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();
                            MedicalRecordModel medicalRecordModel = groupOfSameProfil.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                            SubStationsReplyInfoPerson infoPerson = new SubStationsReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                            infoAddress = new SubStationsReplyInfoAddress(personModel.getAddress(), personModel.getCity(), personModel.getZip());
                            liste.add(infoPerson);
                            logger.debug("Processed person: {}, medical record: {}", personModel, medicalRecordModel);
                        }

                        logger.debug("Processed address: {}", infoAddress);
                        return new SubStationsReplyInfoPersonByAddress(infoAddress, liste);
                    })
                    .toList();

            logger.info("Flood station reply fetched successfully.");
            return new StationsReplyPersonDTO(subStationsReplyInfoPersonByAddressList);
        } catch (Exception e) {
            logger.error("Error fetching flood station reply: ", e);
            throw new FloodStationResponseException(e);
        }
    }

    /**
     * Method to get detailed information of persons based on last name.
     *
     * @param lastName the last name to query
     * @return PersonInfoReplyPersonDTO containing the information
     * @throws PersonInfoResponseException if an error occurs during the process
     */
    public PersonInfoReplyPersonDTO personInfo(String lastName) throws PersonInfoResponseException {
        logger.info("Fetching person info for {}", lastName);
        try {
            List<SubPersonInfoReplyPerson> subPersonInfoReplyPersonList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                    .filter(group -> group.stream().anyMatch(model -> model instanceof PersonModel personModel && personModel.getLastName().equals(lastName)))
                    .map(subDto -> {
                        PersonModel personModel = subDto.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).findAny().orElseThrow();
                        MedicalRecordModel medicalRecordModel = subDto.stream().filter(model -> model instanceof MedicalRecordModel).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                        logger.debug("Processed person: {}, medical record: {}", personModel, medicalRecordModel);
                        return new SubPersonInfoReplyPerson(personModel.getLastName(), personModel.getAddress(), personModel.getCity(), personModel.getZip(), personModel.getEmail(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                    })
                    .toList();

            logger.debug("Person info list: {}", subPersonInfoReplyPersonList);
            logger.info("Person info fetched successfully.");
            return new PersonInfoReplyPersonDTO(subPersonInfoReplyPersonList);
        } catch (Exception e) {
            logger.error("Error fetching person info: ", e);
            throw new PersonCustomException.PersonInfoResponseException(e);
        }
    }

    /**
     * Method to get email addresses of persons living in a specific city.
     *
     * @param city the city to query
     * @return CommunityEmailReplyPersonDTO containing the email addresses
     * @throws CommunityEmailException if an error occurs during the process
     */
    public CommunityEmailReplyPersonDTO communityEmail(String city) throws CommunityEmailException {
        logger.info("Fetching community email for city: {}", city);
        try {
            List<String> listEmail = manageJsonData.personReaderJsonData().stream()
                    .filter(filteringCity -> filteringCity.getCity().equals(city))
                    .map(PersonModel::getEmail)
                    .toList();

            logger.debug("Email list: {}", listEmail);
            logger.info("Community email fetched successfully.");
            return new CommunityEmailReplyPersonDTO(listEmail);
        } catch (Exception e) {
            logger.error("Error fetching community email: ", e);
            throw new CommunityEmailException(e);
        }
    }

    /**
     * Private method to combine and group MedicalRecord and Person with the same profile into one group.
     *
     * @return Stream of grouped lists
     * @throws FactoringConcatStreamMethodException if an error occurs during the process
     */
    private Stream<? extends List<?>> factoringConcatStreamForGroupingPersonAndMedicalSameProfile() throws FactoringConcatStreamMethodException {
        logger.debug("Combining and grouping person and medical record data");
        try {
            return Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                    .flatMap(List::stream)
                    .collect(Collectors.groupingBy(
                            model -> {
                                if (model instanceof PersonModel personModel) {
                                    return personModel.getFirstName() + personModel.getLastName();
                                }
                                MedicalRecordModel medicalRecordModel = ((MedicalRecordModel) model);
                                return medicalRecordModel.getFirstName() + medicalRecordModel.getLastName();
                            }
                    ))
                    .values()
                    .stream();
        } catch (Exception e) {
            logger.error("Error combining and grouping data: ", e);
            throw new PersonCustomException.FactoringConcatStreamMethodException(e);
        }
    }

    /**
     * Method to add a new person.
     *
     * @param person the person to add
     * @throws AddPersonException if an error occurs during the addition
     */
    public void addPerson(AddPersonDto person) throws AddPersonException {
        logger.info("Adding new person: {} {}", person.getFirstName(), person.getLastName());
        try {
            List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
            logger.debug("Existing persons: {}", listPersonsExisting);

            if (listPersonsExisting.stream().noneMatch(personExist -> personExist.getLastName().equals(person.getLastName()) && personExist.getFirstName().equals(person.getFirstName()))) {
                listPersonsExisting.add(new PersonModel(person.getFirstName(), person.getLastName(), person.getAddress(), person.getCity(), person.getZip(), person.getPhone(), person.getEmail()));
                logger.info("New person added successfully.");
            } else {
                throw new AlreadyExistPersonException();
            }

            manageJsonData.personWriterJsonData(listPersonsExisting);
        } catch (Exception e) {
            logger.error("Error adding person: ", e);
            throw new AddPersonException(e);
        }
    }

    /**
     * Method to update an existing person.
     *
     * @param updatePerson the person to update
     * @throws UpdatePersonException if an error occurs during the update
     */
    public void updatePerson(UpdatePersonDto updatePerson) throws UpdatePersonException {
        logger.info("Updating person: {} {}", updatePerson.getFirstName(), updatePerson.getLastName());
        try {
            List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
            logger.debug("Existing persons: {}", listPersonsExisting);

            Optional<PersonModel> wantedPersonUpdate = listPersonsExisting.stream().filter(person -> person.getFirstName().equals(updatePerson.getFirstName()) && person.getLastName().equals(updatePerson.getLastName())).findFirst();
            if (wantedPersonUpdate.isEmpty()) {
                throw new NotFoundPersonException();
            }

            wantedPersonUpdate.ifPresent(modifyPerson -> {
                modifyPerson.setCity(updatePerson.getCity());
                modifyPerson.setPhone(updatePerson.getPhone());
                modifyPerson.setEmail(updatePerson.getEmail());
                modifyPerson.setAddress(updatePerson.getAddress());
                modifyPerson.setZip(updatePerson.getZip());
                logger.debug("Updated person details: {}", modifyPerson);
            });

            manageJsonData.personWriterJsonData(listPersonsExisting);
            logger.info("Person updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating person: ", e);
            throw new UpdatePersonException(e);
        }
    }

    /**
     * Method to delete an existing person.
     *
     * @param deletePerson the person to delete
     * @throws DeletePersonException if an error occurs during the deletion
     */
    public void deletePerson(DeletePersonDto deletePerson) throws DeletePersonException {
        logger.info("Deleting person: {} {}", deletePerson.getFirstName(), deletePerson.getLastName());
        try {
            List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
            logger.debug("Existing persons: {}", listPersonsExisting);

            if (listPersonsExisting.removeIf(person -> person.getFirstName().equals(deletePerson.getFirstName()) && person.getLastName().equals(deletePerson.getLastName()))) {
                manageJsonData.personWriterJsonData(listPersonsExisting);
                logger.info("Person deleted successfully.");
            } else {
                throw new NotFoundPersonException();
            }
        } catch (Exception e) {
            logger.error("Error deleting person: ", e);
            throw new DeletePersonException(e);
        }
    }
}
