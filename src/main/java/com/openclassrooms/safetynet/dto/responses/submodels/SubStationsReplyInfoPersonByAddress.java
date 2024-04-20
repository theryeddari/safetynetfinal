package com.openclassrooms.safetynet.dto.responses.submodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class SubStationsReplyInfoPersonByAddress {
    public SubStationsReplyInfoPersonByAddress(){
        //constructor for jackson serialisation
    }
    SubStationsReplyInfoAddress infoAddress;
    List<SubStationsReplyInfoPerson> infoPerson;
}
