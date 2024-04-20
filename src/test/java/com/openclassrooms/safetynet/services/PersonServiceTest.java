package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.responses.*;
import com.openclassrooms.safetynet.dto.responses.submodels.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class PersonServiceTest {
    @Autowired
    PersonService personService;
    @Test
    void fireStationReplyTest() throws IOException {
        FireStationReplyPersonDTO reply = personService.fireStationReply("2");

        SubFireStationModelReplyForCount countExcepted = new SubFireStationModelReplyForCount("4", "1");
        SubFireStationReplyPerson personExcepted = new SubFireStationReplyPerson("Jonanathan","Marrack","29 15th St","Culver","97451","841-874-6513");

        Assertions.assertEquals(countExcepted.toString(),reply.getCountPerson().toString());
        Assertions.assertEquals(List.of(personExcepted).toString(), reply.getPersons().stream().filter(listPerson -> listPerson.getFirstName().equals("Jonanathan") && listPerson.getLastName().equals("Marrack")).toList().toString());
    }
    @Test
    void childAlertReplyTest() throws IOException {
        ChildAlertReplyPersonDTO reply = personService.childAlertReply("1509 Culver St");

        SubChildAlertReplyChildren childExcepted = new SubChildAlertReplyChildren("Tenley","Boyd","02/18/2012");
        SubChildAlertReplyAdultFamily adultExcepted = new SubChildAlertReplyAdultFamily("John","Boyd");

        Assertions.assertEquals(List.of(adultExcepted).toString(),reply.getIdentityFamily().stream().filter(listAdult -> listAdult.getFirstName().equals("John") && listAdult.getLastName().equals("Boyd")).toList().toString());
        Assertions.assertEquals(List.of(childExcepted).toString(),reply.getChildren().stream().filter(listChildren -> listChildren.getFirstName().equals("Tenley") && listChildren.getLastName().equals("Boyd")).toList().toString());
    }
    @Test
    void phoneAlertReplyTest() throws IOException {
        PhoneAlertReplyPersonDTO reply = personService.phoneAlert("1");
        PhoneAlertReplyPersonDTO phoneExcepted = new PhoneAlertReplyPersonDTO(List.of("841-874-7462"));

        Assertions.assertEquals(phoneExcepted.getPhone(),reply.getPhone().stream().filter(listPhone -> listPhone.contains("841-874-7462")).toList());
    }
    @Test
    void fireTest() throws IOException {
        FireReplyPersonDTO reply = personService.fire("1509 Culver St");
        SubFireReplyReplyInfoPerson personInfoExcepted = new SubFireReplyReplyInfoPerson("Boyd","841-874-6512","03/06/1984", List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan"));
        Assertions.assertEquals(List.of(personInfoExcepted).toString(),reply.getInfoPerson().stream().filter(listpersonInfo -> listpersonInfo.getLastName().equals("Boyd") && listpersonInfo.getBirthdate().equals("03/06/1984")).toList().toString());
    }
    @Test
    void StationsTest() throws IOException {
       StationsReplyPersonDTO reply = personService.floodStation(new ArrayList<>(Arrays.asList("1", "2")));
       SubStationsReplyInfoPerson preExcepted1InfoPerson = new SubStationsReplyInfoPerson("Cadigan","841-874-7458","08/06/1945", List.of("tradoxidine:400mg"),List.of(""));
       SubStationsReplyInfoAddress preExcepted1InfoAddress = new SubStationsReplyInfoAddress("951 LoneTree Rd","Culver","97451");
        SubStationsReplyInfoPerson preExcepted2InfoPerson = new SubStationsReplyInfoPerson("Marrack","841-874-6513","01/03/1989", List.of(""),List.of(""));
        SubStationsReplyInfoAddress preExcepted2InfoAddress = new SubStationsReplyInfoAddress("29 15th St","Culver","97451");

        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoAddress().toString().equals(preExcepted1InfoAddress.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoPerson().toString().contains(preExcepted1InfoPerson.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoAddress().toString().equals(preExcepted2InfoAddress.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoPerson().toString().contains(preExcepted2InfoPerson.toString())));
    }
}

