package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = { "path.file = src/main/resources/dataForWriterTest.json" })
class ManageJsonDataTest {
    @Autowired
    ManageJsonData manageJsonData;
    @BeforeEach
    void setUp() throws IOException {
        Path originalFile = Path.of("src/main/resources/data.json");
        Path testFile = Path.of("src/main/resources/dataForWriterTest.json");
        Files.write(testFile, new byte[0]);

        // Lire le contenu de la sauvegarde
        String content = Files.readString(originalFile);

        // Écrire le contenu de la sauvegarde dans le fichier de test
        Files.write(testFile, content.getBytes());
    }
    @Test
    void fireStationReaderJsonDataTest() {
        List<FireStationModel> reply = manageJsonData.fireStationReaderJsonData();
        assertNotNull(reply);
        }
    @Test
    void personReaderJsonDataJsonDataTest() {
        List<PersonModel> reply = manageJsonData.personReaderJsonData();
        assertNotNull(reply);
    }
    @Test
    void medicalRecordReaderJsonDataTest() {
        List<MedicalRecordModel> reply = manageJsonData.medicalRecordReaderJsonData();
        assertNotNull(reply);
    }
    @Test
    void testFireStationReaderByReturn() {
        FireStationModel response = manageJsonData.testFireStationReaderByReturn("908 73rd St");
        FireStationModel exceptedResponse = new FireStationModel("908 73rd St","1");
        Assertions.assertEquals(exceptedResponse.toString(),response.toString());
    }
    @Test
    void testPersonReaderByReturn() {
        PersonModel response = manageJsonData.testPersonReaderByReturn("John", "Boyd");
        PersonModel exceptedResponse = new PersonModel("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Assertions.assertEquals(exceptedResponse.toString(),response.toString());
    }
    @Test
    void testMedicalRecordReaderByReturn() {
        MedicalRecordModel response = manageJsonData.testMedicalRecordReaderByReturn("John", "Boyd");
        MedicalRecordModel exceptedResponse = new MedicalRecordModel("John","Boyd","03/06/1984",List.of( "aznol:350mg, hydrapermazol:100mg"),List.of("nillacilan"));
        Assertions.assertEquals(exceptedResponse.toString(),response.toString());
    }
    //test writer
    @Test
    void personWriterJsonDataTest() throws IOException {
        List<PersonModel> listPerson = List.of(new PersonModel("Thery","Eddari","","","","",""));
        manageJsonData.personWriterJsonData(listPerson);

    }
}
//TODO: pas de mock sur ma class util.
