package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CommunityEmailReplyPersonDTO {
    public CommunityEmailReplyPersonDTO(){
        //constructor for jackson serialisation
    }
    String mail;
}
