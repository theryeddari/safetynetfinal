package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.dto.requests.AddMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.UpdateMedicalRecordDto;
import com.openclassrooms.safetynet.services.FireStationService;
import com.openclassrooms.safetynet.services.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;

@RestController
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMedicalRecord(@RequestBody AddMedicalRecordDto addMedicalRecordDto) throws AddMedicalRecordException {
        medicalRecordService.addMedicalRecord(addMedicalRecordDto);
        logger.info("POST request successfully saved with the following data: {}",addMedicalRecordDto);
    }
    @PutMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMedicalRecord(@RequestBody UpdateMedicalRecordDto updateMedicalRecordDto) throws UpdateMedicalRecordException {
        medicalRecordService.updateMedicalRecord(updateMedicalRecordDto);
        logger.info("PUT request successfully saved with the following data: {}", updateMedicalRecordDto);
    }
    @DeleteMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicalRecord(@RequestBody DeleteMedicalRecordDto deleteMedicalRecordDto) throws DeleteMedicalRecordException {
        medicalRecordService.deleteMedicalRecord(deleteMedicalRecordDto);
        logger.info("DELETE request successfully saved with the following data: {}", deleteMedicalRecordDto);
    }
}
