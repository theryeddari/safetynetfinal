package com.openclassrooms.safetynet.integrations;

import com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class PersonControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ManageJsonData manageJsonDataSpy;
    

    @BeforeEach
    void setUp() throws IOException, ManageJsonDataCustomException.InitException {
        Files.copy(Path.of("src/main/resources/data.json"), Path.of("src/main/resources/dataForWriterTest.json"), StandardCopyOption.REPLACE_EXISTING);
        manageJsonDataSpy.init();
    }


    @Test
    public void testAddPerson() throws Exception {
        String personJson = "{ \"firstName\": \"Barque\", \"lastName\": \"Coule\", \"address\": \"blablarue\", \"city\": \"\", \"zip\": \"\", \"phone\": \"\", \"email\": \"\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated());
      Assertions.assertTrue(manageJsonDataSpy.personReaderJsonData().stream().anyMatch(model -> model.getFirstName().equals("Barque") && model.getLastName().equals("Coule")));
    }

    @Test
    public void testAddPersonAlreadyExist() throws Exception {
        String personJson = "{ \"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"blablarue\", \"city\": \"\", \"zip\": \"\", \"phone\": \"\", \"email\": \"\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                ).andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testUpdatePerson() throws Exception {
        String personJson = "{ \"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"blablarue\", \"city\": \"\", \"zip\": \"\", \"phone\": \"\", \"email\": \"\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertTrue(manageJsonDataSpy.personReaderJsonData().stream().anyMatch(model -> model.getFirstName().equals("John") && model.getLastName().equals("Boyd") && model.getAddress().equals("blablarue")));
    }
    @Test
    public void testUpdatePersonNotFound() throws Exception {
        String personJson = "{ \"firstName\": \"Berne\", \"lastName\": \"Jean\", \"address\": \"\", \"city\": \"\", \"zip\": \"\", \"phone\": \"\", \"email\": \"\" }";
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testDeletePerson() throws Exception {
        String personJson = "{ \"firstName\": \"John\", \"lastName\": \"Boyd\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertTrue(manageJsonDataSpy.personReaderJsonData().stream().noneMatch(model -> model.getFirstName().equals("John") && model.getLastName().equals("Boyd")));
    }

    @Test
    public void testDeletePersonNotFound() throws Exception {
        String personJson = "{ \"firstName\": \"Lourd\", \"lastName\": \"Al\" }";

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testGetResidentsByFireStation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
                        .param("stationNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetChildAlertByAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                        .param("address", "123 Main St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetPhoneAlertByFireStation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                        .param("firestation", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetFireByAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fire")
                        .param("address", "123 Main St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetFloodStationsByStations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                        .param("stations", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetPersonInfoByNames() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetCommunityEmailByCity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Smalltown")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

