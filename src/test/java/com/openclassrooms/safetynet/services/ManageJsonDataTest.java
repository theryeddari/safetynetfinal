package com.openclassrooms.safetynet.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ManageJsonDataTest {
    @Autowired
    ManageJsonData manageJsonData;
    @Test
    void fireStationReaderJsonDataTest() throws IOException {
        List<FireStationModel> reply = manageJsonData.fireStationReaderJsonData();
        assertNotNull(reply);
        }
    @Test
    void personReaderJsonDataJsonDataTest() throws IOException {
        List<PersonModel> reply = manageJsonData.personReaderJsonData();
        assertNotNull(reply);
    }
    @Test
    void medicalRecordReaderJsonDataTest() throws IOException {
        List<MedicalRecordModel> reply = manageJsonData.medicalRecordReaderJsonData();
        assertNotNull(reply);
    }
}
