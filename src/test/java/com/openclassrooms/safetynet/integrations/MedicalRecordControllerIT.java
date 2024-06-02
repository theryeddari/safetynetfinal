package com.openclassrooms.safetynet.integrations;

import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@SpringBootTest(properties = { "path.file = src/main/resources/dataForWriterTest.json" })
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ManageJsonData manageJsonData;
    

    @AfterEach
    void setUp() throws IOException {
        Files.copy(Path.of("src/main/resources/data.json"), Path.of("src/main/resources/dataForWriterTest.json"), StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    public void testAddMedicalRecord() throws Exception {
        String medicalRecordJson = "{ \"firstName\": \"Barque\", \"lastName\": \"Coule\", \"birthdate\": \"01/01/2000\", \"medications\": [], \"allergies\": [] }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated());
      Assertions.assertTrue(manageJsonData.medicalRecordReaderJsonData().stream().anyMatch(model -> model.getFirstName().equals("Barque") && model.getLastName().equals("Coule")));
    }

    @Test
    public void testAddMedicalRecordAlreadyExist() throws Exception {
        String medicalRecordJson = "{ \"firstName\": \"John\", \"lastName\": \"Boyd\", \"birthdate\": \"\", \"medications\": [], \"allergies\": [] }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson)
                ).andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        String medicalRecordJson = "{ \"firstName\": \"John\", \"lastName\": \"Boyd\", \"birthdate\": \"\", \"medications\": [], \"allergies\": [] }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson)
                ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertTrue(manageJsonData.medicalRecordReaderJsonData().stream().anyMatch(model -> model.getFirstName().equals("John") && model.getLastName().equals("Boyd") && model.getBirthdate().isEmpty()));
    }
    @Test
    public void testUpdateMedicalRecordNotFound() throws Exception {
        String medicalRecordJson = "{ \"firstName\": \"Berne\", \"lastName\": \"Jean\", \"birthdate\": \"\", \"medications\": [], \"allergies\": [] }";
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson)
                ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testDeleteMedicalRecord() throws Exception {
        String medicalRecordJson = "{ \"firstName\": \"John\", \"lastName\": \"Boyd\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson)
                ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertTrue(manageJsonData.medicalRecordReaderJsonData().stream().noneMatch(model -> model.getFirstName().equals("John") && model.getLastName().equals("Boyd")));
    }

    @Test
    public void testDeleteMedicalRecordNotFound() throws Exception {
        String medicalRecordJson = "{ \"firstName\": \"Lourd\", \"lastName\": \"Al\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson)
                ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
