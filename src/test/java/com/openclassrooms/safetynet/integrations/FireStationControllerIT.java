package com.openclassrooms.safetynet.integrations;

import com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException;
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
public class FireStationControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ManageJsonData manageJsonDataSpy;


    @AfterEach
    void setUp() throws IOException, ManageJsonDataCustomException.InitException {
        Files.copy(Path.of("src/main/resources/data.json"), Path.of("src/main/resources/dataForWriterTest.json"), StandardCopyOption.REPLACE_EXISTING);
        manageJsonDataSpy.init();
    }


    @Test
    public void testAddFireStation() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\": \"2 rue des bois\", \"station\": \"1\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
      Assertions.assertTrue(manageJsonDataSpy.fireStationReaderJsonData().stream().anyMatch(model -> model.getAddress().equals("2 rue des bois")));
    }

    @Test
    public void testAddFireStationAlreadyExist() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\": \"1509 Culver St\", \"station\": \"3\"}"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\": \"1509 Culver St\", \"station\": \"3\", \"newNumberStation\": \"5\"}"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertTrue(manageJsonDataSpy.fireStationReaderJsonData().stream().anyMatch(model -> model.getAddress().equals("1509 Culver St") && model.getStation().equals("5")));
    }
    @Test
    public void testUpdateFireStationNotFound() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\": \"2 rue des bois\", \"station\": \"3\", \"newNumberStation\": \"5\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testDeleteFireStation() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\": \"1509 Culver St\", \"station\": \"3\"}"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertTrue(manageJsonDataSpy.fireStationReaderJsonData().stream().noneMatch(model -> model.getAddress().equals("1509 Culver St") && model.getStation().equals("3")));
    }

    @Test
    public void testDeleteFireStationNotFound() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\": \"2 rue des bois\", \"station\": \"3\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
