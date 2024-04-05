package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.responses.FireStationReplyPersonDTO;
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
        Assertions.assertEquals(List.of(personExcepted).toString(), reply.getPersons().stream().filter(listperson -> listperson.getFirstName().equals("Jonanathan") && listperson.getLastName().equals("Marrack")).toList().toString());
    }
}
