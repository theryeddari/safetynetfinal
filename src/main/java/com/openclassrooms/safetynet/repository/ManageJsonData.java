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
/**
 *  class which manages all write and read operations on the Json file which serves as the database
 */
@Repository
public class ManageJsonData {
    private static final Logger logger = LogManager.getLogger(ManageJsonData.class);

    private final ObjectMapper objectMapper;

    @Value("${path.file}")
    String path;

    File file;

    Map<String, Object> dataJson = new HashMap<>();

    /**
     * Constructor to inject the ObjectMapper.
     *
     * @param objectMapper the ObjectMapper to use for JSON operations
     */
    public ManageJsonData(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Initializes the file and reads the JSON data.
     * This method is called after the object is constructed.
     *
     * @throws InitException if initialization fails
     */
    @PostConstruct
    public void init() throws InitException {
        try {
            if (path.isBlank()) {
                throw new NullPointerException("Path is blank or empty");
            }
            this.file = new File(path);
            this.dataJson = objectMapper.readValue(file, new TypeReference<>() {
            });
            logger.info("JSON data initialized successfully from file: {}", path);
            logger.debug("JSON data initialized successfully from file: {} and contains: {}", file.getAbsolutePath(), this.dataJson);
        } catch (Exception e) {
            logger.error("Failed to initialize JSON data: {}", e.getMessage());
            throw new InitException(e);
        }
    }

    /**
     * Reads fire station data from JSON.
     *
     * @return a list of FireStationModel objects
     */
    public List<FireStationModel> fireStationReaderJsonData() {
        logger.info("Reading fire station data from JSON");
        return objectMapper.convertValue(dataJson.get("firestations"), new TypeReference<>() {
        });
    }

    /**
     * Reads person data from JSON.
     *
     * @return a list of PersonModel objects
     */
    public List<PersonModel> personReaderJsonData() {
        logger.info("Reading person data from JSON");
        return objectMapper.convertValue(dataJson.get("persons"), new TypeReference<>() {
        });
    }

    /**
     * Reads medical records from JSON.
     *
     * @return a list of MedicalRecordModel objects
     */
    public List<MedicalRecordModel> medicalRecordReaderJsonData() {
        logger.info("Reading medical record data from JSON");
        return objectMapper.convertValue(dataJson.get("medicalrecords"), new TypeReference<>() {
        });
    }

    /**
     * Writes person data to the JSON file.
     *
     * @param list the list of PersonModel to write
     * @throws PersonWriterException if writing fails
     */
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

    /**
     * Writes fire station data to the JSON file.
     *
     * @param list the list of FireStationModel to write
     * @throws FireStationWriterException if writing fails
     */
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

    /**
     * Writes medical records to the JSON file.
     *
     * @param list the list of MedicalRecordModel to write
     * @throws MedicalRecordWriterException if writing fails
     */
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
