package com.openclassrooms.safetynet.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    manageJsonData.init();
    }
    // Testing methods for reading JSON data
    @Test
    void fireStationReaderJsonDataTest() {
        FireStationModel exceptedResponse = new FireStationModel("908 73rd St", "1");
        FireStationModel reply = manageJsonData.fireStationReaderJsonData().stream().filter(listDTO -> listDTO.getAddress().equals(exceptedResponse.getAddress()))
                .findFirst()
                .orElse(null);
        assertNotNull(reply);
        assertEquals(exceptedResponse.toString(), reply.toString());

    }

    @Test
    void personReaderJsonDataJsonDataTest() {
        PersonModel exceptedResponse = new PersonModel("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        PersonModel reply = manageJsonData.personReaderJsonData().stream().filter(listDTO -> listDTO.getFirstName().equals(exceptedResponse.getFirstName())
                        && listDTO.getLastName().equals(exceptedResponse.getLastName()))
                .findFirst()
                .orElse(null);
        assertNotNull(reply);
        assertEquals(exceptedResponse.toString(), reply.toString());

    }

    @Test
    void medicalRecordReaderJsonDataTest() {
        MedicalRecordModel exceptedResponse = new MedicalRecordModel("John", "Boyd", "03/06/1984", List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan"));
        MedicalRecordModel reply = manageJsonData.medicalRecordReaderJsonData().stream().filter(listDTO -> listDTO.getFirstName().equals(exceptedResponse.getFirstName())
                        && listDTO.getLastName().equals(exceptedResponse.getLastName()))
                .findFirst()
                .orElse(null);
        assertNotNull(reply);
        assertEquals(exceptedResponse.toString(), reply.toString());

    }


    // Test method for writing JSON data for persons
    @Test
    void personWriterJsonDataTest() throws IOException {
        // List of persons to write
        List<PersonModel> listPerson = List.of(new PersonModel("Thery", "Eddari", "", "", "", "", ""));
        // StringWriter to store output data from writeValue
        StringWriter stringWriterJsonOutput = new StringWriter();
        // StringWriter to store expected data (response and expected format)
        StringWriter stringWriterExpectedJsonValue = getStringWriterForPerson();

        doNothing().when(objectWriterSpy).writeValue(any(File.class),any());

        // Stubbing objectMapperSpy.writer to return an ObjectWriterSpy which will be used to Stub this ObjectWriter's writeValue method
        when(objectMapperSpy.writer(any(OutputFormatIndentationJsonData.class))).thenReturn(objectWriterSpy);
        // Stub the writeValue method to write the response (from File to StringWriter)
        doAnswer(invocation -> {
            objectWriterSpy.writeValue(stringWriterJsonOutput, invocation.getArgument(1));
            return null; // writeValue returns void, so need to return null
        }).when(objectWriterSpy).writeValue(any(File.class), any());


        // Call the method to be tested
        manageJsonData.personWriterJsonData(listPerson);


        // Verify the method call
        verify(objectWriterSpy).writeValue(any(StringWriter.class), any());
        // Assert on part of String must be same.
        Assertions.assertEquals(stringWriterJsonOutput.toString().substring(0, stringWriterExpectedJsonValue.toString().length()), stringWriterExpectedJsonValue.toString());

    }

    // Test method for writing JSON data for fire stations
    @Test
    void fireStationWriterJsonDataTest() throws IOException {

        // List of fire stations to write
        List<FireStationModel> listFireStation = List.of(new FireStationModel("10 rue des bois", "20"));
        // StringWriter to store output data from writeValue
        StringWriter stringWriterJsonOutput = new StringWriter();
        // StringWriter to store expected data (response and expected format)
        StringWriter stringWriterExpectedJsonValue = getStringWriterForFireStation();


        // Stubbing objectMapperSpy.writer to return an ObjectWriterSpy which will be used to Stub this ObjectWriter's writeValue method
        when(objectMapperSpy.writer(any(OutputFormatIndentationJsonData.class))).thenReturn(objectWriterSpy);
        // Stub the writeValue method to write the response (from File to StringWriter)
        doAnswer(invocation -> {
            objectWriterSpy.writeValue(stringWriterJsonOutput, invocation.getArgument(1));
            return null; // writeValue returns void, so need to return null
        }).when(objectWriterSpy).writeValue(any(File.class), any());


        // Call the method to be tested
        manageJsonData.fireStationWriterJsonData(listFireStation);


        // Verify the method call
        verify(objectWriterSpy).writeValue(any(StringWriter.class), any());
        // Assert on part of String must be same (-7 this is to rectify the offset in relation to the desired character)
        int beginIndex = stringWriterJsonOutput.toString().indexOf("firestations") - 7;
        int endIndex = beginIndex + stringWriterExpectedJsonValue.toString().length();
        assertEquals(stringWriterJsonOutput.toString().substring(beginIndex, endIndex), stringWriterExpectedJsonValue.toString());

    }

    // Test method for writing JSON data for medical records
    @Test
    void MedicalRecordWriterJsonDataTest() throws IOException {
        // List of medical records to write
        List<MedicalRecordModel> listMedicalRecord = List.of(new MedicalRecordModel("Thery", "Eddari", "", List.of(), List.of()));
        // StringWriter to store output data from writeValue
        StringWriter stringWriterJsonOutput = new StringWriter();
        // StringWriter to store expected data (response and expected format)
        StringWriter stringWriterExpectedJsonValue = getStringWriterForMedicalRecord();


        // Stubbing objectMapperSpy.writer to return an ObjectWriterSpy which will be used to Stub this ObjectWriter's writeValue method
        when(objectMapperSpy.writer(any(OutputFormatIndentationJsonData.class))).thenReturn(objectWriterSpy);
        // Stub the writeValue method to write the response (from File to StringWriter)
        doAnswer(invocation -> {
            objectWriterSpy.writeValue(stringWriterJsonOutput, invocation.getArgument(1));
            return null; // writeValue returns void, so need to return null
        }).when(objectWriterSpy).writeValue(any(File.class), any());


        // Call the method to be tested
        manageJsonData.medicalRecordWriterJsonData(listMedicalRecord);


        // Verify the method call
        verify(objectWriterSpy).writeValue(any(StringWriter.class), any());
        // Assert on part of String must be same (-7 this is to rectify the offset in relation to the desired character)
        int beginIndex = stringWriterJsonOutput.toString().indexOf("medicalrecords") - 7;
        int endIndex = beginIndex + stringWriterExpectedJsonValue.toString().length();
        assertEquals(stringWriterJsonOutput.toString().substring(beginIndex, endIndex), stringWriterExpectedJsonValue.toString());
    }

    // Test method for writing JSON data for medical records
    @Test
    void ensureJsonInMemoryUpdate() throws IOException, NoSuchFieldException, IllegalAccessException {
        // List of medical records to write
        List<PersonModel> listPerson = List.of(new PersonModel("Thery", "Eddari", "", "", "", "", ""));
        //Person excepted into dataJsonMemory (dataJson) in manageJsonData
        PersonModel exceptedResponse = new PersonModel("Thery", "Eddari", "", "", "", "", "");
        // Stubbing objectMapperSpy.writer to return an ObjectWriterSpy which will be used to Stub this ObjectWriter's writeValue method
        when(objectMapperSpy.writer(any(OutputFormatIndentationJsonData.class))).thenReturn(objectWriterSpy);
        // Stub the writeValue method to do nothing
        doNothing().when(objectWriterSpy).writeValue(any(File.class),any());

        // Call the method to be tested
        manageJsonData.personWriterJsonData(listPerson);

        // process to read the current context of the dataJson field (reflexion)
        //Initialize map to store data
        Map<String, Object> mapDataJsonBackup = new HashMap<>();
        try {
            Field dataJsonField = ManageJsonData.class.getDeclaredField("dataJson");
            dataJsonField.setAccessible(true);
            Object dataJsonBackupObject = dataJsonField.get(manageJsonData);
            dataJsonField.setAccessible(false);
            mapDataJsonBackup = objectMapperSpy.convertValue(dataJsonBackupObject, new TypeReference<>() {});
        } catch (NoSuchFieldException e) {
        //"Erreur lors de la sauvegarde du contexte : champ non trouvé."
        } catch (IllegalAccessException e) {
        //Erreur lors de la sauvegarde du contexte : accès illégal au champ
        } catch (Exception e) {
        //"Erreur inattendue lors de la sauvegarde du contexte."
        }
        //get persons collection of dataBackup and check the modification
        List<PersonModel> listPersonDataJsonBackup = objectMapperSpy.convertValue(mapDataJsonBackup.get("persons"), new TypeReference<>() {});
        PersonModel personExist =  listPersonDataJsonBackup.stream()
                .filter(personWanted ->
                     personWanted.getFirstName().equals(exceptedResponse.getFirstName()))
                .findFirst().orElseThrow();
        Assertions.assertEquals(exceptedResponse.toString(), personExist.toString());

    }

    // Method to create a StringWriter for Person data to test the formatting and response waiting
    private StringWriter getStringWriterForPerson() {
        StringWriter stringWriterExpectedJsonValue = new StringWriter();
        stringWriterExpectedJsonValue.write(
                "{" + System.lineSeparator()
                        + "  " + "\"persons\": [" + System.lineSeparator()
                        + "\t{ " + "\"firstName\":\"Thery\", \"lastName\":\"Eddari\", \"address\":\"\", \"city\":\"\", \"zip\":\"\", \"phone\":\"\", \"email\":\"\" }" + System.lineSeparator() +
                        "  ]" + ","
        );
        return stringWriterExpectedJsonValue;
    }

    // Method to create a StringWriter for FireStation data to test the formatting and response waiting
    private StringWriter getStringWriterForFireStation() {
        StringWriter stringWriterExpectedJsonValue = new StringWriter();
        stringWriterExpectedJsonValue.write(
                "]," + System.lineSeparator()
                        + "  " + "\"firestations\": [" + System.lineSeparator()
                        + "\t{ " + "\"address\":\"10 rue des bois\", \"station\":\"20\" }" + System.lineSeparator() +
                        "  ]" + ","
        );
        return stringWriterExpectedJsonValue;
    }

    // Method to create a StringWriter for Medical Record data to test the formatting and response waiting
    private StringWriter getStringWriterForMedicalRecord() {
        StringWriter stringWriterExpectedJsonValue = new StringWriter();
        stringWriterExpectedJsonValue.write(
                "]," + System.lineSeparator()
                        + "  " + "\"medicalrecords\": [" + System.lineSeparator()
                        + "\t{ " + "\"firstName\":\"Thery\", \"lastName\":\"Eddari\", \"birthdate\":\"\", \"medications\":[], \"allergies\":[] }" + System.lineSeparator() +
                        "  ]"
        );
        return stringWriterExpectedJsonValue;
    }

}
