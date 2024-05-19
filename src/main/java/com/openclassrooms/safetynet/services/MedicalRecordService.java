package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdateMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    private final ManageJsonData manageJsonData;

    // Constructor to inject ManageJsonData dependency
    public MedicalRecordService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    // Method to add a new medical record
    public void addMedicalRecord(AddOrUpdateMedicalRecordDto newMedicalRecord) throws IOException {
        List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
        // Check if the medical record already exists
        if (listMedicalRecordExisting.stream().noneMatch(medicalRecordExist -> medicalRecordExist.getLastName().equals(newMedicalRecord.getLastName()) && medicalRecordExist.getFirstName().equals(newMedicalRecord.getFirstName()))) {
            // Add the new medical record if it does not exist
            listMedicalRecordExisting.add(new MedicalRecordModel(newMedicalRecord.getFirstName(), newMedicalRecord.getLastName(), newMedicalRecord.getBirthdate(), newMedicalRecord.getMedications(), newMedicalRecord.getAllergies()));
        }
        // Write the updated list of medical records back to the JSON file
        manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
    }

    // Method to update an existing medical record
    public void updateMedicalRecord(AddOrUpdateMedicalRecordDto updateMedicalRecord) throws IOException {
        List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
        // Get the reference to the filtered medical record object
        Optional<MedicalRecordModel> wantedMedicalRecordUpdate = listMedicalRecordExisting.stream().filter(medicalRecord -> medicalRecord.getFirstName().equals(updateMedicalRecord.getFirstName()) && medicalRecord.getLastName().equals(updateMedicalRecord.getLastName())).findFirst();
        // If there is a reference, access the object and modify its properties
        wantedMedicalRecordUpdate.ifPresent(modifyMedicalRecord -> {
            modifyMedicalRecord.setBirthdate(updateMedicalRecord.getBirthdate());
            modifyMedicalRecord.setMedications(updateMedicalRecord.getMedications());
            modifyMedicalRecord.setAllergies(updateMedicalRecord.getAllergies());
        });
        // Write the updated list of medical records back to the JSON file
        manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
    }

    // Method to delete an existing medical record
    public void deleteMedicalRecord(DeleteMedicalRecordDto deleteMedicalRecord) throws IOException {
        List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
        // Remove the medical record if it matches the given first name and last name
        if(listMedicalRecordExisting.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(deleteMedicalRecord.getFirstName()) && medicalRecord.getLastName().equals(deleteMedicalRecord.getLastName()))) {
            // Write the updated list of medical records back to the JSON file
            manageJsonData.medicalRecordWriterJsonData(new ArrayList<>());
        } // TODO: Add exception handling if the medical record is not found
    }
}

