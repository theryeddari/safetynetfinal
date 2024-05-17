package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubFireStationModelReplyForCount;
import com.openclassrooms.safetynet.dto.responses.submodels.SubFireStationReplyPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class FireStationReplyPersonDTO {
    /**
     * DTO for returning a list of residents covered by a specific fire station,
     * including their personal details and a count of adults and children.
     */
    public FireStationReplyPersonDTO() {
        //constructor for jackson serialisation
    }
    List<SubFireStationReplyPerson> persons;
    SubFireStationModelReplyForCount countPerson;
}
