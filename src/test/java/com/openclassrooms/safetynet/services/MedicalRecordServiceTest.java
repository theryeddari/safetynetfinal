package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdateMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MedicalRecordServiceTest {
    @Autowired
    MedicalRecordService medicalRecordService;
    @SpyBean
    ManageJsonData manageJsonDataSpy;

    MedicalRecordServiceTest(){
    }
    @Test
    void addMedicalRecordTest() throws IOException {
        //use of a modifiable table to store an initial list which can be dynamically modified by the method tested thanks to the reference
        List<MedicalRecordModel> listDataJsonMedicalRecordForMock = new ArrayList<>(List.of(new MedicalRecordModel("Jean","Jacque","",List.of(""),List.of(""))));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.medicalRecordReaderJsonData()).thenReturn(listDataJsonMedicalRecordForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        //person to add
        AddOrUpdateMedicalRecordDto newMedicalRecord = new AddOrUpdateMedicalRecordDto("Jean","Jacque","04/13/2000",List.of("aspirin"),List.of("pollen"));
        medicalRecordService.addMedicalRecord(newMedicalRecord);
        Assertions.assertTrue(listDataJsonMedicalRecordForMock.stream().anyMatch(medicalRecord -> medicalRecord.getLastName().equals(newMedicalRecord.getLastName()) && medicalRecord.getFirstName().equals(newMedicalRecord.getFirstName())));
    }
    @Test
    void updateMedicalRecordTest() throws IOException {
        AddOrUpdateMedicalRecordDto updateMedicalRecord = new AddOrUpdateMedicalRecordDto("John","Boyd","",List.of(),List.of());
        List<MedicalRecordModel> listUpdatedMedicalRecord = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        doAnswer(invocation -> {
            listUpdatedMedicalRecord.addAll(invocation.getArgument(0));
            return null;
        }).when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        medicalRecordService.updateMedicalRecord(updateMedicalRecord);
        Assertions.assertTrue(listUpdatedMedicalRecord.stream().anyMatch(medicalRecord ->
                medicalRecord.getFirstName().equals(updateMedicalRecord.getFirstName())
                        && medicalRecord.getLastName().equals(updateMedicalRecord.getLastName())
                        && medicalRecord.getBirthdate().equals(updateMedicalRecord.getBirthdate())
                        && medicalRecord.getMedications().equals(updateMedicalRecord.getMedications())
                        && medicalRecord.getAllergies().equals(updateMedicalRecord.getAllergies())
        ));
    }
    @Test
    void deleteMedicalRecordTest() throws IOException {
        DeleteMedicalRecordDto deleteMedicalRecord = new DeleteMedicalRecordDto("John","Boyd");
        List<MedicalRecordModel> listDeletedMedicalRecord = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        doAnswer(invocation -> {
            listDeletedMedicalRecord.addAll(invocation.getArgument(0));
            return null;
        }).when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        medicalRecordService.deleteMedicalRecord(deleteMedicalRecord);
        Assertions.assertTrue(listDeletedMedicalRecord.stream().noneMatch(medicalRecord ->
                medicalRecord.getLastName().equals(deleteMedicalRecord.getLastName())
                && medicalRecord.getFirstName().equals(deleteMedicalRecord.getFirstName()))
        );
    }
}
