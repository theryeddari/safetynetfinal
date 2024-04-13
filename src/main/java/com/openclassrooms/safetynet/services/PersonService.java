package com.openclassrooms.safetynet.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.dto.responses.ChildAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireStationReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.PhoneAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.submodels.*;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonService {
    ObjectMapper objectMapper = new ObjectMapper();
    ManageJsonData manageJsonData = new ManageJsonData();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public FireStationReplyPersonDTO fireStationReply(String stationNumber) throws IOException {

        List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();
        List<Map<Boolean, SubFireStationReplyPerson>> necessaryData = Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(model -> {
                            if (model instanceof PersonModel personModel) {
                                return personModel.getFirstName() + personModel.getLastName();
                            } else {
                                MedicalRecordModel medicalRecordModel = ((MedicalRecordModel) model);
                                return medicalRecordModel.getFirstName() + medicalRecordModel.getLastName();
                            }
                        }
                ))
                .values()
                .stream()
                .filter(listconcat ->
                        listconcat.stream().anyMatch(person -> person instanceof PersonModel personModel && fireStationModelList.stream().filter(model -> model.getStation().equals(stationNumber)).toList().toString().contains(personModel.getAddress()))
                )
                .map(subDto -> {
                    PersonModel personModel = subDto.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();
                    Boolean countMinorAdult = subDto.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).anyMatch(age -> LocalDate.parse(age.getBirthdate(), dateFormatter).until(currentDate).getYears() < 18);
                    SubFireStationReplyPerson subFireStationReplyPerson = new SubFireStationReplyPerson(personModel.getFirstName(), personModel.getLastName(), personModel.getAddress(), personModel.getCity(), personModel.getZip(), personModel.getPhone());
                    Map<Boolean, SubFireStationReplyPerson> map = new HashMap<>();
                    map.put(countMinorAdult, subFireStationReplyPerson);
                    System.out.println(map);
                    return map;
                }).toList();

        List<SubFireStationReplyPerson> reply = necessaryData.stream().flatMap(model -> model.values().stream()).toList();
        long minor = necessaryData.stream().filter(booleanMinor -> booleanMinor.containsKey(true)).count();
        long adult = necessaryData.stream().filter(booleanAdult -> booleanAdult.containsKey(false)).count();
        SubFireStationModelReplyForCount subFireStationModelReplyForCount = new SubFireStationModelReplyForCount(String.valueOf(adult), String.valueOf(minor));
        return new FireStationReplyPersonDTO(reply, subFireStationModelReplyForCount);
    }

    public ChildAlertReplyPersonDTO childAlertReply(String address) throws IOException {
        //convert Collection persons  to flux,filter about  address and create list of persons.
        List<PersonModel> listPersonModelFiltered = manageJsonData.personReaderJsonData()
                .stream()
                .filter(listPerson -> listPerson.getAddress().equals(address)).toList();

        //convert Collection medicalrecords to flux, filter about birthdate in order to two list : adult and minor
        List<MedicalRecordModel> listChildFiltered = manageJsonData.medicalRecordReaderJsonData()
                .stream()
                .filter(medicalRecordModel -> listPersonModelFiltered.stream().anyMatch(listPerson -> medicalRecordModel.getFirstName().equals(listPerson.getFirstName())
                        && medicalRecordModel.getLastName().equals(listPerson.getLastName())
                        && (LocalDate.parse(medicalRecordModel.getBirthdate(), dateFormatter).until(currentDate).getYears() < 18)
                )).toList();
        List<MedicalRecordModel> listAdultFiltered = manageJsonData.medicalRecordReaderJsonData()
                .stream()
                .filter(medicalRecordModel -> listPersonModelFiltered.stream().anyMatch(listPerson -> medicalRecordModel.getFirstName().equals(listPerson.getFirstName())
                        && medicalRecordModel.getLastName().equals(listPerson.getLastName())
                        && (LocalDate.parse(medicalRecordModel.getBirthdate(), dateFormatter).until(currentDate).getYears() > 18)
                )).toList();

        //complete List<SubChildAlertReplyAdultFamily> to List<PersonModel> with autocompletion
        TypeReference<List<SubChildAlertReplyAdultFamily>> typeReferenceSubChildAlertReplyAdultFamily = new TypeReference<>() {
        };
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<SubChildAlertReplyAdultFamily> listSubChildAlertAdultFamily = objectMapper.convertValue(listAdultFiltered, typeReferenceSubChildAlertReplyAdultFamily);

        //complete List<SubChildAlertReplyAdultFamily> to List<PersonModel> with autocompletion
        TypeReference<List<SubChildAlertReplyChildren>> typeReferenceSubChildAlertReplyChildren = new TypeReference<>() {
        };
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<SubChildAlertReplyChildren> listSubChildAlertChildren = objectMapper.convertValue(listChildFiltered, typeReferenceSubChildAlertReplyChildren);

        // initialize DTO reply
        return new ChildAlertReplyPersonDTO(listSubChildAlertChildren, listSubChildAlertAdultFamily);
    }

    public PhoneAlertReplyPersonDTO phoneAlert(String stationNumber) throws IOException {
        //convert Collection firestations to flux, filter about stationNumber et create list of address
        List<String> listAddressFireStationModelFiltered = manageJsonData.fireStationReaderJsonData()
                .stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStationModel::getAddress)
                .toList();

        //convert Collection persons  to flux,filter about  firestation address and create list of persons.
        List<String> listPhonePersonFiltered = manageJsonData.personReaderJsonData()
                .stream()
                .filter(personModel -> listAddressFireStationModelFiltered.contains(personModel.getAddress())).map(PersonModel::getPhone).toList();

        System.out.println(listPhonePersonFiltered);
        return new PhoneAlertReplyPersonDTO(listPhonePersonFiltered);
    }

    public FireReplyPersonDTO fire(String address) throws IOException {

        List<SubFireReplyReplyInfoPerson> tests = Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(model -> {
                    if (model instanceof PersonModel personModel) {
                        return personModel.getFirstName() + personModel.getLastName();
                    } else {
                        MedicalRecordModel medicalRecordModel = ((MedicalRecordModel) model);
                        return medicalRecordModel.getFirstName() + medicalRecordModel.getLastName();
                    }
                }))
                .values()
                .stream()
                .filter(listconcat ->
                        listconcat.stream().anyMatch(model -> model instanceof PersonModel personModel
                                && personModel.getAddress().equals(address)))
                .map(subDto -> {
                    PersonModel personModel = subDto.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();
                    MedicalRecordModel medicalRecordModel = subDto.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                    return new SubFireReplyReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                })
                .toList();
        String stationNumber = manageJsonData.fireStationReaderJsonData().stream()
                .filter(model -> model.getAddress().equals(address)).map(FireStationModel::getStation).findAny().orElse(null);
        return new FireReplyPersonDTO(tests, stationNumber);
    }
}