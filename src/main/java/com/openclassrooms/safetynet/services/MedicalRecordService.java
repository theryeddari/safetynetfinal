package com.openclassrooms.safetynet.services;

import com.openclassrooms.safetynet.dto.requests.AddMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.DeleteMedicalRecordDto;
import com.openclassrooms.safetynet.dto.requests.UpdateMedicalRecordDto;
import com.openclassrooms.safetynet.models.MedicalRecordModel;
import com.openclassrooms.safetynet.repository.ManageJsonData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.openclassrooms.safetynet.exceptions.MedicalRecordCustomException.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *  class which manages all operations related to a person's medical record information (updating, deleting and adding)
 */
@Service
public class MedicalRecordService {
    private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);
    private final ManageJsonData manageJsonData;

    /**
     * Constructor to inject ManageJsonData dependency.
     *
     * @param manageJsonData the manageJsonData to be injected
     */
    public MedicalRecordService(ManageJsonData manageJsonData) {
        this.manageJsonData = manageJsonData;
    }

    /**
     * Method to add a new medical record.
     *
     * @param newMedicalRecord the medical record to add
     * @throws AddMedicalRecordException if an error occurs during the addition
     */
    public void addMedicalRecord(AddMedicalRecordDto newMedicalRecord) throws AddMedicalRecordException {
        logger.info("Adding new medical record for {} {}", newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
        try {
            List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
            logger.debug("Existing medical records: {}", listMedicalRecordExisting);

            // Check if the medical record already exists
            if (listMedicalRecordExisting.stream().noneMatch(medicalRecordExist ->
                    medicalRecordExist.getLastName().equals(newMedicalRecord.getLastName()) &&
                            medicalRecordExist.getFirstName().equals(newMedicalRecord.getFirstName()))) {

                // Add the new medical record if it does not exist
                listMedicalRecordExisting.add(new MedicalRecordModel(
                        newMedicalRecord.getFirstName(),
                        newMedicalRecord.getLastName(),
                        newMedicalRecord.getBirthdate(),
                        newMedicalRecord.getMedications(),
                        newMedicalRecord.getAllergies()));
                logger.info("New medical record added successfully.");
            } else {
                throw new AlreadyExistMedicalRecordException();
            }

            // Write the updated list of medical records back to the JSON file
            manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
        } catch (Exception e) {
            logger.error("Error adding medical record: ", e);
            throw new AddMedicalRecordException(e);
        }
    }

    /**
     * Method to update an existing medical record.
     *
     * @param updateMedicalRecord the medical record to update
     * @throws UpdateMedicalRecordException if an error occurs during the update
     */
    public void updateMedicalRecord(UpdateMedicalRecordDto updateMedicalRecord) throws UpdateMedicalRecordException {
        logger.info("Updating medical record for {} {}", updateMedicalRecord.getFirstName(), updateMedicalRecord.getLastName());
        try {
            List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
            logger.debug("Existing medical records: {}", listMedicalRecordExisting);

            // Get the reference to the filtered medical record object
            Optional<MedicalRecordModel> wantedMedicalRecordUpdate = listMedicalRecordExisting.stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equals(updateMedicalRecord.getFirstName()) &&
                            medicalRecord.getLastName().equals(updateMedicalRecord.getLastName()))
                    .findFirst();

            if (wantedMedicalRecordUpdate.isEmpty()) {
                throw new NotFoundMedicalRecordException();
            }

            // If there is a reference, access the object and modify its properties
            wantedMedicalRecordUpdate.ifPresent(modifyMedicalRecord -> {
                modifyMedicalRecord.setBirthdate(updateMedicalRecord.getBirthdate());
                modifyMedicalRecord.setMedications(updateMedicalRecord.getMedications());
                modifyMedicalRecord.setAllergies(updateMedicalRecord.getAllergies());
            });

            // Write the updated list of medical records back to the JSON file
            manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
            logger.info("Medical record updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating medical record: ", e);
            throw new UpdateMedicalRecordException(e);
        }
    }

    /**
     * Method to delete an existing medical record.
     *
     * @param deleteMedicalRecord the medical record to delete
     * @throws DeleteMedicalRecordException if an error occurs during the deletion
     */
    public void deleteMedicalRecord(DeleteMedicalRecordDto deleteMedicalRecord) throws DeleteMedicalRecordException {
        logger.info("Deleting medical record for {} {}", deleteMedicalRecord.getFirstName(), deleteMedicalRecord.getLastName());
        try {
            List<MedicalRecordModel> listMedicalRecordExisting = manageJsonData.medicalRecordReaderJsonData();
            logger.debug("Existing medical records: {}", listMedicalRecordExisting);

            // Remove the medical record if it matches the given first name and last name
            if (listMedicalRecordExisting.removeIf(medicalRecord ->
                medicalRecord.getFirstName().equals(deleteMedicalRecord.getFirstName()) &&
                medicalRecord.getLastName().equals(deleteMedicalRecord.getLastName()))) {

                // Write the updated list of medical records back to the JSON file
                manageJsonData.medicalRecordWriterJsonData(listMedicalRecordExisting);
                logger.info("Medical record deleted successfully.");
            } else {
                throw new NotFoundMedicalRecordException();
            }
        } catch (Exception e) {
            logger.error("Error deleting medical record: ", e);
            throw new DeleteMedicalRecordException(e);
        }
    }
}


