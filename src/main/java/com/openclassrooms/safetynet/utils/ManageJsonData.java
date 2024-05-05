package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ManageJsonData {
    ObjectMapper objectMapper;
    @Value("${path.file}")
    String path;
    File file;
    Map<String, Object> dataJson = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        this.file  = new File(path);
        globalReaderJsonData();
    }

    public ManageJsonData() {
        this.objectMapper = new ObjectMapper();
    }
    private void globalReaderJsonData() throws IOException {
        this.dataJson = objectMapper.readValue(file, new TypeReference<>() {});
    }

    public List<FireStationModel> fireStationReaderJsonData() {
        return objectMapper.convertValue(dataJson.get("firestations"), new TypeReference<>() {});
    }
    public  List<PersonModel>  personReaderJsonData() {
        return objectMapper.convertValue(dataJson.get("persons"), new TypeReference<>() {});
    }
    public List<MedicalRecordModel> medicalRecordReaderJsonData() {
        return objectMapper.convertValue(dataJson.get("medicalrecords"), new TypeReference<>() {});
    }
    public FireStationModel testFireStationReaderByReturn(String address) {
       return fireStationReaderJsonData().stream().filter(listDTO -> listDTO.getAddress().equals(address))
               .findFirst()
                .orElse(null);
    }
    public PersonModel testPersonReaderByReturn(String firstName, String lastName) {
        return personReaderJsonData().stream().filter(listDTO -> listDTO.getFirstName().equals(firstName)
                && listDTO.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
    public MedicalRecordModel testMedicalRecordReaderByReturn(String firstName, String lastName) {
        return medicalRecordReaderJsonData().stream().filter(listDTO -> listDTO.getFirstName().equals(firstName)
                        && listDTO.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

   public void personWriterJsonData(List<PersonModel> list) throws IOException {
        this.dataJson.replace("persons", list);
       objectMapper.writer(new OutputFormatIndentationJsonData()).writeValue(file,dataJson);
    }
}
