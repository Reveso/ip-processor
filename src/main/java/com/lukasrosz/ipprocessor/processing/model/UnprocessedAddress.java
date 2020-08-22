package com.lukasrosz.ipprocessor.processing.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;

@ToString
@Value
public class UnprocessedAddress {
    Long id;
    InetAddress address;

    public UnprocessedAddress(@NonNull Long id, @NonNull InetAddress address) {
        this.id = id;
        this.address = address;
    }

    public ProcessedAddress process(@NonNull AddressCountryDetails countryDetails) {
        return ProcessedAddress.builder()
                .id(this.id)
                .address(this.address)
                .countryDetails(countryDetails)
                .processedDate(new Timestamp(new Date().getTime()))
                .build();
    }
}
