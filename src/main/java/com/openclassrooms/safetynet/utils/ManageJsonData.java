package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ManageJsonData {
    ObjectMapper objectMapper = new ObjectMapper();
    File file = new File("src/main/resources/data.json");

    private Map<String, Object> globalReaderJsonData() throws IOException {
        TypeReference<Map<String, Object>> typeReferenceJsonData = new TypeReference<>() {
        };
       return  objectMapper.readValue(file, typeReferenceJsonData);
    }

    public List<FireStationModel> fireStationReaderJsonData() throws IOException {
        TypeReference<List<FireStationModel>> typeReferenceFireStationModel = new TypeReference<>() {};
        return objectMapper.convertValue(globalReaderJsonData().get("firestations"), typeReferenceFireStationModel);
    }
    public List<PersonModel> personReaderJsonData() throws IOException {
        TypeReference<List<PersonModel>> typeReferencePersonModel = new TypeReference<>() {};
        return objectMapper.convertValue(globalReaderJsonData().get("persons"), typeReferencePersonModel);
    }
    public List<MedicalRecordModel> medicalRecordReaderJsonData() throws IOException {
        TypeReference<List<MedicalRecordModel>> typeReferenceMedicalRecordModel = new TypeReference<>() {};
        return objectMapper.convertValue(globalReaderJsonData().get("medicalrecords"), typeReferenceMedicalRecordModel);
    }
    public FireStationModel testFireStationReaderByReturn(String address) throws IOException {
       return fireStationReaderJsonData().stream().filter(listDTO -> listDTO.getAddress().equals(address))
               .findFirst()
                .orElse(null);
    }
    public PersonModel testPersonReaderByReturn(String firstName, String lastName) throws IOException {
        return personReaderJsonData().stream().filter(listDTO -> listDTO.getFirstName().equals(firstName)
                && listDTO.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
    public MedicalRecordModel testMedicalRecordReaderByReturn(String firstName, String lastName) throws IOException {
        return medicalRecordReaderJsonData().stream().filter(listDTO -> listDTO.getFirstName().equals(firstName)
                        && listDTO.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
    //méthodes pour vérifier la class en renvoyer chaque modéle  person station medical record (pour me permettre de tester). couverture de test.
}
