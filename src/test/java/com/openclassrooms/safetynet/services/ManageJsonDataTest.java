package com.openclassrooms.safetynet.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import com.openclassrooms.safetynet.utils.OutputFormatIndentationJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = { "path.file = src/main/resources/dataForWriterTest.json" })
class ManageJsonDataTest {

    @Autowired
    ManageJsonData manageJsonData;

    @SpyBean
    ObjectMapper objectMapperSpy;
    @Spy
    ObjectWriter objectWriterSpy = new ObjectMapper().writer(new OutputFormatIndentationJsonData());

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
        FireStationModel exceptedResponse = new FireStationModel("908 73rd St", "1");
        assertEquals(exceptedResponse.toString(), response.toString());
    }

    @Test
    void testPersonReaderByReturn() {
        PersonModel response = manageJsonData.testPersonReaderByReturn("John", "Boyd");
        PersonModel exceptedResponse = new PersonModel("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        assertEquals(exceptedResponse.toString(), response.toString());
    }

    @Test
    void testMedicalRecordReaderByReturn() {
        MedicalRecordModel response = manageJsonData.testMedicalRecordReaderByReturn("John", "Boyd");
        MedicalRecordModel exceptedResponse = new MedicalRecordModel("John", "Boyd", "03/06/1984", List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan"));
        assertEquals(exceptedResponse.toString(), response.toString());
    }

    //test writer
    @Test
    void personWriterJsonDataTest() throws IOException {
        //list to writing
        List<PersonModel> listPerson = List.of(new PersonModel("Thery", "Eddari", "", "", "", "", ""));
        //variable to store output data from writeValue
        StringWriter stringWriterJsonOutput = new StringWriter();
        //variable to store excepted data(response and format excepted) with Same Type of stringWriterJsonOutput in order to use AssertEqual later
        StringWriter stringWriterExpectedJsonValue = getStringWriter();
        // stubbing objectMapperSpy.writer to return an ObjectWriterSpy which will be used to Stub this ObjectWriter's writeValue method
        when(objectMapperSpy.writer(any(OutputFormatIndentationJsonData.class))).thenReturn(objectWriterSpy);
        // Stub the writeValue method to modify the argument or write the response (from File to Stringwritter).
        doAnswer(invocation -> {
            objectWriterSpy.writeValue(stringWriterJsonOutput, invocation.getArgument(1));
            return null; // writeValue return void, so need to return null
        }).when(objectWriterSpy).writeValue(any(File.class), any());
        //call methode
        manageJsonData.personWriterJsonData(listPerson);
        //verify call methode
        verify(objectWriterSpy).writeValue(any(StringWriter.class), any());
        // assert on part of String must be same.
        Assertions.assertEquals(stringWriterJsonOutput.toString().substring(0,stringWriterExpectedJsonValue.toString().length()),stringWriterExpectedJsonValue.toString());
    }

    private StringWriter getStringWriter() {
        StringWriter stringWriterExpectedJsonValue = new StringWriter();
        stringWriterExpectedJsonValue.write(
                 "{"+ System.lineSeparator()
                         + "  " + "\"persons\": [" + System.lineSeparator()
                         +"\t{ " + "\"firstName\":\"Thery\", \"lastName\":\"Eddari\", \"address\":\"\", \"city\":\"\", \"zip\":\"\", \"phone\":\"\", \"email\":\"\" }" + System.lineSeparator() +
                         "  ]"+","
        );
        return stringWriterExpectedJsonValue;
    }
}