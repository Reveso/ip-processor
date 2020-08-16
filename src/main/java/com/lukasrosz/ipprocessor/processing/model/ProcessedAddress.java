package com.lukasrosz.ipprocessor.processing.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.sql.Timestamp;

@ToString
@Builder
@Value
public class ProcessedAddress {
    Long id;
    String address;
    Boolean processed;
    AddressCountryDetails countryDetails;
    Timestamp processedDate;
}
