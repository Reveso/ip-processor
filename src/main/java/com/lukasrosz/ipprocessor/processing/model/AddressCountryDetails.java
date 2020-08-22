package com.lukasrosz.ipprocessor.processing.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


@Value
@Builder
public class AddressCountryDetails {
    String countryCode;
    String countryCode3;
    String countryName;
    String countryEmoji;

    public AddressCountryDetails(@NonNull String countryCode, @NonNull String countryCode3, @NonNull String countryName, String countryEmoji) {
        if (countryCode.length() < 1) {
            throw new IllegalArgumentException("CountryCode cannot be empty");
        } else if (countryCode3.length() < 1) {
            throw new IllegalArgumentException("CountryCode3 cannot be empty");
        } else if (countryName.length() < 1) {
            throw new IllegalArgumentException("Country name cannot be empty");
        }

        this.countryCode = countryCode;
        this.countryCode3 = countryCode3;
        this.countryName = countryName;
        this.countryEmoji = countryEmoji;
    }
}
