package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdatePersonDto;
import com.openclassrooms.safetynet.dto.requests.DeletePersonDto;
import com.openclassrooms.safetynet.dto.responses.*;
import com.openclassrooms.safetynet.dto.responses.submodels.*;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
@SpringBootTest
class PersonServiceTest {
    @Autowired
    PersonService personService;
    @SpyBean
    ManageJsonData manageJsonDataSpy;

    PersonServiceTest() {
    }

    @Test
    void fireStationReplyTest() {
        FireStationReplyPersonDTO reply = personService.fireStationReply("2");
        SubFireStationModelReplyForCount countExcepted = new SubFireStationModelReplyForCount("4", "1");
        SubFireStationReplyPerson personExcepted = new SubFireStationReplyPerson("Jonanathan","Marrack","29 15th St","Culver","97451","841-874-6513");

        Assertions.assertEquals(countExcepted.toString(),reply.getCountPerson().toString());
        Assertions.assertEquals(List.of(personExcepted).toString(), reply.getPersons().stream().filter(listPerson -> listPerson.getFirstName().equals("Jonanathan") && listPerson.getLastName().equals("Marrack")).toList().toString());
    }
    @Test
    void childAlertReplyTest() {
        ChildAlertReplyPersonDTO reply = personService.childAlertReply("1509 Culver St");

        SubChildAlertReplyChildren childExcepted = new SubChildAlertReplyChildren("Tenley","Boyd","02/18/2012");
        SubChildAlertReplyAdultFamily adultExcepted = new SubChildAlertReplyAdultFamily("John","Boyd");

        Assertions.assertEquals(List.of(adultExcepted).toString(),reply.getIdentityFamily().stream().filter(listAdult -> listAdult.getFirstName().equals("John") && listAdult.getLastName().equals("Boyd")).toList().toString());
        Assertions.assertEquals(List.of(childExcepted).toString(),reply.getChildren().stream().filter(listChildren -> listChildren.getFirstName().equals("Tenley") && listChildren.getLastName().equals("Boyd")).toList().toString());
    }
    @Test
    void phoneAlertReplyTest() {
        PhoneAlertReplyPersonDTO reply = personService.phoneAlert("1");
        PhoneAlertReplyPersonDTO phoneExcepted = new PhoneAlertReplyPersonDTO(List.of("841-874-7462"));

        Assertions.assertEquals(phoneExcepted.getPhone(),reply.getPhone().stream().filter(listPhone -> listPhone.contains("841-874-7462")).toList());
    }
    @Test
    void fireTest() {
        FireReplyPersonDTO reply = personService.fire("1509 Culver St");
        SubFireReplyReplyInfoPerson personInfoExcepted = new SubFireReplyReplyInfoPerson("Boyd","841-874-6512","03/06/1984", List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan"));
        Assertions.assertEquals(List.of(personInfoExcepted).toString(),reply.getInfoPerson().stream().filter(listpersonInfo -> listpersonInfo.getLastName().equals("Boyd") && listpersonInfo.getBirthdate().equals("03/06/1984")).toList().toString());
    }
    @Test
    void StationsTest() {
       StationsReplyPersonDTO reply = personService.floodStation(new ArrayList<>(Arrays.asList("1", "2")));
       SubStationsReplyInfoPerson preExcepted1InfoPerson = new SubStationsReplyInfoPerson("Cadigan","841-874-7458","08/06/1945", List.of("tradoxidine:400mg"),List.of(""));
       SubStationsReplyInfoAddress preExcepted1InfoAddress = new SubStationsReplyInfoAddress("951 LoneTree Rd","Culver","97451");
        SubStationsReplyInfoPerson preExcepted2InfoPerson = new SubStationsReplyInfoPerson("Marrack","841-874-6513","01/03/1989", List.of(""),List.of(""));
        SubStationsReplyInfoAddress preExcepted2InfoAddress = new SubStationsReplyInfoAddress("29 15th St","Culver","97451");

        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoAddress().toString().equals(preExcepted1InfoAddress.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoPerson().toString().contains(preExcepted1InfoPerson.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoAddress().toString().equals(preExcepted2InfoAddress.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoPerson().toString().contains(preExcepted2InfoPerson.toString())));
    } @Test
    void personInfoTest() {
        PersonInfoReplyPersonDTO reply = personService.personInfo("John", "Boyd");
        List<SubPersonInfoReplyPerson> personInfoExcepted =  List.of( new SubPersonInfoReplyPerson("Boyd","1509 Culver St","Culver","97451","jaboyd@email.com","03/06/1984",List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan")));
        Assertions.assertTrue(reply.getListPerson().toString().contains(personInfoExcepted.toString()));
    }
    @Test
    void communityEmailTest() {
        CommunityEmailReplyPersonDTO reply = personService.communityEmail("Culver");
        String exceptedMail = "jaboyd@email.com";
        Assertions.assertTrue(reply.getMail().contains(exceptedMail));
    }
    @Test
    void addPersonTest() throws PersonWriterException {
        //use of a modifiable table to store an initial list which can be dynamically modified by the method tested thanks to the reference
        List<PersonModel> listDataJsonPersonForMock = new ArrayList<>(List.of(new PersonModel("Jean", "Jacque", "bla", "bla", "", "", "")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.personReaderJsonData()).thenReturn(listDataJsonPersonForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        //person to add
        AddOrUpdatePersonDto newPerson = new AddOrUpdatePersonDto("thery","eddari","","","","","");
        personService.addPerson(newPerson);
        Assertions.assertTrue(listDataJsonPersonForMock.stream().anyMatch(person -> person.getLastName().equals(newPerson.getLastName()) && person.getFirstName().equals(newPerson.getFirstName())));
    }
    @Test
    void updatePersonTest() throws PersonWriterException {
        AddOrUpdatePersonDto updatePerson = new AddOrUpdatePersonDto("John","Boyd","","","","","");
        List<PersonModel> listUpdatedPersons = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        doAnswer(invocation -> {
            listUpdatedPersons.addAll(invocation.getArgument(0));
            return null;
                }
        ).when(manageJsonDataSpy).personWriterJsonData(anyList());
        personService.updatePerson(updatePerson);
        System.out.println(listUpdatedPersons);
        Assertions.assertTrue(listUpdatedPersons.stream().anyMatch(person ->
                person.getFirstName().equals(updatePerson.getFirstName())
                        && person.getLastName().equals(updatePerson.getLastName())
                        && person.getEmail().equals(updatePerson.getEmail())
                        && person.getAddress().equals(updatePerson.getAddress())
                        && person.getPhone().equals(updatePerson.getPhone())
                        && person.getCity().equals(updatePerson.getCity())
                        && person.getZip().equals(updatePerson.getZip())
        ));
    }
    @Test
    void deletePersonTest() throws PersonWriterException {
        DeletePersonDto deletePerson = new DeletePersonDto("John","Boyd");
        List<PersonModel> listUpdatedPersons = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        doAnswer(invocation -> {
                    listUpdatedPersons.addAll(invocation.getArgument(0));
                    return null;
                }
        ).when(manageJsonDataSpy).personWriterJsonData(anyList());
        personService.deletePerson(deletePerson);
        System.out.println(listUpdatedPersons);
        Assertions.assertTrue(listUpdatedPersons.stream().noneMatch(person ->
                person.getLastName().equals(deletePerson.getLastName()) && person.getFirstName().equals(deletePerson.getFirstName()))
        );
    }
}