package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * DTO for returning a list of phone numbers of residents covered by a specific fire station,
 * to facilitate emergency text message alerts.
 */
@Getter
@AllArgsConstructor
@ToString
public class PhoneAlertReplyPersonDTO {
    /**
     * Constructor for Jackson serialization.
     */
    public PhoneAlertReplyPersonDTO() {
        // Constructor for Jackson serialization
    }

    private List<String> phone;
}
