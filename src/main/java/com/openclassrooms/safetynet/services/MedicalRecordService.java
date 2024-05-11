package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdateMedicalRecordDto;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    private final ManageJsonData manageJsonData;

    public MedicalRecordService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    public void addMedicalRecord(AddOrUpdateMedicalRecordDto newMedicalRecord) throws IOException {
        List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
        if (listMedicalRecordExisting.stream().noneMatch(medicalRecordExist -> medicalRecordExist.getLastName().equals(newMedicalRecord.getLastName()) && medicalRecordExist.getFirstName().equals(newMedicalRecord.getFirstName()))) {
            listMedicalRecordExisting.add(new MedicalRecordModel(newMedicalRecord.getFirstName(), newMedicalRecord.getLastName(), newMedicalRecord.getBirthdate(), newMedicalRecord.getMedications(), newMedicalRecord.getAllergies()));
        }
        manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
    }

    public void updateMedicalRecord(AddOrUpdateMedicalRecordDto updateMedicalRecord) throws IOException {
        List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
        //get the reference to the filtered person object
        Optional<MedicalRecordModel> wantedMedicalRecordUpdate = listMedicalRecordExisting.stream().filter(medicalRecord -> medicalRecord.getFirstName().equals(updateMedicalRecord.getFirstName()) && medicalRecord.getLastName().equals(updateMedicalRecord.getLastName())).findFirst();
        //if there is a reference then I access the object using this and modify the properties of the person
        wantedMedicalRecordUpdate.ifPresent(modifyMedicalRecord -> {
            modifyMedicalRecord.setBirthdate(updateMedicalRecord.getBirthdate());
            modifyMedicalRecord.setMedications(updateMedicalRecord.getMedications());
            modifyMedicalRecord.setAllergies(updateMedicalRecord.getAllergies());
        });
        manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
    }
}
