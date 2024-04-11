package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubFireReplyReplyInfoPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class FireReplyPersonDTO {
    public FireReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    List<SubFireReplyReplyInfoPerson> infoPerson;
    String stationNumber;
}
