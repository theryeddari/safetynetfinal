package com.openclassrooms.safetynet.controllers;

import com.openclassrooms.safetynet.dto.requests.AddMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.UpdateMedicalRecordDto;
import com.openclassrooms.safetynet.services.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;

/**
 * Controller for handling medical record related requests.
 */
@RestController
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);

    private final MedicalRecordService medicalRecordService;

    /**
     * Constructs a new MedicalRecordController with the specified MedicalRecordService.
     *
     * @param medicalRecordService the medical record service to be used by this controller
     */
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Adds a new medical record.
     *
     * @param addMedicalRecordDto the medical record to add
     * @throws AddMedicalRecordException if an error occurs during the addition
     */
    @PostMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMedicalRecord(@RequestBody AddMedicalRecordDto addMedicalRecordDto) throws AddMedicalRecordException {
        logger.info("Attempting to save POST request with data: {}", addMedicalRecordDto);
        medicalRecordService.addMedicalRecord(addMedicalRecordDto);
        logger.info("POST request successfully saved with the following data: {}", addMedicalRecordDto);
    }

    /**
     * Updates an existing medical record.
     *
     * @param updateMedicalRecordDto the medical record to update
     * @throws UpdateMedicalRecordException if an error occurs during the update
     */
    @PutMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMedicalRecord(@RequestBody UpdateMedicalRecordDto updateMedicalRecordDto) throws UpdateMedicalRecordException {
        logger.info("Attempting to save PUT request with data: {}", updateMedicalRecordDto);
        medicalRecordService.updateMedicalRecord(updateMedicalRecordDto);
        logger.info("PUT request successfully saved with the following data: {}", updateMedicalRecordDto);
    }

    /**
     * Deletes a medical record.
     *
     * @param deleteMedicalRecordDto the medical record to delete
     * @throws DeleteMedicalRecordException if an error occurs during the deletion
     */
    @DeleteMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicalRecord(@RequestBody DeleteMedicalRecordDto deleteMedicalRecordDto) throws DeleteMedicalRecordException {
        logger.info("Attempting to save DELETE request with data: {}", deleteMedicalRecordDto);
        medicalRecordService.deleteMedicalRecord(deleteMedicalRecordDto);
        logger.info("DELETE request successfully saved with the following data: {}", deleteMedicalRecordDto);
    }
}
