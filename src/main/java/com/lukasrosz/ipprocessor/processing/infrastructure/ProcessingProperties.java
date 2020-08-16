package com.lukasrosz.ipprocessor.processing.infrastructure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "processing")
@Getter
@Setter
@ToString
public class ProcessingProperties {

    private String ipCountryInfoApiUrl;
    private double apiRateLimit;
    private int batchLimit;
}
