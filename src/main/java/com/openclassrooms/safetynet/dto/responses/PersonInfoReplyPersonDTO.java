package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubPersonInfoReplyPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class PersonInfoReplyPersonDTO {
    public PersonInfoReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    List<SubPersonInfoReplyPerson> listPerson;
}
