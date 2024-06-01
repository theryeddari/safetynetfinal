package com.openclassrooms.safetynet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.dto.requests.AddMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.UpdateMedicalRecordDto;
import com.openclassrooms.safetynet.services.MedicalRecordService;
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
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddMedicalRecord() throws Exception {
        doNothing().when(medicalRecordService).addMedicalRecord(any(AddMedicalRecordDto.class));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddMedicalRecordDto())))
                .andExpect(status().isCreated());

        verify(medicalRecordService).addMedicalRecord(any(AddMedicalRecordDto.class));
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        doNothing().when(medicalRecordService).updateMedicalRecord(any(UpdateMedicalRecordDto.class));

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateMedicalRecordDto())))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).updateMedicalRecord(any(UpdateMedicalRecordDto.class));
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        doNothing().when(medicalRecordService).deleteMedicalRecord(any(DeleteMedicalRecordDto.class));

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DeleteMedicalRecordDto())))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).deleteMedicalRecord(any(DeleteMedicalRecordDto.class));
    }
}