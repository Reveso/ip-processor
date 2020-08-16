package com.lukasrosz.ipprocessor.processing.infrastructure;

import com.google.common.util.concurrent.RateLimiter;
import com.lukasrosz.ipprocessor.processing.application.AddressProcessor;
import com.lukasrosz.ipprocessor.processing.application.AddressService;
import com.lukasrosz.ipprocessor.processing.model.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@RequiredArgsConstructor
public class ProcessingConfig {

    private final ProcessingProperties processingProperties;

    @Bean
    public AddressService addressService(AddressRepository addressRepository) {
        return new AddressService(addressRepository);
    }

    @Bean
    public AddressDatabaseRepository addressDatabaseRepository(JdbcTemplate jdbcTemplate) {
        return new AddressDatabaseRepository(jdbcTemplate);
    }

    @Bean
    public IpCountryInfoApi ipCountryInfoApi() {
        return new IpCountryInfoApi(processingProperties.getIpCountryInfoApiUrl());
    }

    @Bean
    public AddressProcessor addressProcessor(AddressService addressService, IpCountryInfoApi ipCountryInfoApi) {
        return new AddressProcessor(addressService, ipCountryInfoApi, processingProperties.getBatchLimit(),
                RateLimiter.create(processingProperties.getApiRateLimit()));
    }
}
