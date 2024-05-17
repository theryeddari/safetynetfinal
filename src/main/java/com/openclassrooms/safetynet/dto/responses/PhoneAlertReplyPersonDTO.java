package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class PhoneAlertReplyPersonDTO {
    /**
     * DTO for returning a list of phone numbers of residents covered by a specific fire station,
     * to facilitate emergency text message alerts.
     */
    public PhoneAlertReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    List<String> phone;
}
