package com.lukasrosz.ipprocessor.processing.application;

import com.google.common.util.concurrent.RateLimiter;
import com.lukasrosz.ipprocessor.processing.infrastructure.IpCountryInfoApi;
import com.lukasrosz.ipprocessor.processing.model.AddressCountryDetails;
import com.lukasrosz.ipprocessor.processing.model.ProcessedAddress;
import com.lukasrosz.ipprocessor.processing.model.UnprocessedAddress;
import com.lukasrosz.ipprocessor.processing.model.external.IpCountryInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
@Slf4j
public class AddressProcessor {

    private final AddressService addressService;
    private final IpCountryInfoApi ipCountryInfoApi;
    private final int BATCH_LIMIT;
    private final RateLimiter rateLimiter;

    public void processAddresses() {
        List<UnprocessedAddress> unprocessedAddresses = addressService.getUnprocessedAddresses(BATCH_LIMIT);
        while (unprocessedAddresses.size() > 0) {
            Queue<ProcessedAddress> processedAddresses = new ConcurrentLinkedQueue<>();
            populateProcessedAddressesQueue(unprocessedAddresses, processedAddresses);
            addressService.update(processedAddresses);
            unprocessedAddresses = addressService.getUnprocessedAddresses(BATCH_LIMIT);
        }
    }

    private void populateProcessedAddressesQueue(List<UnprocessedAddress> unprocessedAddresses,
                                                 Queue<ProcessedAddress> processedAddresses) {
        unprocessedAddresses.parallelStream().forEach(unprocessedAddress -> {
            rateLimiter.acquire();
            ipCountryInfoApi.getIpCountryInfo(unprocessedAddress.getAddress()).ifPresent(ipCountryInfo -> {
                        val processedAddress = unprocessedAddress.process(fromIpCountryInfo(ipCountryInfo));
                        log.info("Adding to update list: {}", processedAddress);
                        processedAddresses.add(processedAddress);
                    }
            );
        });
    }

    private AddressCountryDetails fromIpCountryInfo(IpCountryInfo ipCountryInfo) {
        return AddressCountryDetails.builder()
                .countryCode(ipCountryInfo.getCountryCode())
                .countryCode3(ipCountryInfo.getCountryCode3())
                .countryName(ipCountryInfo.getCountryName())
                .countryEmoji(ipCountryInfo.getCountryEmoji())
                .build();
    }
}