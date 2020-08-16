package com.lukasrosz.ipprocessor.processing.model;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class AddressCountryDetails {
    String countryCode;
    String countryCode3;
    String countryName;
    String countryEmoji;
}
