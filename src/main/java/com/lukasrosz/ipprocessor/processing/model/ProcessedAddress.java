package com.lukasrosz.ipprocessor.processing.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.net.InetAddress;
import java.sql.Timestamp;

@ToString
@Builder
@Value
public class ProcessedAddress {
    Long id;
    InetAddress address;
    AddressCountryDetails countryDetails;
    Timestamp processedDate;

    public ProcessedAddress(@NonNull Long id, @NonNull InetAddress address,
                            @NonNull AddressCountryDetails countryDetails, @NonNull Timestamp processedDate) {
        this.id = id;
        this.address = address;
        this.countryDetails = countryDetails;
        this.processedDate = processedDate;
    }
}
