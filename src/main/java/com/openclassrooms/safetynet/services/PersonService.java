package com.openclassrooms.safetynet.services;

//import com.openclassrooms.safetynet.dto.requests.AddPersonDto;
import com.openclassrooms.safetynet.dto.requests.AddPersonDto;
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
    ManageJsonData manageJsonData;
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public PersonService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;

        // pour faire remonter l'exception de manageJsonData car dans son constructor nous lancer une  méthode qui peut lever une exeption.
    }

    public FireStationReplyPersonDTO fireStationReply(String stationNumber) {

        List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();
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
                    return map;
                }).toList();

        List<SubFireStationReplyPerson> reply = necessaryData.stream().flatMap(model -> model.values().stream()).toList();
        long minor = necessaryData.stream().filter(booleanMinor -> booleanMinor.containsKey(true)).count();
        long adult = necessaryData.stream().filter(booleanAdult -> booleanAdult.containsKey(false)).count();
        SubFireStationModelReplyForCount subFireStationModelReplyForCount = new SubFireStationModelReplyForCount(String.valueOf(adult), String.valueOf(minor));
        return new FireStationReplyPersonDTO(reply, subFireStationModelReplyForCount);
    }

    public ChildAlertReplyPersonDTO childAlertReply(String address) {

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
                    if (replyChildren != null) {
                        return replyChildren;
                    } else
                        return replyAdultFamily;
                        }
                ).toList();
        List<SubChildAlertReplyChildren> listChild = necessaryData.stream().filter(SubChildAlertReplyChildren.class::isInstance).map(SubChildAlertReplyChildren.class::cast).toList();
        List<SubChildAlertReplyAdultFamily> listAdult = necessaryData.stream().filter(SubChildAlertReplyAdultFamily.class::isInstance).map(SubChildAlertReplyAdultFamily.class::cast).toList();
        return new ChildAlertReplyPersonDTO(listChild, listAdult);
    }

    public PhoneAlertReplyPersonDTO phoneAlert(String stationNumber) {
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

        return new PhoneAlertReplyPersonDTO(listPhone);
    }

    public FireReplyPersonDTO fire(String address) {

        List<SubFireReplyReplyInfoPerson> subFireReplyReplyInfoPersonList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                .filter(listConcat ->
                        listConcat.stream().anyMatch(model -> model instanceof PersonModel personModel
                                && personModel.getAddress().equals(address)))
                .map(subDto -> {
                    PersonModel personModel = subDto.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).findAny().orElseThrow();
                    MedicalRecordModel medicalRecordModel = subDto.stream().filter(model -> model instanceof MedicalRecordModel).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                    return new SubFireReplyReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                })
                .toList();
        String stationNumber = manageJsonData.fireStationReaderJsonData().stream()
                .filter(model -> model.getAddress().equals(address)).map(FireStationModel::getStation).findAny().orElse(null);
        return new FireReplyPersonDTO(subFireReplyReplyInfoPersonList, stationNumber);
    }

    public StationsReplyPersonDTO floodStation(List<String> listStationNumber) {

        List<FireStationModel> fireStationModelList = manageJsonData.fireStationReaderJsonData();
        List<SubStationsReplyInfoPersonByAddress> subStationsReplyInfoPersonByAddressList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                .collect(Collectors.groupingBy(
                        group -> {
                            for (Object model : group) {
                                if (model instanceof PersonModel personModel) {
                                    return personModel.getAddress();
                                }
                            }
                            return "UnknownAddress"; //Si aucun PersonModel n'est trouvé
                        }
                ))
                .values().stream()
                .filter(group -> group.stream()
                        .anyMatch(list -> list.stream()
                                .anyMatch(model -> model instanceof PersonModel personModel
                                        && listStationNumber.stream().anyMatch(stationNumber -> fireStationModelList.stream().anyMatch(stationAddress -> stationAddress.getStation().equals(stationNumber)
                                        && personModel.getAddress().equals(stationAddress.getAddress())
                                )))
                        ))
                .map(group -> {
                    List<SubStationsReplyInfoPerson> liste = new ArrayList<>();
                    SubStationsReplyInfoAddress infoAddress = null;
                    for (List<?> list : group) {
                        PersonModel personModel = list.stream().filter(PersonModel.class::isInstance).map(PersonModel.class::cast).findAny().orElseThrow();
                        MedicalRecordModel medicalRecordModel = list.stream().filter(MedicalRecordModel.class::isInstance).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                        SubStationsReplyInfoPerson infoPerson = new SubStationsReplyInfoPerson(personModel.getLastName(), personModel.getPhone(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                        infoAddress = new SubStationsReplyInfoAddress(personModel.getAddress(), personModel.getCity(), personModel.getZip());
                        liste.add(infoPerson);
                    }
                    return new SubStationsReplyInfoPersonByAddress(infoAddress, liste);
                })
                .toList();

        return new StationsReplyPersonDTO(subStationsReplyInfoPersonByAddressList);
    }

    public PersonInfoReplyPersonDTO personInfo(String firstName, String lastName) {

        List<SubPersonInfoReplyPerson> subPersonInfoReplyPersonList = factoringConcatStreamForGroupingPersonAndMedicalSameProfile()
                .filter(group -> group.stream().anyMatch(model -> model instanceof PersonModel personModel && personModel.getFirstName().equals(firstName) && personModel.getLastName().equals(lastName)))
                .map(subDto -> {
                    PersonModel personModel = subDto.stream().filter(model -> model instanceof PersonModel).map(PersonModel.class::cast).findAny().orElseThrow();
                    MedicalRecordModel medicalRecordModel = subDto.stream().filter(model -> model instanceof MedicalRecordModel).map(MedicalRecordModel.class::cast).findAny().orElseThrow();
                    return new SubPersonInfoReplyPerson(personModel.getLastName(), personModel.getAddress(), personModel.getCity(), personModel.getZip(), personModel.getEmail(), medicalRecordModel.getBirthdate(), medicalRecordModel.getMedications(), medicalRecordModel.getAllergies());
                })
                .toList();
        return new PersonInfoReplyPersonDTO(subPersonInfoReplyPersonList);
    }

    public CommunityEmailReplyPersonDTO communityEmail(String city) {
        List<String> listEmail = manageJsonData.personReaderJsonData().stream().filter(filteringCity -> filteringCity.getCity().equals(city)).map(PersonModel::getEmail).toList();
        return new CommunityEmailReplyPersonDTO(listEmail);
    }

    private Stream<? extends List<?>> factoringConcatStreamForGroupingPersonAndMedicalSameProfile(){
      return Stream.concat(Stream.of(manageJsonData.personReaderJsonData()), Stream.of(manageJsonData.medicalRecordReaderJsonData()))
                .flatMap(List::stream).collect(Collectors.groupingBy(
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
    }

    public void addPerson(AddPersonDto person) throws IOException {
        List<PersonModel> listPersonsExisting = manageJsonData.personReaderJsonData();
        if (manageJsonData.personReaderJsonData().stream().noneMatch(personExist -> personExist.getLastName().equals(person.getLastName()) && personExist.getFirstName().equals(person.getFirstName()))) {
            listPersonsExisting.add(new PersonModel(person.getFirstName(), person.getLastName(), person.getAddress(), person.getCity(), person.getZip(), person.getPhone(), person.getEmail()));
        }
        manageJsonData.personWriterJsonData(listPersonsExisting);
    }

}