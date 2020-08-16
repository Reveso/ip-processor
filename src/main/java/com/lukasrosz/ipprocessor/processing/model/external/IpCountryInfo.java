package com.lukasrosz.ipprocessor.processing.model.external;

import lombok.Value;

@Value
public class IpCountryInfo {
    String countryCode;
    String countryCode3;
    String countryName;
    String countryEmoji;
}