package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddOrUpdateFireStationDto;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.utils.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        AddOrUpdateFireStationDto newFireStation = new AddOrUpdateFireStationDto("10 rue des frites","20");
        fireStationService.addFireStation(newFireStation);
        Assertions.assertTrue(listDataJsonFireStationForMock.stream().anyMatch(fireStation ->
                fireStation.getStation().equals(newFireStation.getStation())
                        && fireStation.getAddress().equals(newFireStation.getAddress())));
    }
}
