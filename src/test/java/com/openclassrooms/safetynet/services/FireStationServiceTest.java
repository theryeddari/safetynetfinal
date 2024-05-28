package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.*;
import com.openclassrooms.safetynet.exceptions.FireStationCustomException;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.safetynet.exceptions.FireStationCustomException.*;
import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class FireStationServiceTest {
    @Autowired
    FireStationService fireStationService;
    @SpyBean
    ManageJsonData manageJsonDataSpy;

    FireStationServiceTest(){
    }

    @Test
    void addStationTest() throws FireStationWriterException, AddFireStationException {
        //use of a modifiable table to store an initial list which can be dynamically modified by the method tested thanks to the reference
        List<FireStationModel> listDataJsonFireStationForMock = new ArrayList<>(List.of(new FireStationModel("5 rue des arbres","5")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.fireStationReaderJsonData()).thenReturn(listDataJsonFireStationForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        AddFireStationDto newFireStation = new AddFireStationDto("10 rue des frites","20");
        fireStationService.addFireStation(newFireStation);
        Assertions.assertTrue(listDataJsonFireStationForMock.stream().anyMatch(fireStation ->
                fireStation.getStation().equals(newFireStation.getStation())
                        && fireStation.getAddress().equals(newFireStation.getAddress())));
    }

    @Test
    void addStationWithException() throws FireStationWriterException {
        //creation of the Exception chain
        IOException ioException = new IOException();
        FireStationWriterException exceptionChain = new FireStationWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        AddFireStationDto newFireStation = new AddFireStationDto("Au rue du feu","3");

        Throwable exception = assertThrows(FireStationCustomException.class, () -> fireStationService.addFireStation(newFireStation));
        assertEquals(exception.getCause().getClass(), FireStationWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }

    @Test
    void addStationWithFireStationExistAlreadyException() throws FireStationWriterException {
        //use  an initial list in order to find a correspondance between list and fire station wanted for start exception AlreadyException
        List<FireStationModel> listDataJsonFireStationForMock = new ArrayList<>(List.of(new FireStationModel("5 rue des arbres","5")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.fireStationReaderJsonData()).thenReturn(listDataJsonFireStationForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        AddFireStationDto newFireStation = new AddFireStationDto("5 rue des arbres","5");
        Throwable exception = assertThrows(AddFireStationException.class, () -> fireStationService.addFireStation(newFireStation));
        assertEquals(exception.getCause().getClass(), AlreadyExistFireStationException.class);
    }

    @Test
    void updateFireStationTest() throws FireStationWriterException, UpdateFireStationException {
        UpdateFireStationDto updateFireStation = new UpdateFireStationDto("112 Steppes Pl","3","5");
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doAnswer(invocation -> {
            listUpdatedFireStation.addAll(invocation.getArgument(0));
            return null;
        }).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        fireStationService.updateFireStation(updateFireStation);
        Assertions.assertTrue(listUpdatedFireStation.stream().anyMatch(fireStation ->
                fireStation.getStation().equals(updateFireStation.getNewNumberStation())
                    && fireStation.getAddress().equals(updateFireStation.getAddress())
        ));
    }

    @Test
    void updateFireStationWithException() throws FireStationWriterException {
        //use  an initial list in order to find a correspondance between list and medical record wanted without exception NotFound Throw
        List<FireStationModel> listDataJsonFireStationForMock = new ArrayList<>(List.of(new FireStationModel("5 rue des arbres","5")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.fireStationReaderJsonData()).thenReturn(listDataJsonFireStationForMock);

        //creation of the Exception chain
        IOException ioException = new IOException();
        FireStationWriterException exceptionChain = new FireStationWriterException(ioException);

        doThrow(exceptionChain).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        UpdateFireStationDto updateFireStation = new UpdateFireStationDto("5 rue des arbres","5","2");

        Throwable exception = assertThrows(UpdateFireStationException.class, () -> fireStationService.updateFireStation(updateFireStation));
        assertEquals(exception.getCause().getClass(), FireStationWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());

    }

    @Test
    void updateFireStationWithNotFoundException() throws FireStationWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());

        UpdateFireStationDto updateFireStation = new UpdateFireStationDto("5 rue des arbres","5","2");

        Throwable exception = assertThrows(FireStationCustomException.class, () -> fireStationService.updateFireStation(updateFireStation));
        assertEquals(exception.getCause().getClass(), NotFoundFireStationException.class);
    }

    @Test
    void deleteFireStationByNumberTest() throws FireStationWriterException, DeleteFireStationException {
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("","2");
        //list to store modification of  list writer and check result
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doAnswer(invocation -> {
            listUpdatedFireStation.addAll(invocation.getArgument(0));
            return null;
        }).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        fireStationService.deleteFireStation(deleteFireStation);
        Assertions.assertTrue(listUpdatedFireStation.stream().noneMatch(fireStation ->
                fireStation.getStation().equals(deleteFireStation.getStation()))
        );
    }
    @Test
    void deleteFireStationByAddressTest() throws FireStationWriterException, DeleteFireStationException {
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("112 Steppes Pl","");
        //list to store modification of  list writer and check result
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doAnswer(invocation -> {
            listUpdatedFireStation.addAll(invocation.getArgument(0));
            return null;
        }).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        fireStationService.deleteFireStation(deleteFireStation);
        Assertions.assertTrue(listUpdatedFireStation.stream().noneMatch(fireStation ->
                fireStation.getAddress().equals(deleteFireStation.getAddress()))
        );
    }
    @Test
    void deleteFireStationByAddressAndNumberTest() throws FireStationWriterException, DeleteFireStationException {
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("748 Townings Dr","3");
        //list to store modification of  list writer and check result
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doAnswer(invocation -> {
            listUpdatedFireStation.addAll(invocation.getArgument(0));
            return null;
        }).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        fireStationService.deleteFireStation(deleteFireStation);
        Assertions.assertTrue(listUpdatedFireStation.stream().noneMatch(fireStation ->
                fireStation.getAddress().equals(deleteFireStation.getAddress())
                        && fireStation.getStation().equals(deleteFireStation.getStation()))
        );
    }
    @Test
    void deleteFireStationWithException() throws FireStationWriterException {
        //use  an initial list in order to find a correspondance between list and medical record wanted without exception NotFound Throw
        List<FireStationModel> listDataJsonFireStationForMock = new ArrayList<>(List.of(new FireStationModel("5 rue des arbres","5")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.fireStationReaderJsonData()).thenReturn(listDataJsonFireStationForMock);
        //creation of the Exception chain
        IOException ioException = new IOException();
        FireStationWriterException exceptionChain = new FireStationWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).fireStationWriterJsonData(anyList());

        DeleteFireStationDto deleteFireStationDto = new DeleteFireStationDto("5 rue des arbres","5");

        Throwable exception = assertThrows(DeleteFireStationException.class, () -> fireStationService.deleteFireStation(deleteFireStationDto));
        assertEquals(exception.getCause().getClass(), FireStationWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }
    @Test
    void deleteFireStationWithNotFoundException() throws FireStationWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        //fire station to delete and not exist to start exception
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("2 rue des pompiers","2");
        Throwable exception = assertThrows(DeleteFireStationException.class, () -> fireStationService.deleteFireStation(deleteFireStation));
        assertEquals(exception.getCause().getClass(), NotFoundFireStationException.class);
    }
    @Test
    void deleteFireStationWithNoneArgumentFilledException() throws FireStationWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
        //fire station to delete and not exist to start exception
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("","");
        Throwable exception = assertThrows(DeleteFireStationException.class, () -> fireStationService.deleteFireStation(deleteFireStation));
        assertEquals(exception.getCause().getClass(), MissingFireStationArgument.class);
    }
}
