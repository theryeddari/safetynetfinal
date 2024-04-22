package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubStationsReplyInfoPersonByAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class StationsReplyPersonDTO {
    public StationsReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    List<SubStationsReplyInfoPersonByAddress> listHome;
}
