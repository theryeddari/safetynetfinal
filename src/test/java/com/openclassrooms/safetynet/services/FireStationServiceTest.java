package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddFireStationDto;
import com.openclassrooms.safetynet.dto.requests.DeleteFireStationDto;
import com.openclassrooms.safetynet.dto.requests.UpdateFireStationDto;
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
    void addStationTest() throws IOException {
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
    void updateFireStationTest() throws IOException {
        UpdateFireStationDto updateFireStation = new UpdateFireStationDto("112 Steppes Pl","3","5");
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
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
    void deleteFireStationByNumberTest() throws IOException {
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("","2");
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
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
    void deleteFireStationByAddressTest() throws IOException {
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("112 Steppes Pl","");
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
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
    void deleteFireStationByAddressAndNumberTest() throws IOException {
        DeleteFireStationDto deleteFireStation = new DeleteFireStationDto("748 Townings Dr","3");
        List<FireStationModel> listUpdatedFireStation = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).fireStationWriterJsonData(anyList());
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
}
