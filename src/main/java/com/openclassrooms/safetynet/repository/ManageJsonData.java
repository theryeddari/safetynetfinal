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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ManageJsonData {
    ObjectMapper objectMapper;

    @Value("${path.file}")
    String path;

    File file;

    Map<String, Object> dataJson = new HashMap<>();

    // Method called after constructing the object to initialize the file and read the JSON data, because Spring annotations are read last (avoid an empty path)
    @PostConstruct
    public void init() throws IOException {
        this.file = new File(path);
        globalReaderJsonData();
    }

    // Constructor to inject the ObjectMapper
    public ManageJsonData(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Method to read global JSON data from file
    private void globalReaderJsonData() throws IOException {
        this.dataJson = objectMapper.readValue(file, new TypeReference<>() {
        });
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
    public void personWriterJsonData(List<PersonModel> list) throws IOException {
        this.dataJson.replace("persons", list);
        objectMapper.writer(new OutputFormatIndentationJsonData()).writeValue(file, dataJson);
    }

    // Method to write fire station data to the JSON file
    public void fireStationWriterJsonData(List<FireStationModel> list) throws IOException {
        this.dataJson.replace("firestations", list);
        objectMapper.writer(new OutputFormatIndentationJsonData()).writeValue(file, dataJson);
    }

    // Method to write medical records to JSON file
    public void medicalRecordWriterJsonData(List<MedicalRecordModel> list) throws IOException {
        this.dataJson.replace("medicalrecords", list);
        objectMapper.writer(new OutputFormatIndentationJsonData()).writeValue(file, dataJson);
    }
}
