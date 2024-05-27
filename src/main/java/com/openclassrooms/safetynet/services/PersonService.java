package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdatePersonDto;
import com.openclassrooms.safetynet.dto.requests.DeletePersonDto;
import com.openclassrooms.safetynet.dto.responses.ChildAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireStationReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.PhoneAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.StationsReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.PersonInfoReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.CommunityEmailReplyPersonDTO;

import com.openclassrooms.safetynet.dto.responses.submodels.*;
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

import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;

@Service
public class PersonService {
    ManageJsonData manageJsonData;
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Constructor to inject ManageJsonData dependency
    public PersonService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    // Method to get persons and count of minor and major reply based on station number
    public FireStationReplyPersonDTO fireStationReply(String stationNumber) {

        // Read the list of fire stations from JSON data
        List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();

        // Combine person and medical record data into a single stream, group by full name (first + last name)
        List<Map<Boolean, SubFireStationReplyPerson>> necessaryData = Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                .flatMap(List::stream).collect(Collectors.groupingBy(model -> {
                            if (model instanceof PersonModel personModel) {
                                return personModel.getFirstName() + personModel.getLastName(); // Group by person's full name
                            } else {
                                MedicalRecordModel medicalRecordModel = ((MedicalRecordModel) model);
                                return medicalRecordModel.getFirstName() + medicalRecordModel.getLastName(); // Group by medical record's full name
                            }
                        }
                ))
                .values()
                .stream()
                // Filter groups to include only those associated with the given fire station number
                .filter(listconcat ->
                        listconcat.stream().anyMatch(person -> person instanceof PersonModel personModel && fireStationModelList.stream().filter(model -> model.getStation().equals(stationNumber)).toList().toString().contains(personModel.getAddress()))
                )
                // Map each group to a map containing whether the person is a minor and their details
                .map(subDto -> {
                    PersonModel personModel = subDto.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();
                    Boolean countMinorAdult = subDto.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).anyMatch(age -> LocalDate.parse(age.getBirthdate(), dateFormatter).until(currentDate).getYears() < 18);
                    SubFireStationReplyPerson subFireStationReplyPerson = new SubFireStationReplyPerson(personModel.getFirstName(), personModel.getLastName(), personModel.getAddress(), personModel.getCity(), personModel.getZip(), personModel.getPhone());
                    Map<Boolean, SubFireStationReplyPerson> booleanMap = new HashMap<>();
                    booleanMap.put(countMinorAdult, subFireStationReplyPerson); // Map true if minor, false if adult
                    return booleanMap;
                }).toList();

        // Flatten the list of maps to get a list of SubFireStationReplyPerson objects
        List<SubFireStationReplyPerson> reply = necessaryData.stream().flatMap(model -> model.values().stream()).toList();

        // Count the number of minors and adults
        long minor = necessaryData.stream().filter(booleanMinor -> booleanMinor.containsKey(true)).count();
        long adult = necessaryData.stream().filter(booleanAdult -> booleanAdult.containsKey(false)).count();

        // Create a response object containing the counts and the list of persons
        SubFireStationModelReplyForCount subFireStationModelReplyForCount = new SubFireStationModelReplyForCount(String.valueOf(adult), String.valueOf(minor));
        return new FireStationReplyPersonDTO(reply, subFireStationModelReplyForCount);
    }

    // Method to get detail's children and their tutor's FullName reply based on station number
    public ChildAlertReplyPersonDTO childAlertReply(String address) {

        // Combine person and medical record data into a single stream, group by full name (first + last name)
        List<Object> necessaryData = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                // Filter groups to include only those associated with the given address
                .filter(listconcat ->
                        listconcat.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).anyMatch(person -> person.getAddress().equals(address))
                )
                // Flatten the list of lists into a single stream
                .flatMap(List::stream)
                // Filter to include only medical records
                .filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast)
                // Map each medical record to either a child or adult response object based on age
                .map(subDto -> {
                            SubChildAlertReplyChildren replyChildren = LocalDate.parse(subDto.getBirthdate(), dateFormatter).until(currentDate).getYears() < 18
                                    ? new SubChildAlertReplyChildren(subDto.getFirstName(), subDto.getLastName(), subDto.getBirthdate()) : null;
                            SubChildAlertReplyAdultFamily replyAdultFamily = LocalDate.parse(subDto.getBirthdate(), dateFormatter).until(currentDate).getYears() > 18
                                    ? new SubChildAlertReplyAdultFamily(subDto.getFirstName(), subDto.getLastName()) : null;
                            if (replyChildren != null) {
                                return replyChildren; // Return child response if age is less than 18
                            } else
                                return replyAdultFamily; // Return adult response if age is greater than 18
                        }
                ).toList();

        // Separate the mapped responses into lists of children and adults
        List<SubChildAlertReplyChildren> listChild = necessaryData.stream().filter(SubChildAlertReplyChildren.class::isInstance).map(SubChildAlertReplyChildren.class::cast).toList();
        List<SubChildAlertReplyAdultFamily> listAdult = necessaryData.stream().filter(SubChildAlertReplyAdultFamily.class::isInstance).map(SubChildAlertReplyAdultFamily.class::cast).toList();

        // Create and return the final response object containing lists of children and adults
        return new ChildAlertReplyPersonDTO(listChild, listAdult);
    }

    // Method to get detail's children and their tutor's FullName reply based on station number
    public PhoneAlertReplyPersonDTO phoneAlert(String stationNumber) {
        // Combine person and fire station data into a single stream, group by address
        List<String> listPhone = Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.fireStationReaderJsonData()))
                .flatMap(List::stream).collect(Collectors.groupingBy(model -> {
                            if (model instanceof PersonModel personModel) {
                                return personModel.getAddress(); // Group by person's address
                            } else {
                                FireStationModel fireStationModel = ((FireStationModel) model);
                                return fireStationModel.getAddress(); // Group by fire station's address
                            }
                        }
                ))
                .values().stream()
                // Filter groups to include only those associated with the given fire station number
                .filter(listConcat -> listConcat.stream().anyMatch(fireStation -> fireStation instanceof FireStationModel fireStationModel && fireStationModel.getStation().equals(stationNumber)))
                // Flatten the list of lists into a single stream, filter to include only persons, and map to their phone numbers
                .flatMap(phone -> phone.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).map(PersonModel::getPhone))
                .toList();

        // Create and return the final response object containing the list of phone numbers
        return new PhoneAlertReplyPersonDTO(listPhone);
    }

    // Method to get detail's family who's living there and the station linked reply based on address person
    public FireReplyPersonDTO fire(String address) {

        // Combine person and medical record data into a single stream, group by full name (first + last name)
        List<SubFireReplyReplyInfoPerson> subFireReplyReplyInfoPersonList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                // Filter groups to include only those associated with the given address
                .filter(listConcat ->
                        listConcat.stream().anyMatch(model -> model instanceof PersonModel personModel
                                && personModel.getAddress().equals(address)))
                // Map each group to a SubFireReplyReplyInfoPerson object containing relevant details
                .map(subDto -> {
                    PersonModel personModel = subDto.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).findAny().orElseThrow();
                    MedicalRecordModel medicalRecordModel = subDto.stream().filter(model -> model instanceof MedicalRecordModel).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                    return new SubFireReplyReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                })
                .toList();

        // Find the station number associated with the given address
        String stationNumber = manageJsonData.fireStationReaderJsonData().stream()
                .filter(model -> model.getAddress().equals(address)).map(FireStationModel::getStation).findAny().orElse(null);

        // Create and return the final response object containing the list of persons and the station number
        return new FireReplyPersonDTO(subFireReplyReplyInfoPersonList, stationNumber);
    }

    // Method to get detail's person living and linked at the numbers station persons must be grouped by address, reply based on station number
    public StationsReplyPersonDTO floodStation(List<String> listStationNumber) {

        // Read the list of fire stations from JSON data
        List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();

        // Combine person and medical record data into a single stream, group by address
        List<SubStationsReplyInfoPersonByAddress> subStationsReplyInfoPersonByAddressList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
        //Combine each group of (person and MedicalRecord by full name) by common address between its different groups into new group
                .collect(Collectors.groupingBy(
                        group -> {
                            for (Object model : group) {
                                if (model instanceof PersonModel personModel) {
                                    return personModel.getAddress(); // Group by person's address
                                }
                            }
                            return "UnknownAddress"; // If no PersonModel is found
                        }
                ))
                .values().stream()
                // Filter groups to include only those associated with the address of the fireStation which I obtain  with the given list of station numbers
                .filter(groupOfGroups -> groupOfGroups.stream()
                        .anyMatch(group -> group.stream()
                                .anyMatch(model -> model instanceof PersonModel personModel
                                        && listStationNumber.stream().anyMatch(stationNumber -> fireStationModelList.stream().anyMatch(stationAddress -> stationAddress.getStation().equals(stationNumber)
                                        && personModel.getAddress().equals(stationAddress.getAddress())
                                )))
                        ))
                // Map each groups Of group to a SubStationsReplyInfoPersonByAddress object containing relevant details
                .map(groupOfGroups -> {
                    // Initialize a list to hold SubStationsReplyInfoPerson objects
                    List<SubStationsReplyInfoPerson> liste = new ArrayList<>();

                    // Initialize a variable to hold address information
                    SubStationsReplyInfoAddress infoAddress = null;

                    // Iterate over each group in the groupOfGroups
                    for (List<?> groupOfSameProfil : groupOfGroups) {
                        // Find any PersonModel object in the group, or throw an exception if not found
                        PersonModel personModel = groupOfSameProfil.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();

                        // Find any MedicalRecordModel object in the group, or throw an exception if not found
                        MedicalRecordModel medicalRecordModel = groupOfSameProfil.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).findAny().orElseThrow();

                        // Create a SubStationsReplyInfoPerson object with the person's details and medical record information
                        SubStationsReplyInfoPerson infoPerson = new SubStationsReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());

                        // Create a SubStationsReplyInfoAddress object with the person's address information
                        infoAddress = new SubStationsReplyInfoAddress(personModel.getAddress(), personModel.getCity(), personModel.getZip());

                        // Add the SubStationsReplyInfoPerson object to the list
                        liste.add(infoPerson);
                    }

                    // Return a new SubStationsReplyInfoPersonByAddress object containing the address information and the list of persons
                    return new SubStationsReplyInfoPersonByAddress(infoAddress, liste);
                })
                .toList();

        // Create and return the final response object containing the list of persons grouped by address
        return new StationsReplyPersonDTO(subStationsReplyInfoPersonByAddressList);
    }
    // Method to get detail's persons reply based on station firstName and LastName
    public PersonInfoReplyPersonDTO personInfo(String firstName, String lastName) {

        // Combine person and medical record data into a single stream, group by full name (first + last name)
        List<SubPersonInfoReplyPerson> subPersonInfoReplyPersonList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                // Filter groups to include only those matching the given first name and last name
                .filter(group -> group.stream().anyMatch(model -> model instanceof PersonModel personModel && personModel.getFirstName().equals(firstName) && personModel.getLastName().equals(lastName)))
                // Map each group to a SubPersonInfoReplyPerson object containing relevant details
                .map(subDto -> {
                    // Find any PersonModel object in the group, or throw an exception if not found
                    PersonModel personModel = subDto.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).findAny().orElseThrow();

                    // Find any MedicalRecordModel object in the group, or throw an exception if not found
                    MedicalRecordModel medicalRecordModel = subDto.stream().filter(model -> model instanceof MedicalRecordModel).map(MedicalRecordModel.class::cast).findAny().orElseThrow();

                    // Create and return a SubPersonInfoReplyPerson object with the person's details and medical record information
                    return new SubPersonInfoReplyPerson(personModel.getLastName(), personModel.getAddress(), personModel.getCity(), personModel.getZip(), personModel.getEmail(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                })
                .toList();

        // Create and return the final response object containing the list of persons
        return new PersonInfoReplyPersonDTO(subPersonInfoReplyPersonList);
    }
    // Method to get mail's persons living in the city reply based on city
    public CommunityEmailReplyPersonDTO communityEmail(String city) {
        // Read the list of persons from JSON data and filter by the given city
        List<String> listEmail = manageJsonData.personReaderJsonData().stream()
                .filter(filteringCity -> filteringCity.getCity().equals(city)) // Filter persons by city
                .map(PersonModel::getEmail) // Map each person to their email address
                .toList(); // Collect the email addresses into a list
        // Create and return the final response object containing the list of email addresses
        return new CommunityEmailReplyPersonDTO(listEmail);
    }

    // Private method same code in this class in order to combine and group Medical record and Person with the same profil in one group
    private Stream<? extends List<?>> factoringConcatStreamForGroupingPersonAndMedicalSameProfile() {
        // Combine person and medical record data into a single stream
        return Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                // Flatten the list of lists into a single stream
                .flatMap(List::stream)
                // Group the combined data by full name (first + last name)
                .collect(Collectors.groupingBy(
                        model -> {
                            if (model instanceof PersonModel personModel) {
                                // If the model is a PersonModel, group by person's full name
                                return personModel.getFirstName() + personModel.getLastName();
                            }
                            // If the model is a MedicalRecordModel, group by medical record's full name
                            MedicalRecordModel medicalRecordModel = ((MedicalRecordModel) model);
                            return medicalRecordModel.getFirstName() + medicalRecordModel.getLastName();
                        }
                ))
                // Get the values (groups) from the map and convert them to a stream
                .values()
                .stream();
    }

    // Method to add a new person
    public void addPerson(AddOrUpdatePersonDto person) throws PersonWriterException {
        List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
        // Check if the person already exists
        if (listPersonsExisting.stream().noneMatch(personExist -> personExist.getLastName().equals(person.getLastName()) && personExist.getFirstName().equals(person.getFirstName()))) {
            // Add the new person if it does not exist
            listPersonsExisting.add(new PersonModel(person.getFirstName(), person.getLastName(), person.getAddress(), person.getCity(), person.getZip(), person.getPhone(), person.getEmail()));
        }
        // Write the updated list of medical records back to the JSON file
        manageJsonData.personWriterJsonData(listPersonsExisting);
    }
    // Method to update an existing person
    public void updatePerson(AddOrUpdatePersonDto updatePerson) throws PersonWriterException {
        List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
        // Get the reference to the filtered person object
        Optional<PersonModel> wantedPersonUpdate = listPersonsExisting.stream().filter(person -> person.getFirstName().equals(updatePerson.getFirstName()) && person.getLastName().equals(updatePerson.getLastName())).findFirst();
        // If there is a reference, access the object and modify its properties
        wantedPersonUpdate.ifPresent(modifyPerson -> {
            modifyPerson.setCity(updatePerson.getCity());
            modifyPerson.setPhone(updatePerson.getPhone());
            modifyPerson.setEmail(updatePerson.getEmail());
            modifyPerson.setAddress(updatePerson.getAddress());
            modifyPerson.setZip(updatePerson.getZip());
        });
        // Write the updated list of person back to the JSON file
        manageJsonData.personWriterJsonData(listPersonsExisting);
    }

    // Method to delete an existing person
    public void deletePerson(DeletePersonDto deletePerson) throws PersonWriterException {
        List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
        // Remove the person if it matches the given first name and last name
        if(listPersonsExisting.removeIf(person -> person.getFirstName().equals(deletePerson.getFirstName()) && person.getLastName().equals(deletePerson.getLastName()))){
            // Write the updated list of medical records back to the JSON file
            manageJsonData.personWriterJsonData(listPersonsExisting);
        } //TODO:: add exception.
    }
}