package com.openclassrooms.safetynet.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

@Getter
@AllArgsConstructor
@ToString
public class PhoneAlertReplyPersonDTO {
    String phone;
}
