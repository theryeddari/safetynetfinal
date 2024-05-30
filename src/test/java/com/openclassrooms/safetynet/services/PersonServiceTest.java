package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.*;
import com.openclassrooms.safetynet.dto.responses.*;
import com.openclassrooms.safetynet.dto.responses.submodels.*;
import com.openclassrooms.safetynet.exceptions.PersonCustomException;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;
import static com.openclassrooms.safetynet.exceptions.PersonCustomException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
@SpringBootTest
class PersonServiceTest {
    @Autowired
    PersonService personService;
    @SpyBean
    ManageJsonData manageJsonDataSpy;

    PersonServiceTest() {
    }

    @Test
    void fireStationReplyTest() throws FireStationResponseException {
        FireStationReplyPersonDTO reply = personService.fireStationReply("2");
        SubFireStationModelReplyForCount countExcepted = new SubFireStationModelReplyForCount("4", "1");
        SubFireStationReplyPerson personExcepted = new SubFireStationReplyPerson("Jonanathan","Marrack","29 15th St","Culver","97451","841-874-6513");
        Assertions.assertEquals(countExcepted.toString(),reply.getCountPerson().toString());
        Assertions.assertEquals(List.of(personExcepted).toString(), reply.getPersons().stream().filter(listPerson -> listPerson.getFirstName().equals("Jonanathan") && listPerson.getLastName().equals("Marrack")).toList().toString());
    }

    @Test
    void fireStationReplyWithException() {
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        FireStationResponseException exceptionChain = new FireStationResponseException(runtimeException);
        when(manageJsonDataSpy.fireStationReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(FireStationResponseException.class, () -> personService.fireStationReply("2"));
        assertEquals(RuntimeException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(),exception.getMessage());
    }

    @Test
    void childAlertReplyTest() throws ChildAlertResponseException {
        ChildAlertReplyPersonDTO reply = personService.childAlertReply("1509 Culver St");

        SubChildAlertReplyChildren childExcepted = new SubChildAlertReplyChildren("Tenley","Boyd","02/18/2012");
        SubChildAlertReplyAdultFamily adultExcepted = new SubChildAlertReplyAdultFamily("John","Boyd");

        Assertions.assertEquals(List.of(adultExcepted).toString(),reply.getIdentityFamily().stream().filter(listAdult -> listAdult.getFirstName().equals("John") && listAdult.getLastName().equals("Boyd")).toList().toString());
        Assertions.assertEquals(List.of(childExcepted).toString(),reply.getChildren().stream().filter(listChildren -> listChildren.getFirstName().equals("Tenley") && listChildren.getLastName().equals("Boyd")).toList().toString());
    }

    @Test
    void childAlertReplyWithException() {
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        FactoringConcatStreamMethodException factoringConcatStreamMethodException = new FactoringConcatStreamMethodException(runtimeException);
        ChildAlertResponseException exceptionChain = new ChildAlertResponseException(factoringConcatStreamMethodException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(ChildAlertResponseException.class, () -> personService.childAlertReply("rue non"));
        assertEquals(FactoringConcatStreamMethodException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(),exception.getMessage());
    }

    @Test
    void phoneAlertReplyTest() throws PhoneAlertResponseException {
        PhoneAlertReplyPersonDTO reply = personService.phoneAlert("1");
        PhoneAlertReplyPersonDTO phoneExcepted = new PhoneAlertReplyPersonDTO(List.of("841-874-7462"));

        Assertions.assertEquals(phoneExcepted.getPhone(),reply.getPhone().stream().filter(listPhone -> listPhone.contains("841-874-7462")).toList());
    }

    @Test
    void phoneAlertReplyWithException(){
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        PhoneAlertResponseException exceptionChain = new PhoneAlertResponseException(runtimeException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(PhoneAlertResponseException.class, () -> personService.phoneAlert("2"));
        assertEquals(RuntimeException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(),exception.getMessage());
    }

    @Test
    void fireTest() throws FireResponseException {
        FireReplyPersonDTO reply = personService.fire("1509 Culver St");
        SubFireReplyReplyInfoPerson personInfoExcepted = new SubFireReplyReplyInfoPerson("Boyd","841-874-6512","03/06/1984", List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan"));
        Assertions.assertEquals(List.of(personInfoExcepted).toString(),reply.getInfoPerson().stream().filter(listpersonInfo -> listpersonInfo.getLastName().equals("Boyd") && listpersonInfo.getBirthdate().equals("03/06/1984")).toList().toString());
    }

    @Test
    void fireTestWithException(){
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        FactoringConcatStreamMethodException factoringConcatStreamMethodException = new FactoringConcatStreamMethodException(runtimeException);
        FireResponseException exceptionChain = new FireResponseException(factoringConcatStreamMethodException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(FireResponseException.class, () -> personService.fire("test"));
        assertEquals(FactoringConcatStreamMethodException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(),exception.getMessage());
    }

    @Test
    void floodStationsTest() throws FloodStationResponseException {
       StationsReplyPersonDTO reply = personService.floodStation(new ArrayList<>(Arrays.asList("3", "2")));
       SubStationsReplyInfoPerson preExcepted1InfoPerson = new SubStationsReplyInfoPerson("Cadigan","841-874-7458","08/06/1945", List.of("tradoxidine:400mg"),List.of(""));
       SubStationsReplyInfoAddress preExcepted1InfoAddress = new SubStationsReplyInfoAddress("951 LoneTree Rd","Culver","97451");
        SubStationsReplyInfoPerson preExcepted2InfoPerson = new SubStationsReplyInfoPerson("Boyd","841-874-6512","03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"),List.of("nillacilan"));
        SubStationsReplyInfoAddress preExcepted2InfoAddress = new SubStationsReplyInfoAddress("1509 Culver St","Culver","97451");

        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoAddress().toString().equals(preExcepted1InfoAddress.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoPerson().toString().contains(preExcepted1InfoPerson.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoAddress().toString().equals(preExcepted2InfoAddress.toString())));
        Assertions.assertTrue(reply.getListHome().stream().anyMatch(infoAddress -> infoAddress.getInfoPerson().toString().contains(preExcepted2InfoPerson.toString())));
    }

    @Test
    void floodStationsWithException(){
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        FactoringConcatStreamMethodException factoringConcatStreamMethodException = new FactoringConcatStreamMethodException(runtimeException);
        FloodStationResponseException exceptionChain = new FloodStationResponseException(factoringConcatStreamMethodException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(FloodStationResponseException.class, () -> personService.floodStation(List.of("1","2")));
        assertEquals(FactoringConcatStreamMethodException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(),exception.getMessage());
    }

    @Test
    void personInfoTest() throws PersonInfoResponseException {
        PersonInfoReplyPersonDTO reply = personService.personInfo("John", "Boyd");
        List<SubPersonInfoReplyPerson> personInfoExcepted =  List.of( new SubPersonInfoReplyPerson("Boyd","1509 Culver St","Culver","97451","jaboyd@email.com","03/06/1984",List.of("aznol:350mg, hydrapermazol:100mg"), List.of("nillacilan")));
        Assertions.assertTrue(reply.getListPerson().toString().contains(personInfoExcepted.toString()));
    }

    @Test
    void personInfoException() {
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        FactoringConcatStreamMethodException factoringConcatStreamMethodException = new FactoringConcatStreamMethodException(runtimeException);
        PersonInfoResponseException exceptionChain = new PersonInfoResponseException(factoringConcatStreamMethodException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(PersonInfoResponseException.class, () -> personService.personInfo("Paul","Afond"));
        assertEquals(FactoringConcatStreamMethodException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(), exception.getMessage());
    }

    @Test
    void communityEmailTest() throws CommunityEmailException {
        CommunityEmailReplyPersonDTO reply = personService.communityEmail("Culver");
        String exceptedMail = "jaboyd@email.com";
        Assertions.assertTrue(reply.getMail().contains(exceptedMail));
    }

    @Test
    void communityEmailWithException() {
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException();
        FireResponseException exceptionChain = new FireResponseException(runtimeException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(RuntimeException.class);

        Throwable exception = assertThrows(CommunityEmailException.class, () -> personService.communityEmail("test"));
        assertEquals(RuntimeException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(),exception.getMessage());
    }

    @Test
    void factoringConcatStreamTest() throws PersonInfoResponseException {
        List<PersonModel> listPersonSpy = new ArrayList<>(List.of(new PersonModel("thery","eddari", "","","","","")));
        List<MedicalRecordModel> listMedicalRecordSpy = new ArrayList<>(List.of(new MedicalRecordModel("thery","eddari","",List.of(),List.of())));

        when(manageJsonDataSpy.personReaderJsonData()).thenReturn(listPersonSpy);
        when(manageJsonDataSpy.medicalRecordReaderJsonData()).thenReturn(listMedicalRecordSpy);

        //use another method who use this one to check good execution because this method is private
        PersonInfoReplyPersonDTO returnExcepted = personService.personInfo("thery","eddari");
        SubPersonInfoReplyPerson infoExcepted = new SubPersonInfoReplyPerson("eddari","","","","","",List.of(),List.of()) ;
        Assertions.assertEquals(returnExcepted.getListPerson().getFirst().toString(),infoExcepted.toString());
    }

    @Test
    void factoringConcatStreamWithException() {
        //creation of the Exception chain
        RuntimeException runtimeException = new RuntimeException("transmission des infos");
        FactoringConcatStreamMethodException factoringConcatStreamMethodException = new FactoringConcatStreamMethodException(runtimeException);
        PersonInfoResponseException exceptionChain = new PersonInfoResponseException(factoringConcatStreamMethodException);
        when(manageJsonDataSpy.personReaderJsonData()).thenThrow(runtimeException);

        Throwable exception = assertThrows(PersonInfoResponseException.class, () -> personService.personInfo("Paul","Afond"));
        assertEquals(FactoringConcatStreamMethodException.class, exception.getCause().getClass());
        assertEquals(exceptionChain.getMessage(), exception.getMessage());
    }

    @Test
    void addPersonTest() throws PersonWriterException, AddPersonException {
        //use of a modifiable table to store an initial list which can be dynamically modified by the method tested thanks to the reference
        List<PersonModel> listDataJsonPersonForMock = new ArrayList<>(List.of(new PersonModel("Jean", "Jacque", "bla", "bla", "", "", "")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.personReaderJsonData()).thenReturn(listDataJsonPersonForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        //person to add
        AddPersonDto newPerson = new AddPersonDto("thery","eddari","","","","","");
        personService.addPerson(newPerson);
        Assertions.assertTrue(listDataJsonPersonForMock.stream().anyMatch(person -> person.getLastName().equals(newPerson.getLastName()) && person.getFirstName().equals(newPerson.getFirstName())));
    }
    @Test
    void addPersonWithException() throws PersonWriterException {
        //creation of the Exception chain
        IOException ioException = new IOException();
        PersonWriterException exceptionChain = new PersonWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).personWriterJsonData(anyList());

        AddPersonDto newPerson = new AddPersonDto("Jean","Jacque","","","","","");

        Throwable exception = assertThrows(AddPersonException.class, () -> personService.addPerson(newPerson));
        assertEquals(exception.getCause().getClass(), PersonWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }
    @Test
    void addPersonWithMedicalRecordAlreadyException() throws PersonWriterException {
        //use  an initial list in order to find a correspondance between list and person wanted with exception AlreadyException Throw
        List<PersonModel> listDataJsonPersonForMock = new ArrayList<>(List.of(new PersonModel("Jean","Jacque","","","","","")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.personReaderJsonData()).thenReturn(listDataJsonPersonForMock);
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        //person to add and already exist to start exception
        AddPersonDto newPerson = new AddPersonDto("Jean","Jacque","","","","","");
        Throwable exception = assertThrows(AddPersonException.class, () -> personService.addPerson(newPerson));
        assertEquals(exception.getCause().getClass(), PersonCustomException.AlreadyExistPersonException.class);
    }


    @Test
    void updatePersonTest() throws PersonWriterException, UpdatePersonException {
        UpdatePersonDto updatePerson = new UpdatePersonDto("John","Boyd","","","","","");
        List<PersonModel> listUpdatedPersons = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        doAnswer(invocation -> {
            listUpdatedPersons.addAll(invocation.getArgument(0));
            return null;
                }
        ).when(manageJsonDataSpy).personWriterJsonData(anyList());
        personService.updatePerson(updatePerson);
        System.out.println(listUpdatedPersons);
        Assertions.assertTrue(listUpdatedPersons.stream().anyMatch(person ->
                person.getFirstName().equals(updatePerson.getFirstName())
                        && person.getLastName().equals(updatePerson.getLastName())
                        && person.getEmail().equals(updatePerson.getEmail())
                        && person.getAddress().equals(updatePerson.getAddress())
                        && person.getPhone().equals(updatePerson.getPhone())
                        && person.getCity().equals(updatePerson.getCity())
                        && person.getZip().equals(updatePerson.getZip())
        ));
    }

    @Test
    void updatePersonWithException() throws PersonWriterException {
        //use  an initial list in order to find a correspondance between list and person wanted without exception NotFound Throw
        List<PersonModel> listDataJsonPersonForMock = new ArrayList<>(List.of(new PersonModel("Jean","Jacque","","","","","")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.personReaderJsonData()).thenReturn(listDataJsonPersonForMock);
        //creation of the Exception chain
        IOException ioException = new IOException();
        PersonWriterException exceptionChain = new PersonWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).personWriterJsonData(anyList());

        UpdatePersonDto updatePerson = new UpdatePersonDto("Jean","Jacque","","","","","");

        Throwable exception = assertThrows(UpdatePersonException.class, () -> personService.updatePerson(updatePerson));
        assertEquals(exception.getCause().getClass(), PersonWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }

    @Test
    void updateMedicalRecordWithNotFoundException() throws PersonWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        //person to add
        UpdatePersonDto updatePerson = new UpdatePersonDto("Jean","Jacque","","","","","");
        Throwable exception = assertThrows(UpdatePersonException.class, () -> personService.updatePerson(updatePerson));
        assertEquals(exception.getCause().getClass(), NotFoundPersonException.class);
    }

    @Test
    void deletePersonTest() throws PersonWriterException, DeletePersonException {
        DeletePersonDto deletePerson = new DeletePersonDto("John","Boyd");
        List<PersonModel> listUpdatedPersons = new ArrayList<>();
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        doAnswer(invocation -> {
                    listUpdatedPersons.addAll(invocation.getArgument(0));
                    return null;
                }
        ).when(manageJsonDataSpy).personWriterJsonData(anyList());
        personService.deletePerson(deletePerson);
        System.out.println(listUpdatedPersons);
        Assertions.assertTrue(listUpdatedPersons.stream().noneMatch(person ->
                person.getLastName().equals(deletePerson.getLastName()) && person.getFirstName().equals(deletePerson.getFirstName()))
        );
    }

    @Test
    void deletePersonWithException() throws PersonWriterException {
        //use  an initial list in order to find a correspondance between list and person wanted without exception NotFound Throw
        List<PersonModel> listDataJsonPersonForMock = new ArrayList<>(List.of(new PersonModel("Jean","Jacque","","","","","")));
        //stub the mock method to introduce our list into the method
        when(manageJsonDataSpy.personReaderJsonData()).thenReturn(listDataJsonPersonForMock);
        //creation of the Exception chain
        IOException ioException = new IOException();
        PersonWriterException exceptionChain = new PersonWriterException(ioException);

        //stub the mock method to throw ExceptionChain
        doThrow(exceptionChain).when(manageJsonDataSpy).personWriterJsonData(anyList());

        DeletePersonDto deletePerson = new DeletePersonDto("Jean","Jacque");

        Throwable exception = assertThrows(DeletePersonException.class, () -> personService.deletePerson(deletePerson));
        assertEquals(exception.getCause().getClass(), PersonWriterException.class);
        assertEquals(exception.getCause().getMessage(), exceptionChain.getMessage());
    }

    @Test
    void deletePersonWithNotFoundException() throws PersonWriterException {
        //indicates not to launch the write method on the file
        doNothing().when(manageJsonDataSpy).personWriterJsonData(anyList());
        //person to delete and not exist to start exception
        DeletePersonDto deletePerson = new DeletePersonDto("Arnault","Brie");
        Throwable exception = assertThrows(DeletePersonException.class, () -> personService.deletePerson(deletePerson));
        assertEquals(exception.getCause().getClass(), NotFoundPersonException.class);
    }
}