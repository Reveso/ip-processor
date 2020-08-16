package com.lukasrosz.ipprocessor.processing.model.external;

import java.util.Optional;

public interface CountryInfoRepository {
    Optional<IpCountryInfo> getIpCountryInfo(String ipAddress);
}
