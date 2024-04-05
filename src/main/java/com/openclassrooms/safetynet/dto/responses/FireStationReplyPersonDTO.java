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
    public FireStationReplyPersonDTO() {
        //constructor for jackson serialisation
    }
    List<SubFireStationReplyPerson> persons;
    SubFireStationModelReplyForCount countPerson;
}
