package com.openclassrooms.safetynet.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.models.FireStationModel;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.models.PersonModel;
import com.openclassrooms.safetynet.utils.OutputFormatIndentationJsonData;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openclassrooms.safetynet.exceptions.ManageJsonDataCustomException.*;

@Repository
public class ManageJsonData {
    private static final Logger logger = LogManager.getLogger(ManageJsonData.class);

    private final ObjectMapper objectMapper;

    @Value("${path.file}")
    String path;

    private File file;

    private Map<String, Object> dataJson = new HashMap<>();

    // Constructor to inject the ObjectMapper
    public ManageJsonData(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Method called after constructing the object to initialize the file and read the JSON data
    @PostConstruct
    public void init() throws InitException {
        try {
            if (path.isBlank() || path.isEmpty()) {
                throw new NullPointerException();
            }
            this.file = new File(path);
            this.dataJson = objectMapper.readValue(file, new TypeReference<>() {});
            logger.info("JSON data initialized successfully from file: {}", path);
            logger.debug("JSON data initialized successfully from file: {} and contains : {}", file.getAbsolutePath(), this.dataJson.toString());
        } catch (Exception e) {
            logger.error("Failed to initialize JSON data: {}", e.getMessage());
            throw new InitException(e);
        }
    }

    // Method to read fire station data from JSON data
    public List<FireStationModel> fireStationReaderJsonData() {
        logger.info("Reading fire station data from JSON");
        return objectMapper.convertValue(dataJson.get("firestations"), new TypeReference<>() {});
    }

    // Method to read people data from JSON data
    public List<PersonModel> personReaderJsonData() {
        logger.info("Reading person data from JSON");
        return objectMapper.convertValue(dataJson.get("persons"), new TypeReference<>() {});
    }

    // Method to read medical records from JSON data
    public List<MedicalRecordModel> medicalRecordReaderJsonData() {
        logger.info("Reading medical record data from JSON");
        return objectMapper.convertValue(dataJson.get("medicalrecords"), new TypeReference<>() {});
    }

    // Method to write people data to the JSON file
    public void personWriterJsonData(List<PersonModel> list) throws PersonWriterException {
        try {
            this.dataJson.replace("persons", list);
            objectMapper.writer(OutputFormatIndentationJsonData.getInstance()).writeValue(file, dataJson);
            logger.info("Person data written to JSON file");
        } catch (Exception e) {
            logger.error("Failed to write person data to JSON file: {}", e.getMessage());
            throw new PersonWriterException(e);
        }
    }

    // Method to write fire station data to the JSON file
    public void fireStationWriterJsonData(List<FireStationModel> list) throws FireStationWriterException {
        try {
            this.dataJson.replace("firestations", list);
            objectMapper.writer(OutputFormatIndentationJsonData.getInstance()).writeValue(file, dataJson);
            logger.info("Fire station data written to JSON file");
        } catch (Exception e) {
            logger.error("Failed to write fire station data to JSON file: {}", e.getMessage());
            throw new FireStationWriterException(e);
        }
    }

    // Method to write medical records to JSON file
    public void medicalRecordWriterJsonData(List<MedicalRecordModel> list) throws MedicalRecordWriterException {
        try {
            this.dataJson.replace("medicalrecords", list);
            objectMapper.writer(OutputFormatIndentationJsonData.getInstance()).writeValue(file, dataJson);
            logger.info("Medical record data written to JSON file");
        } catch (Exception e) {
            logger.error("Failed to write medical record data to JSON file: {}", e.getMessage());
            throw new MedicalRecordWriterException(e);
        }
    }
}