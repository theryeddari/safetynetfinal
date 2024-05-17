package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class CommunityEmailReplyPersonDTO {
    /**
     * DTO for returning email addresses of all residents in a specified city.
     */
    public CommunityEmailReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    List<String> mail;
}
