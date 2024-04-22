package com.openclassrooms.safetynet.dto.responses;

import com.openclassrooms.safetynet.dto.responses.submodels.SubChildAlertReplyAdultFamily;
import com.openclassrooms.safetynet.dto.responses.submodels.SubChildAlertReplyChildren;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class ChildAlertReplyPersonDTO {
    public ChildAlertReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    List<SubChildAlertReplyChildren> children;
    List<SubChildAlertReplyAdultFamily> identityFamily;
}

