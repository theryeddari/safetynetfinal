package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubStationsReplyInfoPersonByAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * DTO for returning a list of households served by specified fire stations,
 * grouped by address and including residents' personal details and medical history.
 */
@Getter
@AllArgsConstructor
@ToString
public class StationsReplyPersonDTO {
    /**
     * Constructor for Jackson serialization.
     */
    public StationsReplyPersonDTO() {
        // Constructor for Jackson serialization
    }

    private List<SubStationsReplyInfoPersonByAddress> listHome;
}
