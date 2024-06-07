package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Represents information about a specific address and the persons associated with it.
 */
@Getter
@AllArgsConstructor
@ToString
public class SubStationsReplyInfoPersonByAddress {
    /**
     * Constructor for Jackson serialization.
     */
    public SubStationsReplyInfoPersonByAddress() {
        // Constructor for Jackson serialization
    }

    private SubStationsReplyInfoAddress infoAddress;
    private List<SubStationsReplyInfoPerson> infoPerson;
}
