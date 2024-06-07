package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubPersonInfoReplyPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * DTO for returning personal information about individuals with a specific first and last name,
 * including their contact details and medical history.
 */
@Getter
@AllArgsConstructor
@ToString
public class PersonInfoReplyPersonDTO {
    /**
     * Constructor for Jackson serialization.
     */
    public PersonInfoReplyPersonDTO() {
        // Constructor for Jackson serialization
    }

    private List<SubPersonInfoReplyPerson> listPerson;
}
