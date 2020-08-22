package com.lukasrosz.ipprocessor.processing.model.external;

import java.net.InetAddress;
import java.util.Optional;

public interface CountryInfoRepository {
    Optional<IpCountryInfo> getIpCountryInfo(InetAddress ipAddress);
}
