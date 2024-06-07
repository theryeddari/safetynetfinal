package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * DTO for returning email addresses of all residents in a specified city.
 */
@Getter
@AllArgsConstructor
@ToString
public class CommunityEmailReplyPersonDTO {
    /**
     * Constructor for Jackson serialization.
     */
    public CommunityEmailReplyPersonDTO() {
        // Constructor for Jackson serialization
    }

    private List<String> mail;
}
