package com.openclassrooms.safetynet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.dto.requests.AddFireStationDto;
import com.openclassrooms.safetynet.dto.requests.DeleteFireStationDto;
import com.openclassrooms.safetynet.dto.requests.UpdateFireStationDto;
import com.openclassrooms.safetynet.services.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddFireStation() throws Exception {
        doNothing().when(fireStationService).addFireStation(any(AddFireStationDto.class));

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddFireStationDto())))
                .andExpect(status().isCreated());

        verify(fireStationService).addFireStation(any(AddFireStationDto.class));
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        doNothing().when(fireStationService).updateFireStation(any(UpdateFireStationDto.class));

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateFireStationDto())))
                .andExpect(status().isNoContent());

        verify(fireStationService).updateFireStation(any(UpdateFireStationDto.class));
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        doNothing().when(fireStationService).deleteFireStation(any(DeleteFireStationDto.class));

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DeleteFireStationDto())))
                .andExpect(status().isNoContent());

        verify(fireStationService).deleteFireStation(any(DeleteFireStationDto.class));
    }
}
