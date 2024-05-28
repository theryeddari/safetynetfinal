package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.UpdateMedicalRecordDto;
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

import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;
import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void addMedicalRecordTest() throws MedicalRecordWriterException, AddMedicalRecordException {
        //use of a modifiable table to store an initial list which can be dynamically modified by the method tested thanks to the reference
        List<MedicalRecordModel> listDataJsonMedicalRecordForMock = new ArrayList<>(List.of(new MedicalRecordModel("Paul","Derien","",List.of(""),List.of(""))));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.medicalRecordReaderJsonData()).thenReturn(listDataJsonMedicalRecordForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        //person to add
        AddMedicalRecordDto newMedicalRecord = new AddMedicalRecordDto("Jean","Jacque","04/13/2000",List.of("aspirin"),List.of("pollen"));
        medicalRecordService.addMedicalRecord(newMedicalRecord);
        Assertions.assertTrue(listDataJsonMedicalRecordForMock.stream().anyMatch(medicalRecord -> medicalRecord.getLastName().equals(newMedicalRecord.getLastName()) && medicalRecord.getFirstName().equals(newMedicalRecord.getFirstName())));
    }
    @Test
    void addMedicalRecordWithException() throws MedicalRecordWriterException {
        //creation of the Exception chain
        IOException ioException = new IOException();
        MedicalRecordWriterException exceptionChain = new MedicalRecordWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());

        AddMedicalRecordDto newMedicalRecord = new AddMedicalRecordDto("Jean","Jacque","04/13/2000",List.of("aspirin"),List.of("pollen"));

        Throwable exception = assertThrows(AddMedicalRecordException.class, () -> medicalRecordService.addMedicalRecord(newMedicalRecord));
        assertEquals(exception.getCause().getClass(), MedicalRecordWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }
    @Test
    void addMedicalRecordWithMedicalRecordAlreadyException() throws MedicalRecordWriterException {
        //use  an initial list in order to find a correspondance between list and medical record wanted with exception AlreadyException Throw
        List<MedicalRecordModel> listDataJsonMedicalRecordForMock = new ArrayList<>(List.of(new MedicalRecordModel("Jean","Jacque","",List.of(""),List.of(""))));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.medicalRecordReaderJsonData()).thenReturn(listDataJsonMedicalRecordForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        //person to add and already exist to start exception
        AddMedicalRecordDto newMedicalRecord = new AddMedicalRecordDto("Jean","Jacque","04/13/2000",List.of("aspirin"),List.of("pollen"));
        Throwable exception = assertThrows(AddMedicalRecordException.class, () -> medicalRecordService.addMedicalRecord(newMedicalRecord));
        assertEquals(exception.getCause().getClass(), AlreadyExistMedicalRecordException.class);
    }
    @Test
    void updateMedicalRecordTest() throws MedicalRecordWriterException, UpdateMedicalRecordException {
        UpdateMedicalRecordDto updateMedicalRecord = new UpdateMedicalRecordDto("John","Boyd","",List.of(),List.of());
        List<MedicalRecordModel> listUpdatedMedicalRecord = new ArrayList<>();
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
    void updateMedicalRecordWithException() throws MedicalRecordWriterException {
        //use  an initial list in order to find a correspondance between list and medical record wanted without exception NotFound Throw
        List<MedicalRecordModel> listDataJsonMedicalRecordForMock = new ArrayList<>(List.of(new MedicalRecordModel("Jean","Jacque","",List.of(""),List.of(""))));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.medicalRecordReaderJsonData()).thenReturn(listDataJsonMedicalRecordForMock);
        //creation of the Exception chain
        IOException ioException = new IOException();
        MedicalRecordWriterException exceptionChain = new MedicalRecordWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());

        UpdateMedicalRecordDto updateMedicalRecord = new UpdateMedicalRecordDto("Jean","Jacque","04/13/2000",List.of("aspirin"),List.of("pollen"));

        Throwable exception = assertThrows(UpdateMedicalRecordException.class, () -> medicalRecordService.updateMedicalRecord(updateMedicalRecord));
        assertEquals(exception.getCause().getClass(), MedicalRecordWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }

    @Test
    void updateMedicalRecordWithNotFoundException() throws MedicalRecordWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        //person to add
        UpdateMedicalRecordDto updateMedicalRecord = new UpdateMedicalRecordDto("Arnault","Brie","04/13/2000",List.of("aspirin"),List.of("pollen"));
        Throwable exception = assertThrows(UpdateMedicalRecordException.class, () -> medicalRecordService.updateMedicalRecord(updateMedicalRecord));
        assertEquals(exception.getCause().getClass(), NotFoundMedicalRecordException.class);
    }

    @Test
    void deleteMedicalRecordTest() throws MedicalRecordWriterException, DeleteMedicalRecordException {
        DeleteMedicalRecordDto deleteMedicalRecord = new DeleteMedicalRecordDto("John","Boyd");
        //list to store modification of  list writer and check result
        List<MedicalRecordModel> listDeletedMedicalRecord = new ArrayList<>();
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

    @Test
    void deleteMedicalRecordWithException() throws MedicalRecordWriterException {
        //use  an initial list in order to find a correspondance between list and medical record wanted without exception NotFound Throw
        List<MedicalRecordModel> listDataJsonMedicalRecordForMock = new ArrayList<>(List.of(new MedicalRecordModel("Jean","Jacque","",List.of(""),List.of(""))));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.medicalRecordReaderJsonData()).thenReturn(listDataJsonMedicalRecordForMock);
        //creation of the Exception chain
        IOException ioException = new IOException();
        MedicalRecordWriterException exceptionChain = new MedicalRecordWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());

        DeleteMedicalRecordDto deleteMedicalRecord = new DeleteMedicalRecordDto("Jean","Jacque");

        Throwable exception = assertThrows(DeleteMedicalRecordException.class, () -> medicalRecordService.deleteMedicalRecord(deleteMedicalRecord));
        assertEquals(exception.getCause().getClass(), MedicalRecordWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }

    @Test
    void deleteMedicalRecordWithNotFoundException() throws MedicalRecordWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).medicalRecordWriterJsonData(anyList());
        //medical record to delete and not exist to start exception
        DeleteMedicalRecordDto deleteMedicalRecord = new DeleteMedicalRecordDto("Arnault","Brie");
        Throwable exception = assertThrows(DeleteMedicalRecordException.class, () -> medicalRecordService.deleteMedicalRecord(deleteMedicalRecord));
        assertEquals(exception.getCause().getClass(), NotFoundMedicalRecordException.class);
    }

}
