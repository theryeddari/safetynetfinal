package com.openclassrooms.safetynet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.dto.requests.AddPersonDto;
import com.openclassrooms.safetynet.dto.requests.DeletePersonDto;
import com.openclassrooms.safetynet.dto.requests.UpdatePersonDto;
import com.openclassrooms.safetynet.dto.responses.*;
import com.openclassrooms.safetynet.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetResidentsByFireStation() throws Exception {
        when(personService.fireStationReply("1")).thenReturn(new FireStationReplyPersonDTO());

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).fireStationReply("1");
    }

    @Test
    public void testGetChildAlertByAddress() throws Exception {
        when(personService.childAlertReply("123 Main St")).thenReturn(new ChildAlertReplyPersonDTO());

        mockMvc.perform(get("/childAlert")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).childAlertReply("123 Main St");
    }

    @Test
    public void testGetPhoneAlertByFireStation() throws Exception {
        when(personService.phoneAlert("1")).thenReturn(new PhoneAlertReplyPersonDTO());

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).phoneAlert("1");
    }

    @Test
    public void testGetFireByAddress() throws Exception {
        when(personService.fire("123 Main St")).thenReturn(new FireReplyPersonDTO());

        mockMvc.perform(get("/fire")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).fire("123 Main St");
    }

    @Test
    public void testGetFloodStationsByStations() throws Exception {
        when(personService.floodStation(Arrays.asList("1", "2"))).thenReturn(new StationsReplyPersonDTO());

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).floodStation(Arrays.asList("1", "2"));
    }

    @Test
    public void testGetPersonInfoByNames() throws Exception {
        when(personService.personInfo( "Doe")).thenReturn(new PersonInfoReplyPersonDTO());

        mockMvc.perform(get("/personInfo")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).personInfo("Doe");
    }

    @Test
    public void testGetCommunityEmailByCity() throws Exception {
        when(personService.communityEmail("Springfield")).thenReturn(new CommunityEmailReplyPersonDTO());

        mockMvc.perform(get("/communityEmail")
                        .param("city", "Springfield"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService).communityEmail("Springfield");
    }

    @Test
    public void testAddPerson() throws Exception {
        doNothing().when(personService).addPerson(any(AddPersonDto.class));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddPersonDto())))
                .andExpect(status().isCreated());

        verify(personService).addPerson(any(AddPersonDto.class));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        doNothing().when(personService).updatePerson(any(UpdatePersonDto.class));

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdatePersonDto())))
                .andExpect(status().isNoContent());

        verify(personService).updatePerson(any(UpdatePersonDto.class));
    }

    @Test
    public void testDeletePerson() throws Exception {
        doNothing().when(personService).deletePerson(any(DeletePersonDto.class));

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DeletePersonDto())))
                .andExpect(status().isNoContent());

        verify(personService).deletePerson(any(DeletePersonDto.class));
    }
}