package com.lukasrosz.ipprocessor.processing.model;

import lombok.ToString;
import lombok.Value;

import java.sql.Timestamp;
import java.util.Date;

@ToString
@Value
public class UnprocessedAddress {
    Long id;
    String address;

    public ProcessedAddress process(AddressCountryDetails countryDetails) {
        return ProcessedAddress.builder()
                .id(this.id)
                .address(this.address)
                .processed(true)
                .countryDetails(countryDetails)
                .processedDate(new Timestamp(new Date().getTime()))
                .build();
    }
}
