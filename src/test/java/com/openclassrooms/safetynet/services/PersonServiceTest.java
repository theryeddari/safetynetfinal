package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.responses.ChildAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.FireStationReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.PhoneAlertReplyPersonDTO;
import com.openclassrooms.safetynet.dto.responses.submodels.SubChildAlertReplyAdultFamily;
import com.openclassrooms.safetynet.dto.responses.submodels.SubChildAlertReplyChildren;
import com.openclassrooms.safetynet.dto.responses.submodels.SubFireStationModelReplyForCount;
import com.openclassrooms.safetynet.dto.responses.submodels.SubFireStationReplyPerson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
}
