package com.openclassrooms.safetynet.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.utils.OutputFormatIndentationJsonData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;

@Repository
public class ManageJsonData {
    ObjectMapper objectMapper;

    @Value("${path.file}")
    String path;

    File file;

    Map<String, Object> dataJson = new HashMap<>();

    // Method called after constructing the object to initialize the file and read the JSON data, because Spring annotations are read last (avoid an empty path)
    @PostConstruct
    public void init() throws  InitException {
        try{
        if(path.isBlank() || path.isEmpty()) {throw new NullPointerException();}
        this.file = new File(path);
        this.dataJson = objectMapper.readValue(file, new TypeReference<>() {});
    }catch(Exception e){
            throw new InitException(e);
        }
    }
    // Constructor to inject the ObjectMapper
    public ManageJsonData(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Method to read fire station data from JSON data
    public List<FireStationModel> fireStationReaderJsonData() {
        return objectMapper.convertValue(dataJson.get("firestations"), new TypeReference<>() {
        });
    }

    // Method to read people data from JSON data
    public List<PersonModel> personReaderJsonData() {
        return objectMapper.convertValue(dataJson.get("persons"), new TypeReference<>() {
        });
    }

    // Method to read medical records from JSON data
    public List<MedicalRecordModel> medicalRecordReaderJsonData() {
        return objectMapper.convertValue(dataJson.get("medicalrecords"), new TypeReference<>() {
        });
    }


    // Method to write people data to the JSON file
    public void personWriterJsonData(List<PersonModel> list) throws PersonWriterException {
        try {
        this.dataJson.replace("persons", list);
        objectMapper.writer(OutputFormatIndentationJsonData.getInstance()).writeValue(file, dataJson);
    }catch (Exception e){
            throw new PersonWriterException(e);
        }
    }

    // Method to write fire station data to the JSON file
    public void fireStationWriterJsonData(List<FireStationModel> list) throws FireStationWriterException {
        try{
        this.dataJson.replace("firestations", list);
        objectMapper.writer(OutputFormatIndentationJsonData.getInstance()).writeValue(file, dataJson);
    }catch (Exception e){
    throw new FireStationWriterException(e);
        }
    }

    // Method to write medical records to JSON file
    public void medicalRecordWriterJsonData(List<MedicalRecordModel> list) throws MedicalRecordWriterException {
        try {
        this.dataJson.replace("medicalrecords", list);
        objectMapper.writer(OutputFormatIndentationJsonData.getInstance()).writeValue(file, dataJson);
    }catch (Exception e){
        throw new MedicalRecordWriterException(e);
        }
    }
}
